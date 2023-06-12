package com.odeyalo.sonata.authorization.service.token.access;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.entity.InMemoryUser;
import com.odeyalo.sonata.authorization.entity.RedisAccessToken;
import com.odeyalo.sonata.authorization.entity.User;
import com.odeyalo.sonata.authorization.exception.ScopeLoadingFailedException;
import com.odeyalo.sonata.authorization.repository.ReactiveAccessTokenRepository;
import com.odeyalo.sonata.authorization.repository.RedisAccessTokenRepository;
import com.odeyalo.sonata.authorization.repository.RepositoryType;
import com.odeyalo.sonata.authorization.repository.storage.DelegatingAccessTokenStorage;
import com.odeyalo.sonata.authorization.service.token.access.generator.RandomOpaqueAccessTokenGenerator;
import com.odeyalo.sonata.authorization.support.scope.CommonScope;
import com.odeyalo.sonata.authorization.support.scope.DefaultCacheableSonataScopeProvider;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import com.odeyalo.sonata.authorization.support.scope.loader.ScopeLoader;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ScopeBasedDelegatingPersistentAccessTokenManagerTest {

    @Test
    void createToken() {
        ScopeBasedDelegatingPersistentAccessTokenManager manager =
                new ScopeBasedDelegatingPersistentAccessTokenManager(new RandomOpaqueAccessTokenGenerator(50000L),
                        new DefaultCacheableSonataScopeProvider(() -> Flux.just(new CommonScope("name1", "desc", Set.of("user", "admin")))),
                        new DelegatingAccessTokenStorage(null));

        Mono<AccessToken> token = manager.createToken(new InMemoryUser());

        System.out.println(token.block().getTokenValue().length());

    }

    @Test
    void verifyToken() {
    }
}