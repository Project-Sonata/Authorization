package com.odeyalo.sonata.authorization.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;

import java.util.Map;

/**
 * Represent AccessToken in redis storage
 */
@RedisHash
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class RedisAccessToken extends BaseEntityImpl implements AccessToken {
    private String tokenValue;
    private Long expirationTime;
    private String userId;
    @Singular
    private Map<String, Object> claims;

    public RedisAccessToken(Long id, String businessKey, Long creationTime, String tokenValue, Long expirationTime, String userId, Map<String, Object> claims) {
        super(id, businessKey, creationTime);
        this.tokenValue = tokenValue;
        this.expirationTime = expirationTime;
        this.userId = userId;
        this.claims = claims;
    }

    public static RedisAccessToken copyFrom(AccessToken parent) {
        return RedisAccessToken
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

    @Override
    public RedisAccessToken setId(Long id) {
        super.setId(id);
        return this;
    }

    @Override
    public RedisAccessToken setBusinessKey(String businessKey) {
        super.setBusinessKey(businessKey);
        return this;
    }

    @Override
    public RedisAccessToken setCreationTime(Long creationTime) {
        super.setCreationTime(creationTime);
        return this;
    }

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
