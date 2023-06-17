package com.odeyalo.sonata.authorization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenIntrospectionRequest {
    @JsonProperty("token")
    private String token;

    public static TokenIntrospectionRequest of(String token) {
        return new TokenIntrospectionRequest(token);
    }
}
