package com.odeyalo.sonata.authorization.service.authentication;

/**
 * Payload that holds authentication identifier
 * and used as abstraction between different types of authentication(token, session or a custom one)
 */
public record AuthenticationPayload(String uniqueAuthenticationIdentifier,
                                    long lifetime,
                                    AuthenticationStrategy authenticationStrategy) {

    public static AuthenticationPayload of(String uniqueAuthenticationIdentifier, long lifetime, AuthenticationStrategy authenticationStrategy) {
        return new AuthenticationPayload(uniqueAuthenticationIdentifier, lifetime, authenticationStrategy);
    }

    /**
     * Unique authentication identifier that will be returned to user.
     * For example, if {@link AuthenticationStrategy#SESSION} is used, then this identifier will be set in
     * "Set-Cookie" header and will be returned as response.
     *
     * @return - unique authentication identifier
     */
    @Override
    public String uniqueAuthenticationIdentifier() {
        return uniqueAuthenticationIdentifier;
    }

    /**
     * Strategy that will be used to set authentication.
     */
    public enum AuthenticationStrategy {
        SESSION,
        TOKEN
    }
}
