package com.odeyalo.sonata.authorization.service.registration.remote;

import com.odeyalo.sonata.authorization.service.registration.BasicUserInfo;
import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import com.odeyalo.sonata.authorization.support.mappers.UserRegistrationInfoMapper;
import com.odeyalo.sonata.common.authentication.dto.request.ConfirmationCodeRequestDto;
import com.odeyalo.sonata.common.authentication.dto.request.UserRegistrationInfo;
import com.odeyalo.sonata.common.authentication.exception.ConfirmationCodeException;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import com.odeyalo.sonata.common.shared.GenericApiException;
import com.odeyalo.sonata.suite.reactive.client.ReactiveAuthenticationClient;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * User registrar that uses feign client to send HTTP requests to Authentication microservice
 */
@Component
public class ReactiveFeignClientRemoteServiceUserRegistrar implements RemoteServiceUserRegistrar {
    private final ReactiveAuthenticationClient authenticationClient;
    private final UserRegistrationInfoMapper registrationInfoMapper;
    public static final ErrorDetails CONFIRMATION_FAILED_DETAILS = ErrorDetails.of(
            "invalid_confirmation_data",
            "Data is invalid or expired",
            "To fix the problem - input correct confirmation data or regenerate it");

    public ReactiveFeignClientRemoteServiceUserRegistrar(ReactiveAuthenticationClient authenticationClient, UserRegistrationInfoMapper registrationInfoMapper) {
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
        return Mono.just(new ConfirmationCodeRequestDto(data.getData()))
                .flatMap(authenticationClient::confirmEmail)
                .flatMap(HttpEntity::getBody)
                .map(body -> {
                    BasicUserInfo user = BasicUserInfo.of(body.getUserInfo().getId(), body.getUserInfo().getEmail());
                    return RemoteRegistrationConfirmationResult.confirmedSuccessfully(user);
                })
                .onErrorResume(
                        (error) -> error instanceof ConfirmationCodeException,
                        (error) -> {
                            ConfirmationCodeException ex = (ConfirmationCodeException) error;
                            return Mono.fromCallable(() -> {
                                        ErrorDetails details = Objects.requireNonNullElse(ex.getErrorDetails(), CONFIRMATION_FAILED_DETAILS);
                                        return RemoteRegistrationConfirmationResult.confirmationFailed(details);
                                    }
                            );
                        });
    }
}
