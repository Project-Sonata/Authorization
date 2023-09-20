package com.odeyalo.sonata.authorization.controller;

import com.odeyalo.sonata.authorization.dto.*;
import com.odeyalo.sonata.authorization.exception.RegistrationConfirmationFailedException;
import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import com.odeyalo.sonata.authorization.service.login.ReactiveLoginManager;
import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import com.odeyalo.sonata.authorization.service.registration.manager.RegistrationAnswer;
import com.odeyalo.sonata.authorization.service.registration.manager.RegistrationManager;
import com.odeyalo.sonata.authorization.support.web.enhancer.LoginEndpointResponseEnhancer;
import com.odeyalo.sonata.common.authentication.dto.LoginCredentials;
import com.odeyalo.sonata.common.authentication.exception.LoginAuthenticationFailedException;
import com.odeyalo.sonata.common.authentication.exception.RegistrationFailedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.authorization.dto.SuccessfulRegistrationConfirmationDto.ACCESS_TOKEN_KEY;

@RestController
@RequestMapping("/authorization/")
@Log4j2
public class AuthorizationController {
    private final ReactiveLoginManager loginManager;
    private final RegistrationManager registrationManager;
    private final LoginEndpointResponseEnhancer loginEndpointResponseEnhancer;

    @Autowired
    public AuthorizationController(ReactiveLoginManager loginManager, RegistrationManager registrationManager, LoginEndpointResponseEnhancer loginEndpointResponseEnhancer) {
        this.loginManager = loginManager;
        this.registrationManager = registrationManager;
        this.loginEndpointResponseEnhancer = loginEndpointResponseEnhancer;
    }

    @PostMapping("/login")
    public Mono<?> loginUser(@RequestBody LoginCredentials loginCredentials, ServerWebExchange exchange) throws LoginAuthenticationFailedException {
        return loginManager.login(loginCredentials.getEmail(), loginCredentials.getPassword())
                .flatMap(res -> loginEndpointResponseEnhancer.enhance(res, exchange).map(webExchange -> res))
                .map(res -> LoginResponse.of(res.uniqueAuthenticationIdentifier(), res.authenticationStrategy(), res.lifetime()));
    }

    @PostMapping("/register")
    public Mono<?> registerUser(@RequestBody RegistrationFormDto form) {
        RegistrationForm registrationForm = RegistrationForm.of(form.getUsername(), form.getPassword(), form.getGender(), form.getBirthdate(), form.isEnableNotification());
        return registrationManager.registerUser(registrationForm)
                .flatMap(result -> {
                    if (result.getRegistrationStatus() == RegistrationResult.Type.FAILED) {
                        return Mono.error(new RegistrationFailedException(result.getErrorDetails()));
                    }
                    RegistrationResultResponseDto responseDto = getRegistrationResultResponseDto(result);
                    return Mono.just(responseDto);
                });
    }

    @PostMapping("/register/confirm")
    public Mono<RegistrationConfirmationDto> confirmUserRegistration(@RequestBody RegistrationConfirmationDataDto data) {
        return Mono.just(data)
                .flatMap(body -> {
                    RegistrationConfirmationData confirmationData = RegistrationConfirmationData.of(body.getData());
                    return registrationManager.confirmRegistration(confirmationData);
                })
                .flatMap(result -> {
                    if (result.isConfirmed()) {
                        AuthenticationPayload payload = result.getAuthenticationPayload();
                        TokensDto dto = TokensDto.builder().token(ACCESS_TOKEN_KEY, TokenDto.of(payload.uniqueAuthenticationIdentifier(), payload.lifetime())).build();
                        return Mono.just(SuccessfulRegistrationConfirmationDto.of(dto));
                    }
                    return Mono.error(new RegistrationConfirmationFailedException("Confirmation has been failed"));
                });
    }

    private static RegistrationResultResponseDto getRegistrationResultResponseDto(RegistrationAnswer result) {
        return RegistrationResultResponseDto
                .builder()
                .registrationType(result.getRegistrationStatus())
                .errorDetails(result.getErrorDetails())
                .build();
    }
}
