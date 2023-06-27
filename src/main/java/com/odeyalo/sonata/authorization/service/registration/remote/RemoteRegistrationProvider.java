package com.odeyalo.sonata.authorization.service.registration.remote;

import com.odeyalo.sonata.authorization.entity.Role;
import com.odeyalo.sonata.authorization.repository.storage.PersistableUser;
import com.odeyalo.sonata.authorization.repository.storage.UserStorage;
import com.odeyalo.sonata.authorization.service.registration.*;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationResult;
import com.odeyalo.sonata.authorization.support.GrantedAuthoritiesProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Set;

/**
 * Default registration provider that supports registration and confirmation in the same time
 */
@Service
public class RemoteRegistrationProvider implements ConfirmableRegistrationProvider {
    private final RemoteServiceUserRegistrar remoteRegistrar;
    private final GrantedAuthoritiesProvider authoritiesProvider;
    private final UserStorage userStorage;

    @Autowired
    public RemoteRegistrationProvider(RemoteServiceUserRegistrar remoteRegistrar, GrantedAuthoritiesProvider authoritiesProvider, UserStorage userStorage) {
        this.remoteRegistrar = remoteRegistrar;
        this.authoritiesProvider = authoritiesProvider;
        this.userStorage = userStorage;
    }

    @Override
    public Mono<RegistrationResult> registerUser(RegistrationForm form) {
        return Flux.from(remoteRegistrar.registerUser(form))
                .zipWith(authoritiesProvider.getAuthorities(Role.USER))
                .flatMap(tuple -> {
                    RegistrationResult result = tuple.getT1();
                    if (result.isCompleted()) {
                        Set<GrantedAuthority> authorities = tuple.getT2();
                        return saveUserLocally(result.getUserInfo(), Role.USER, authorities)
                                .thenReturn(result);
                    }
                    return Mono.just(result);
                }).next();
    }

    @Override
    public Mono<RegistrationConfirmationResult> confirmRegistration(RegistrationConfirmationData confirmationData) {
        return Flux.from(remoteRegistrar.confirmRegistration(confirmationData))
                .zipWith(authoritiesProvider.getAuthorities(Role.USER))
                .flatMap(tuple -> {
                    RemoteRegistrationConfirmationResult result = tuple.getT1();
                    if (result.isFailed()) {
                        return Mono.just(
                                RegistrationConfirmationResult.confirmationFailed(result.getErrorDetails())
                        );
                    }
                    Set<GrantedAuthority> authorities = tuple.getT2();
                    return saveUserLocally(result.getUserInfo(), Role.USER, authorities)
                            .map(RegistrationConfirmationResult::confirmedSuccessfully);
                }).next();
    }

    private Mono<RegisteredUser> saveUserLocally(BasicUserInfo info, Role role, Set<GrantedAuthority> grantedAuthorities) {
        PersistableUser user = PersistableUser
                .builder()
                .id(Long.parseLong(info.id()))
                .role(role)
                .username(info.username())
                .authorities(grantedAuthorities)
                .build();

        return userStorage.save(user)
                .subscribeOn(Schedulers.boundedElastic())
                .map(RegisteredUser::from);
    }
}
