package com.odeyalo.sonata.authorization.service.authentication;

import com.odeyalo.sonata.common.authentication.exception.LoginAuthenticationFailedException;
import reactor.core.publisher.Mono;

/**
 * AuthenticationProvider is a main interface to obtain authentication.
 */
public interface SonataAuthenticationProvider {

    /**
     * Method to obtain authentication.
     * @param username - user's name, an unique identifier of user
     * @param password - user's password
     * @return - ready-to-use authentication
     * @throws LoginAuthenticationFailedException - if username or password is invalid
     */
    Mono<SonataAuthentication> obtainAuthentication(String username, String password) throws LoginAuthenticationFailedException;

}