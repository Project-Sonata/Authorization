package com.odeyalo.sonata.authorization.service.authentication;

import com.odeyalo.sonata.common.authentication.dto.LoginCredentials;
import com.odeyalo.sonata.common.authentication.exception.LoginAuthenticationFailedException;
import com.odeyalo.sonata.suite.reactive.client.ReactiveAuthenticationClient;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * SonataAuthenticationProvider implementation that provide authentication using
 * the {@link ReactiveAuthenticationClient}
 */
@Component
public class RemoteSonataAuthenticationProvider implements SonataAuthenticationProvider {
    private final ReactiveAuthenticationClient reactiveAuthenticationClient;

    public RemoteSonataAuthenticationProvider(ReactiveAuthenticationClient reactiveAuthenticationClient) {
        this.reactiveAuthenticationClient = reactiveAuthenticationClient;
    }

    @Override
    public Mono<SonataAuthentication> obtainAuthentication(String username, String password) throws LoginAuthenticationFailedException {
        return reactiveAuthenticationClient.login(LoginCredentials.of(username, password))
                .flatMap(HttpEntity::getBody)
                .map(body -> SonataAuthentication.of(body.getUserInfo(), password, body.getUserInfo(), Set.of(new SimpleGrantedAuthority("USER"))))
                .onErrorResume(ex -> true, Mono::error);
    }
}
