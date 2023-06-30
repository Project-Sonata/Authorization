package com.odeyalo.sonata.authorization.service.authentication.manager;

import com.odeyalo.sonata.authorization.service.authentication.Subject;
import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import com.odeyalo.sonata.authorization.service.authentication.ParsedAuthenticationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DetaultReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final ReactiveAuthenticationProvider authenticationProvider;

    @Autowired
    public DetaultReactiveAuthenticationManager(ReactiveAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public Mono<AuthenticationPayload> createAuthentication(Subject subject) {
        return authenticationProvider.createAuthentication(subject);
    }

    @Override
    public Mono<ParsedAuthenticationPayload> verifyAuthentication(String authenticationIdentifier) {
        return authenticationProvider.verifyAuthentication(authenticationIdentifier);
    }
}
