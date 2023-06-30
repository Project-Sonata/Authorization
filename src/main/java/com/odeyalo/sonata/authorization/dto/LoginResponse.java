package com.odeyalo.sonata.authorization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
public class LoginResponse {
    @JsonProperty("identifier")
    private  String loginIdentifier;
    @JsonProperty("strategy")
    private AuthenticationPayload.AuthenticationStrategy authenticationStrategy;
    @JsonProperty("lifetime")
    private long lifetime;
}
