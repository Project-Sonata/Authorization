package com.odeyalo.sonata.authorization.support.scope;

import reactor.core.publisher.Flux;

/**
 * Interface to get scopes provided by Sonata Project
 */
public interface SonataScopeProvider {
    /**
     * Returns all scopes
     * @return - all scopes or empty Flux otherwise
     */
    Flux<Scope> getScopes();

    /**
     * Return scopes only for specific role
     * @param role - role name
     * @return - scopes only for specific role
     */
    Flux<Scope> getScopesByRole(String role);
}
