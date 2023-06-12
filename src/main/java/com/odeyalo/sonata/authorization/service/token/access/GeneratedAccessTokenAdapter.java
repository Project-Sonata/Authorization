package com.odeyalo.sonata.authorization.service.token.access;

import com.odeyalo.sonata.authorization.entity.AccessToken;

import java.util.Map;

/**
 * Simple adapter to adapt the GeneratedAccessToken to AccessToken
 * Notice that this class does not create any ID and business key and MUST NOT be directly saved to storage
 */
public class GeneratedAccessTokenAdapter implements AccessToken {
    private final GeneratedAccessToken token;

    public GeneratedAccessTokenAdapter(GeneratedAccessToken token) {
        this.token = token;
    }

    public static GeneratedAccessTokenAdapter from(GeneratedAccessToken token) {
        return new GeneratedAccessTokenAdapter(token);
    }

    @Override
    public String getTokenValue() {
        return token.getTokenValue();
    }

    @Override
    public Long getExpirationTime() {
        return token.getExpiresInMs();
    }

    @Override
    public String getUserId() {
        return token.getUserId();
    }

    @Override
    public Map<String, Object> getClaims() {
        return token.getClaims();
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getBusinessKey() {
        return null;
    }

    @Override
    public Long getCreationTime() {
        return token.getCreationTimeMs();
    }

    @Override
    public String toString() {
        return "GeneratedAccessTokenAdapter(token=" + token + ")";
    }
}
