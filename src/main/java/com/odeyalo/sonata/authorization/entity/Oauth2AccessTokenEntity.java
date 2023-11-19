package com.odeyalo.sonata.authorization.entity;

import com.odeyalo.sonata.authorization.support.scope.ScopeContainer;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

/**
 * Represent Oauth2AccessToken saved in any kind of database
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Oauth2AccessTokenEntity {
    Long id;
    String tokenValue;
    long issuedAtSeconds;
    long expiresInSeconds;
    ScopeContainer scopes;

    public boolean isValid() {
        return expiresInSeconds < Instant.now().getEpochSecond();
    }
}
