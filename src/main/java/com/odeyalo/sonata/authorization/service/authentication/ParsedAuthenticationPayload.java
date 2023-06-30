package com.odeyalo.sonata.authorization.service.authentication;

import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload.AuthenticationStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Map;

/**
 * Contain the parsed information about authentication
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class ParsedAuthenticationPayload {
    String authenticationIdentifier;
    AuthenticationStrategy authenticationStrategy;
    // User  associated with this authentication
    String userId;
    // Expiration time of this authentication
    Long expirationTime;
    // Claims of the authentication, that can contain something.
    // Real-world example: claims contain scopes for user
    // Empty map must be returned if authentication does not contain any claims
    @Singular
    Map<String, Object> claims;
}
