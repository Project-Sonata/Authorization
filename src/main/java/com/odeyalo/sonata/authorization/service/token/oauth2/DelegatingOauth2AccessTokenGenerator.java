package com.odeyalo.sonata.authorization.service.token.oauth2;

import com.odeyalo.sonata.authorization.entity.Role;
import com.odeyalo.sonata.authorization.support.scope.CommonScope;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessToken;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerationContext;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Generate the {@link Oauth2AccessToken} using the {@link Oauth2AccessTokenManager}
 */
@Component
public class DelegatingOauth2AccessTokenGenerator implements Oauth2AccessTokenGenerator {
    private final Oauth2AccessTokenManager oauth2AccessTokenManager;

    public DelegatingOauth2AccessTokenGenerator(Oauth2AccessTokenManager oauth2AccessTokenManager) {
        this.oauth2AccessTokenManager = oauth2AccessTokenManager;
    }

    @Override
    public @NotNull Mono<Oauth2AccessToken> generateToken(@NotNull Oauth2AccessTokenGenerationContext context) {

        List<Scope> scopes = context.getScopes().stream()
                .map(scope -> new CommonScope(scope.getName(), "unknown", Set.of(Role.USER.getRoleValue()), Scope.Type.PUBLIC))
                .map(scope -> (Scope) scope)
                .toList();

        return oauth2AccessTokenManager.generateAccessToken(context.getResourceOwner().getPrincipal(), scopes)
                .map(generatedOauth2Token -> mapToOauth2AccessToken(context, generatedOauth2Token));
    }

    private static Oauth2AccessToken mapToOauth2AccessToken(@NotNull Oauth2AccessTokenGenerationContext context,
                                                            com.odeyalo.sonata.authorization.service.token.oauth2.Oauth2AccessToken generatedOauth2Token) {
        return Oauth2AccessToken.builder()
                .tokenValue(generatedOauth2Token.getTokenValue())
                .tokenType(Oauth2AccessToken.TokenType.BEARER)
                .issuedAt(Instant.ofEpochSecond(generatedOauth2Token.getIssuedAt()))
                .expiresIn(Instant.ofEpochSecond(generatedOauth2Token.getExpireTime()))
                .scopes(context.getScopes())
                .build();
    }
}
