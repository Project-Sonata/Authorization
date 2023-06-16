package com.odeyalo.sonata.authorization.support.scope.loader;

import com.odeyalo.sonata.authorization.exception.ScopeLoadingFailedException;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import reactor.core.publisher.Flux;

/**
 * Loader to load the scopes from different sources
 */
public interface ScopeLoader {
    /**
     * Returns all scopes for Sonata Project
     * @return - scopes wrapped in flux
     */
    Flux<Scope> loadScopes() throws ScopeLoadingFailedException;
}
