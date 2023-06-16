package com.odeyalo.sonata.authorization.service.token.access.generator;

/**
 * More specific interface to generate ONLY OPAQUE tokens.
 * Implementation MUST NOT generate JWT or other types of self-contained tokens.
 */
public interface OpaqueAccessTokenGenerator extends AccessTokenGenerator {

}
