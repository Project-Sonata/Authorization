package com.odeyalo.sonata.authorization.testing.factory;

import com.odeyalo.sonata.authorization.repository.InMemoryAccessTokenRepository;
import com.odeyalo.sonata.authorization.repository.storage.AccessTokenStorage;
import com.odeyalo.sonata.authorization.repository.storage.DelegatingAccessTokenStorage;
import com.odeyalo.sonata.authorization.service.token.access.ScopeBasedDelegatingPersistentAccessTokenManager;
import com.odeyalo.sonata.authorization.service.token.access.generator.AccessTokenGenerator;
import com.odeyalo.sonata.authorization.service.token.access.generator.RandomOpaqueAccessTokenGenerator;
import com.odeyalo.sonata.authorization.support.scope.DefaultCacheableSonataScopeProvider;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import com.odeyalo.sonata.authorization.support.scope.SonataScopeProvider;
import reactor.core.publisher.Flux;

import java.util.List;

public class AccessTokenManagerFactory {

    public static ScopeBasedDelegatingPersistentAccessTokenManager scopeBasedManager(List<Scope> scopes) {
        return new ScopeBasedDelegatingPersistentAccessTokenManagerBuilder()
                .withScopes(scopes)
                .build();
    }
    public static ScopeBasedDelegatingPersistentAccessTokenManagerBuilder scopeBasedManagerBuilder(List<Scope> scopes) {
        return new ScopeBasedDelegatingPersistentAccessTokenManagerBuilder()
                .withScopes(scopes);
    }

    public static class ScopeBasedDelegatingPersistentAccessTokenManagerBuilder {
        private AccessTokenGenerator accessTokenGenerator = new RandomOpaqueAccessTokenGenerator(500000L);
        private SonataScopeProvider scopeProvider = new DefaultCacheableSonataScopeProvider(Flux::empty);
        private AccessTokenStorage storage = new DelegatingAccessTokenStorage(List.of(new InMemoryAccessTokenRepository()));

        public ScopeBasedDelegatingPersistentAccessTokenManagerBuilder overrideTokenGenerator(AccessTokenGenerator accessTokenGenerator) {
            this.accessTokenGenerator = accessTokenGenerator;
            return this;
        }

        public ScopeBasedDelegatingPersistentAccessTokenManagerBuilder withScopes(Scope... scopes) {
            this.scopeProvider = new DefaultCacheableSonataScopeProvider(() -> Flux.just(scopes));
            return this;
        }

        public ScopeBasedDelegatingPersistentAccessTokenManagerBuilder withScopes(List<Scope> scopes) {
            this.scopeProvider = new DefaultCacheableSonataScopeProvider(() -> Flux.fromIterable(scopes));
            return this;
        }

        public ScopeBasedDelegatingPersistentAccessTokenManagerBuilder overrideScopeProvider(SonataScopeProvider scopeProvider) {
            this.scopeProvider = scopeProvider;
            return this;
        }

        public ScopeBasedDelegatingPersistentAccessTokenManagerBuilder overrideTokenStorage(AccessTokenStorage storage) {
            this.storage = storage;
            return this;
        }

        public ScopeBasedDelegatingPersistentAccessTokenManager build() {
            return new ScopeBasedDelegatingPersistentAccessTokenManager(accessTokenGenerator, scopeProvider, storage);
        }
    }

}
