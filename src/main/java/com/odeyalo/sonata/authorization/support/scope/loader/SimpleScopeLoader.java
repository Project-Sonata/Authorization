package com.odeyalo.sonata.authorization.support.scope.loader;

import com.odeyalo.sonata.authorization.exception.ScopeLoadingFailedException;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * ScopeLoader impl that returns only predefined scopes
 */
public class SimpleScopeLoader implements ScopeLoader {
    private final List<Scope> scopes;

    public SimpleScopeLoader(List<Scope> scopes) {
        this.scopes = scopes;
    }

    @Override
    public Flux<Scope> loadScopes() throws ScopeLoadingFailedException {
        return Flux.fromIterable(scopes);
    }
}
