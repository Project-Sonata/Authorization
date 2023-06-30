package com.odeyalo.sonata.authorization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenIntrospectionResponse {
    @JsonProperty("valid")
    private boolean valid;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("iat")
    private long issuedAt;
    @JsonProperty("expires_in")
    private long expiresIn;

    protected TokenIntrospectionResponse(boolean valid) {
        this.valid = valid;
    }

    public static TokenIntrospectionResponse valid(String userId, String scope, long iat, long expiresIn) {
        return new TokenIntrospectionResponse(true, userId, scope, iat, expiresIn);
    }

    public static TokenIntrospectionResponse invalid() {
        return new TokenIntrospectionResponse(false);
    }
}