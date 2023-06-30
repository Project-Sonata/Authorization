package com.odeyalo.sonata.authorization.service.authentication.manager;

import com.odeyalo.sonata.authorization.service.authentication.Subject;
import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import com.odeyalo.sonata.authorization.service.authentication.ParsedAuthenticationPayload;
import reactor.core.publisher.Mono;

/**
 * Bridge between {@link ReactiveAuthenticationManager} and real authentication generator.
 *
 * @see ReactiveTokenBasedAuthenticationProvider
 */
public interface ReactiveAuthenticationProvider {

    /**
     * Create and save(optionally) the access token
     * @param subject - subject to generate authentication to
     * @return - a new AuthenticationPayload that has not been issued previously, and is guaranteed to be recognised by this implementation's
     * verifyAuthentication(String) at any future time.
     */
    Mono<AuthenticationPayload> createAuthentication(Subject subject);
    /**
     * @param authenticationIdentifier - unique identifier of authentication
     * @return the parsed authentication, or null if the identifier of authentication was not issued by this AccessTokenManager or if the identifier is invalid
     */
    Mono<ParsedAuthenticationPayload> verifyAuthentication(String authenticationIdentifier);
}
