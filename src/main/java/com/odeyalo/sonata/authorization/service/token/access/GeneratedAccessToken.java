package com.odeyalo.sonata.authorization.service.token.access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Map;

/**
 * Immutable class that represent the generated access token by {@link com.odeyalo.sonata.authorization.service.token.access.generator.AccessTokenGenerator}
 */
@AllArgsConstructor(staticName = "of")
@Value(staticConstructor = "of")
@Builder
public class GeneratedAccessToken {
    String tokenValue;
    Long creationTimeMs;
    Long expiresInMs;
    String userId;
    @Singular
    Map<String, Object> claims;
}
