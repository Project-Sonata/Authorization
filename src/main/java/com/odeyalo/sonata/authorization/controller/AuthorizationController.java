package com.odeyalo.sonata.authorization.controller;

import com.odeyalo.sonata.authorization.dto.AuthorizationResponse;
import com.odeyalo.sonata.authorization.dto.SuccessfulRegistrationResponse;
import com.odeyalo.sonata.authorization.service.TokenBasedAuthenticatorFacade;
import com.odeyalo.sonata.common.authentication.dto.LoginCredentials;
import com.odeyalo.sonata.common.authentication.exception.LoginAuthenticationFailedException;
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

    @Autowired
    public AuthorizationController(TokenBasedAuthenticatorFacade authenticatorFacade) {
        this.authenticatorFacade = authenticatorFacade;
    }

    @PostMapping("/login")
    public Mono<?> loginUser(@RequestBody LoginCredentials loginCredentials) throws LoginAuthenticationFailedException {
        return authenticatorFacade.authenticateAndCreateToken(loginCredentials.getEmail(), loginCredentials.getPassword())
                .map(AuthorizationResponse::from);
    }

    @PostMapping("/register")
    public Mono<?> registerUser() {
        return Mono.just(SuccessfulRegistrationResponse.of("token", 3600));
    }
}
