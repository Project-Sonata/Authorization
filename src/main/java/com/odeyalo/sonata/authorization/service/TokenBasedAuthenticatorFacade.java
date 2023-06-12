package com.odeyalo.sonata.authorization.service;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import reactor.core.publisher.Mono;

/**
 * Simple facade interface to authenticate the user and return the access token as result
 */
public interface TokenBasedAuthenticatorFacade {

    Mono<AccessToken> authenticateAndCreateToken(String username, String password);

}
