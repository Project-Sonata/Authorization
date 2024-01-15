package com.odeyalo.sonata.authorization.service.token.access;

import com.odeyalo.sonata.authorization.entity.AccessToken;

/**
 * Simple adapter to adapt the GeneratedAccessToken to AccessToken
 * Notice that this class does not create any ID and business key and MUST NOT be directly saved to storage
 */
public class GeneratedAccessTokenAdapter {
    private final GeneratedAccessToken token;

    public GeneratedAccessTokenAdapter(GeneratedAccessToken token) {
        this.token = token;
    }

    public static GeneratedAccessTokenAdapter from(GeneratedAccessToken token) {
        return new GeneratedAccessTokenAdapter(token);
    }

    public AccessToken toAccessToken() {
        return AccessToken
                .builder()
                .creationTime(token.getCreationTimeMs())
                .tokenValue(token.getTokenValue())
                .expirationTime(token.getExpiresInMs())
                .claims(token.getClaims())
                .userId(token.getUserId())
                .build();
    }
}
