package com.odeyalo.sonata.authorization.service.registration.manager;

import com.odeyalo.sonata.authorization.service.authentication.Subject;
import com.odeyalo.sonata.authorization.service.authentication.manager.ReactiveAuthenticationManager;
import com.odeyalo.sonata.authorization.service.registration.ConfirmableRegistrationProvider;
import com.odeyalo.sonata.authorization.service.registration.RegisteredUser;
import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class DefaultRegistrationManager implements RegistrationManager {
    private final ConfirmableRegistrationProvider registrationProvider;
    private final ReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<RegistrationAnswer> registerUser(RegistrationForm registrationForm) {
        return registrationProvider.registerUser(registrationForm)
                .map(result -> {
                    if (result.isFailed()) {
                        return RegistrationAnswer.failed(result.getErrorDetails());
                    }
                    return RegistrationAnswer.pendingConfirmation();
                });
    }

    @Override
    public Mono<RegistrationConfirmationAnswer> confirmRegistration(RegistrationConfirmationData data) {
        return registrationProvider.confirmRegistration(data)
                .flatMap(result -> {
                    if (result.isFailed()) {
                        RegistrationConfirmationAnswer answer = RegistrationConfirmationAnswer.confirmationFailed(result.getErrorDetails());
                        return Mono.just(answer);
                    }
                    RegisteredUser user = result.getRegisteredUser();
                    Subject subject = Subject.of(String.valueOf(user.getId()), user.getUsername(), user.getRole(), user.getGrantedAuthorities());
                    return authenticationManager.createAuthentication(subject)
                            .map(RegistrationConfirmationAnswer::successfullyConfirmed);
                });
    }
}