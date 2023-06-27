package com.odeyalo.sonata.authorization.service.registration.remote;

import com.odeyalo.sonata.authorization.service.registration.BasicUserInfo;
import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import com.odeyalo.sonata.authorization.support.mappers.UserRegistrationInfoMapper;
import com.odeyalo.sonata.common.authentication.dto.request.UserRegistrationInfo;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import com.odeyalo.sonata.common.shared.GenericApiException;
import com.odeyalo.sonata.suite.reactive.client.ReactiveAuthenticationClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * User registrar that uses feign client to send HTTP requests to Authentication microservice
 */
@Component
public class ReactiveFeignClientRemoteServiceUserRegistrar implements RemoteServiceUserRegistrar {
    private final ReactiveAuthenticationClient authenticationClient;
    private final UserRegistrationInfoMapper registrationInfoMapper;

    public ReactiveFeignClientRemoteServiceUserRegistrar(ReactiveAuthenticationClient authenticationClient, UserRegistrationInfoMapper registrationInfoMapper) {
        System.out.println(authenticationClient);
        this.authenticationClient = authenticationClient;
        this.registrationInfoMapper = registrationInfoMapper;
    }

    @Override
    public Mono<RegistrationResult> registerUser(RegistrationForm registrationForm) {
        UserRegistrationInfo registrationInfo = registrationInfoMapper.fromRegistrationForm(registrationForm);
        return authenticationClient
                .registerUser(registrationInfo)
                .flatMap(r -> r.getBody() != null ? r.getBody() : Mono.empty())
                .map(body -> RegistrationResult.pendingConfirmation(null, BasicUserInfo.of(null, registrationForm.getUsername())))
                .onErrorResume(error -> {
                    GenericApiException ex = (GenericApiException) error;
                    ErrorDetails errorDetails = ex.getErrorDetails();
                    return Mono.just(RegistrationResult.failed(errorDetails));
                })
                .checkpoint("Register user in remote service using Feign Reactive");
    }

    @Override
    public Mono<RemoteRegistrationConfirmationResult> confirmRegistration(RegistrationConfirmationData data) {
        return null;
    }
}
