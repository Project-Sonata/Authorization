package com.odeyalo.sonata.authorization.controller;

import com.odeyalo.sonata.authorization.dto.AuthorizationResponse;
import com.odeyalo.sonata.authorization.dto.RegistrationFormDto;
import com.odeyalo.sonata.authorization.dto.RegistrationResultResponseDto;
import com.odeyalo.sonata.authorization.service.TokenBasedAuthenticatorFacade;
import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.authorization.service.registration.RegistrationProvider;
import com.odeyalo.sonata.authorization.support.mappers.RegistrationResultResponseDtoMapper;
import com.odeyalo.sonata.common.authentication.dto.LoginCredentials;
import com.odeyalo.sonata.common.authentication.exception.LoginAuthenticationFailedException;
import com.odeyalo.sonata.common.authentication.exception.RegistrationFailedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/authorization/")
@Log4j2
public class AuthorizationController {

    private final TokenBasedAuthenticatorFacade authenticatorFacade;
    private final RegistrationProvider registrationProvider;
    private final RegistrationResultResponseDtoMapper mapper;

    @Autowired
    public AuthorizationController(TokenBasedAuthenticatorFacade authenticatorFacade, RegistrationProvider registrationProvider, RegistrationResultResponseDtoMapper mapper) {
        this.authenticatorFacade = authenticatorFacade;
        this.registrationProvider = registrationProvider;
        this.mapper = mapper;
    }

    @PostMapping("/login")
    public Mono<?> loginUser(@RequestBody LoginCredentials loginCredentials) throws LoginAuthenticationFailedException {
        return authenticatorFacade.authenticateAndCreateToken(loginCredentials.getEmail(), loginCredentials.getPassword())
                .map(AuthorizationResponse::from);
    }

    @PostMapping("/register")
    public Mono<?> registerUser(@RequestBody RegistrationFormDto form) {
        RegistrationForm registrationForm = RegistrationForm.of(form.getUsername(), form.getPassword(), form.getGender(), form.getBirthdate(), form.isEnableNotification());
        return registrationProvider.registerUser(registrationForm)
                .flatMap(result -> {
                    if (result.isFailed()) {
                        return Mono.error(new RegistrationFailedException(result.getErrorDetails()));
                    }
                    RegistrationResultResponseDto responseBody = mapper.from(result);
                    return Mono.just(responseBody);
                });
    }
}
