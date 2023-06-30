package com.odeyalo.sonata.authorization.service.login;

import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import reactor.core.publisher.Mono;

/**
 * Bridge between ReactiveLoginManager and real login process
 */
public interface ReactiveLoginProvider {

    Mono<AuthenticationPayload> login(String username, String password);

}
