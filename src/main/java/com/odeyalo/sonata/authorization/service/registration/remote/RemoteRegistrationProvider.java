package com.odeyalo.sonata.authorization.service.registration.remote;

import com.odeyalo.sonata.authorization.entity.Role;
import com.odeyalo.sonata.authorization.entity.User;
import com.odeyalo.sonata.authorization.repository.user.ReactiveUserRepository;
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
    private final ReactiveUserRepository userRepository;

    @Autowired
    public RemoteRegistrationProvider(RemoteServiceUserRegistrar remoteRegistrar, GrantedAuthoritiesProvider authoritiesProvider, ReactiveUserRepository userRepository) {
        this.remoteRegistrar = remoteRegistrar;
        this.authoritiesProvider = authoritiesProvider;
        this.userRepository = userRepository;
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
        User user = User
                .builder()
                .id(Long.parseLong(info.id()))
                .role(role.getRoleValue())
                .username(info.username())
                .grantedAuthorities(grantedAuthorities)
                .build();

        return userRepository.save(user)
                .subscribeOn(Schedulers.boundedElastic())
                .map(RegisteredUser::from);
    }
}
