package com.odeyalo.sonata.authorization.service.authentication;

import com.odeyalo.sonata.authorization.entity.Role;
import com.odeyalo.sonata.authorization.service.registration.BasicUserInfo;
import com.odeyalo.sonata.common.authentication.dto.LoginCredentials;
import com.odeyalo.sonata.common.authentication.exception.LoginAuthenticationFailedException;
import com.odeyalo.sonata.suite.reactive.client.ReactiveAuthenticationClient;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
                .map(body -> {
                    BasicUserInfo userInfo = BasicUserInfo.of(body.getUserInfo().getId(), body.getUserInfo().getEmail());
                    return SonataAuthentication.builder()
                            .principal(userInfo)
                            .credentials(password)
                            .role(Role.USER.getRoleValue()) // todo: get the value from database
                            .userInfo(userInfo)
                            .build();
                })
                .onErrorResume(ex -> true, Mono::error);
    }
}
