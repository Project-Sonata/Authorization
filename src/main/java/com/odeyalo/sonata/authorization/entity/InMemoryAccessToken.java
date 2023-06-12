package com.odeyalo.sonata.authorization.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * Represent the access token that can be stored in memory.
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InMemoryAccessToken extends BaseEntityImpl implements AccessToken {
    private String tokenValue;
    private Long expirationTime;
    private String userId;

    public static InMemoryAccessToken copyFrom(AccessToken parent) {
        return InMemoryAccessToken
                .builder()
                .id(parent.getId())
                .businessKey(parent.getBusinessKey())
                .creationTime(parent.getCreationTime())
                .tokenValue(parent.getTokenValue())
                .expirationTime(parent.getExpirationTime())
                .claims(parent.getClaims())
                .userId(parent.getUserId())
                .build();
    }

    @Singular
    private Map<String, Object> claims;

    @Override
    public String getTokenValue() {
        return tokenValue;
    }

    @Override
    public Long getExpirationTime() {
        return expirationTime;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }
}
