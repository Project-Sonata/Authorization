package com.odeyalo.sonata.authorization.controller;

import com.odeyalo.sonata.authorization.dto.TokenIntrospectionRequest;
import com.odeyalo.sonata.authorization.dto.TokenIntrospectionResponse;
import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.service.token.access.AccessTokenManager;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/token")
public class TokenController {
    private final AccessTokenManager accessTokenManager;

    public TokenController(AccessTokenManager accessTokenManager) {
        this.accessTokenManager = accessTokenManager;
    }

    @PostMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TokenIntrospectionResponse> tokenIntrospection(@RequestBody TokenIntrospectionRequest body) {
        return Mono.from(accessTokenManager.verifyToken(body.getToken()))
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .map(optionalToken -> optionalToken.map(t -> {
                    String scopes = String.join(" ", ((List<Scope>) t.getClaims().get("scopes"))
                            .stream()
                            .map(Scope::getName)
                            .collect(Collectors.toSet()));
                    return TokenIntrospectionResponse.valid(t.getUserId(), scopes, t.getCreationTime(), getExpiresIn(t));
                }).orElse(TokenIntrospectionResponse.invalid()));

    }

    private static long getExpiresIn(AccessToken token) {
        return TimeUnit.MILLISECONDS.toSeconds(token.getExpirationTime()) - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }
}
