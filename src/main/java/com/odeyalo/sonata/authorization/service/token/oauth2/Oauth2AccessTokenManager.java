package com.odeyalo.sonata.authorization.service.token.oauth2;

import com.odeyalo.sonata.authorization.support.scope.Scope;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;

public interface Oauth2AccessTokenManager {

    @NotNull
    Mono<Oauth2AccessToken> generateAccessToken(@NotNull String userId,
                                                @NotNull List<Scope> scopes);

    @NotNull
    Mono<Oauth2AccessToken> verifyToken(@NotNull String tokenValue);
}
