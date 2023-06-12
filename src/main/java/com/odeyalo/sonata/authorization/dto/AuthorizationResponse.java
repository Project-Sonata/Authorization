package com.odeyalo.sonata.authorization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.odeyalo.sonata.authorization.entity.AccessToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * Base response for success authorization
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationResponse {
    private String token;
    // Number of seconds after this token will be invalid
    @JsonProperty("expires_in")
    private long expiresIn;
    @JsonProperty("user_id")
    private String userId;

    public static AuthorizationResponse from(AccessToken token) {
        return new AuthorizationResponse(token.getTokenValue(), getExpiresIn(token), token.getUserId());
    }

    private static long getExpiresIn(AccessToken token) {
        return TimeUnit.MILLISECONDS.toSeconds(token.getExpirationTime()) - TimeUnit.MILLISECONDS.toSeconds(token.getCreationTime());
    }
}
