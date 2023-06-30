package com.odeyalo.sonata.authorization.service.login;

import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DetaultReactiveLoginManager implements ReactiveLoginManager {
    private final ReactiveLoginProvider reactiveLoginProvider;

    public DetaultReactiveLoginManager(ReactiveLoginProvider reactiveLoginProvider) {
        this.reactiveLoginProvider = reactiveLoginProvider;
    }

    @Override
    public Mono<AuthenticationPayload> login(String username, String password) {
        return reactiveLoginProvider.login(username, password);
    }
}
