package com.odeyalo.sonata.authorization.service.oauth2;

import com.odeyalo.sonata.authorization.entity.Role;
import com.odeyalo.sonata.authorization.service.authentication.SonataAuthentication;
import com.odeyalo.sonata.authorization.service.registration.BasicUserInfo;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import com.odeyalo.sonata.common.authentication.exception.LoginAuthenticationFailedException;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

class DefaultResourceOwnerAuthenticationManagerTest {

    @Test
    void shouldReturnAuthenticationForValidCredentials() {
        DefaultResourceOwnerAuthenticationManager testable = new DefaultResourceOwnerAuthenticationManager(
                (username, password) -> Mono.just(
                        new SonataAuthentication("odeyalo", "password",
                                BasicUserInfo.of("123", "odeyalo"),
                                Role.USER,
                                Collections.emptyList())
                ));

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("username=odeyalo&password=password")
        );

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(AuthenticatedResourceOwnerAuthentication::isAuthenticated)
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthenticationExceptionForInvalidCredentials() {
        DefaultResourceOwnerAuthenticationManager testable = new DefaultResourceOwnerAuthenticationManager(
                (username, password) -> Mono.error(
                        new LoginAuthenticationFailedException(ErrorDetails.of("invalid_credentials", "", ""))
                ));

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("username=invalid&password=password")
        );

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .expectError(ResourceOwnerAuthenticationException.class)
                .verify();
    }
}