package com.odeyalo.sonata.authorization.service.authentication.manager;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import com.odeyalo.sonata.authorization.service.authentication.ParsedAuthenticationPayload;
import com.odeyalo.sonata.authorization.service.authentication.Subject;
import com.odeyalo.sonata.authorization.service.token.access.AccessTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

import static com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload.AuthenticationStrategy.SESSION;

/**
 * Uses session as authentication strategy. set the
 */
@Component
@Profile("session-authentication")
public class ReactiveSessionBasedAuthenticationProvider implements ReactiveAuthenticationProvider {
    private final AccessTokenManager accessTokenManager;

    @Autowired
    public ReactiveSessionBasedAuthenticationProvider(AccessTokenManager accessTokenManager) {
        this.accessTokenManager = accessTokenManager;
    }

    @Override
    public Mono<AuthenticationPayload> createAuthentication(Subject subject) {
        return accessTokenManager.createToken(subject)
                .map(token -> AuthenticationPayload.of(token.getTokenValue(), calculateAuthenticationLifetime(token), SESSION));
    }

    @Override
    public Mono<ParsedAuthenticationPayload> verifyAuthentication(String authenticationIdentifier) {
        return accessTokenManager.verifyToken(authenticationIdentifier)
                .map(token -> convertToParsedAuthenticationPayload(authenticationIdentifier, token));
    }


    private ParsedAuthenticationPayload convertToParsedAuthenticationPayload(String authenticationIdentifier, AccessToken token) {
        return ParsedAuthenticationPayload.builder()
                .authenticationIdentifier(authenticationIdentifier)
                .authenticationStrategy(SESSION)
                .userId(token.getUserId())
                .claims(token.getClaims())
                .expirationTime(token.getExpirationTime())
                .build();
    }

    private long calculateAuthenticationLifetime(AccessToken token) {
        return TimeUnit.MILLISECONDS.toSeconds(token.getExpirationTime()) - TimeUnit.MILLISECONDS.toSeconds(token.getCreationTime());
    }
}
