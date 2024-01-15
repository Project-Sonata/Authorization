package com.odeyalo.sonata.authorization.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * Represent the access token that can be stored in memory.
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class AccessToken extends BaseEntityImpl {
    private String tokenValue;
    private Long expirationTime;
    private String userId;
    @Singular
    private Map<String, Object> claims;

    public static AccessToken copyFrom(AccessToken parent) {
        return AccessToken
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
}
