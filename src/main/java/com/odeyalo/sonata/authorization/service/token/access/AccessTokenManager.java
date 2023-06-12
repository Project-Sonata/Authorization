package com.odeyalo.sonata.authorization.service.token.access;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.entity.User;
import reactor.core.publisher.Mono;

/**
 * A central interface to manage access tokens
 *
 * Note: THIS INTERFACE MUST NOT BE USED AS OAUTH2 TOKENS PROVIDER
 */
public interface AccessTokenManager {
    /**
     * Create and save(optionally) the access token
     * @param user - user to generate token to
     * @return - a new token that has not been issued previously, and is guaranteed to be recognised by this implementation's verifyToken(String) at any future time.
     */
    Mono<AccessToken> createToken(User user);

    /**
     * @param tokenValue - value of the token
     * @return the token, or null if the token was not issued by this AccessTokenManager or if the token is invalid
     */

    Mono<AccessToken> verifyToken(String tokenValue);

}
