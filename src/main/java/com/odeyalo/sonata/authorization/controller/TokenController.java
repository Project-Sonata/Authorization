package com.odeyalo.sonata.authorization.controller;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.service.token.access.AccessTokenManager;
import com.odeyalo.sonata.authorization.service.token.oauth2.Oauth2AccessToken;
import com.odeyalo.sonata.authorization.service.token.oauth2.Oauth2AccessTokenManager;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionRequest;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/token")
public class TokenController {
    private final AccessTokenManager accessTokenManager;
    private final Oauth2AccessTokenManager oauth2AccessTokenManager;

    public TokenController(AccessTokenManager accessTokenManager, Oauth2AccessTokenManager oauth2AccessTokenManager) {
        this.accessTokenManager = accessTokenManager;
        this.oauth2AccessTokenManager = oauth2AccessTokenManager;
    }

    @PostMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TokenIntrospectionResponse> tokenIntrospection(@RequestBody TokenIntrospectionRequest body) {
        return accessTokenManager.verifyToken(body.getToken())
                .map(t -> {
                    String scopes = String.join(" ", ((List<Scope>) t.getClaims().get("scopes"))
                            .stream()
                            .map(Scope::getName)
                            .collect(Collectors.toSet()));
                    return TokenIntrospectionResponse.valid(t.getUserId(), scopes, t.getCreationTime(), getAccessTokenExpiresIn(t));
                })
                .defaultIfEmpty(TokenIntrospectionResponse.invalid());

    }

    @PostMapping(value = "/oauth2/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TokenIntrospectionResponse> oauth2TokenIntrospection(@RequestBody TokenIntrospectionRequest body) {
        return oauth2AccessTokenManager.verifyToken(body.getToken())
                .map(token -> {
                    String scopes = String.join(" ", token.getScopes().stream().map(Scope::getName).toList());
                    return TokenIntrospectionResponse.valid(token.getUserId(), scopes, token.getIssuedAt(), getOauth2AccessTokenExpiresIn(token));
                })
                .defaultIfEmpty(TokenIntrospectionResponse.invalid());

    }

    private static long getAccessTokenExpiresIn(AccessToken token) {
        return TimeUnit.MILLISECONDS.toSeconds(token.getExpirationTime()) - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }

    private static long getOauth2AccessTokenExpiresIn(Oauth2AccessToken token) {
        return TimeUnit.MILLISECONDS.toSeconds(token.getExpireTime()) - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }
}
