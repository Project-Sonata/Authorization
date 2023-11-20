package com.odeyalo.sonata.authorization.controller;

import com.odeyalo.sonata.authorization.dto.GeneratedInternalAccessTokenResponseDto;
import com.odeyalo.sonata.authorization.exception.ScopeLoadingFailedException;
import com.odeyalo.sonata.authorization.service.token.oauth2.Oauth2AccessTokenManager;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import com.odeyalo.sonata.authorization.support.scope.loader.ScopeLoader;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/internal")
public class InternalEndpointsController {
    private final Oauth2AccessTokenManager accessTokenManager;
    private final ScopeLoader scopeLoader;

    public InternalEndpointsController(Oauth2AccessTokenManager accessTokenManager, ScopeLoader scopeLoader) {
        this.accessTokenManager = accessTokenManager;
        this.scopeLoader = scopeLoader;
    }

    @PostMapping("/oauth/token/access")
    public Mono<ResponseEntity<GeneratedInternalAccessTokenResponseDto>> generateAccessToken(@RequestParam("user_id") String userId,
                                                                                             @RequestParam("scope") String scopes) throws ScopeLoadingFailedException {
        String[] scopesArray = scopes.split(" ");
        Mono<List<Scope>> scopesList = scopeLoader.loadScopes()
                .filter(scope -> ArrayUtils.contains(scopesArray, scope.getName()))
                .collectList();

        return scopesList.flatMap(list -> accessTokenManager.generateAccessToken(userId, list))
                .map(token -> GeneratedInternalAccessTokenResponseDto.of(token.getTokenValue()))
                .map(ResponseEntity::ok);
    }
}
