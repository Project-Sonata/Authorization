package com.odeyalo.sonata.authorization.service.token.oauth2;

import lombok.*;
import org.jetbrains.annotations.NotNull;

/**
 * Represent access token in Oauth2 Standard
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class Oauth2AccessToken {
    @NotNull
    String tokenValue;
    long issuedAt;
    long expireTime;
    @NotNull
    ScopeContainer scopes;
}
