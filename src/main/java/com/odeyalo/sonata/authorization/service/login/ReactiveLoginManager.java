package com.odeyalo.sonata.authorization.service.login;

import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import reactor.core.publisher.Mono;

public interface ReactiveLoginManager {

    Mono<AuthenticationPayload> login(String username, String password);

}
