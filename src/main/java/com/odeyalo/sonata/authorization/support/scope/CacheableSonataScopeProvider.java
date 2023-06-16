package com.odeyalo.sonata.authorization.support.scope;

/**
 * SonataScopeProvider that has ability to cache the loaded scopes and refresh them
 */
public interface CacheableSonataScopeProvider extends SonataScopeProvider {
    /**
     * Refresh the cached scopes and load the scopes again
     */
    void refresh();

}