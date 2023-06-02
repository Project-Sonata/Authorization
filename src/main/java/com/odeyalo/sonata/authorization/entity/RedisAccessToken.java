package com.odeyalo.sonata.authorization.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Map;

/**
 * Represent AccessToken in redis storage
 */
@RedisHash
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RedisAccessToken extends BaseEntityImpl implements AccessToken {
    @Id
    private String tokenValue;
    private Long expirationTime;
    private String userId;
    private Map<String, Object> claims;

    public RedisAccessToken(Long id, String businessKey, Long creationTime, String tokenValue, Long expirationTime, String userId, Map<String, Object> claims) {
        super(id, businessKey, creationTime);
        this.tokenValue = tokenValue;
        this.expirationTime = expirationTime;
        this.userId = userId;
        this.claims = claims;
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
        return null;
    }
}
