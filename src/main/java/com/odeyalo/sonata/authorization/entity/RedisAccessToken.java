package com.odeyalo.sonata.authorization.entity;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
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
@AllArgsConstructor
@SuperBuilder
@Accessors(chain = true)
public class RedisAccessToken {
    @Id
    protected Long id;
    protected String businessKey;
    protected Long creationTime;
    private String tokenValue;
    private Long expirationTime;
    private String userId;
    @Singular
    private Map<String, Object> claims;

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

    public AccessToken toAccessToken() {
        return AccessToken
                .builder()
                .id(getId())
                .businessKey(getBusinessKey())
                .creationTime(getCreationTime())
                .tokenValue(getTokenValue())
                .expirationTime(getExpirationTime())
                .claims(getClaims())
                .userId(getUserId())
                .build();
    }
}
