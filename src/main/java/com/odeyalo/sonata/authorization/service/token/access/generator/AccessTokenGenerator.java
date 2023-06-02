package com.odeyalo.sonata.authorization.service.token.access.generator;

import com.odeyalo.sonata.authorization.service.token.access.GeneratedAccessToken;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Generator to generate access tokens
 */
public interface AccessTokenGenerator {
    /**
     * Generate new access token
     * @param userId - user's id that associated with this token. In other words - token's owner
     * @param body - claims or body of the token.
     * @return - fully constructed GeneratedAccessToken with presented all fields
     */
    Mono<GeneratedAccessToken> generateAccessToken(String userId, Map<String, Object> body);

}
