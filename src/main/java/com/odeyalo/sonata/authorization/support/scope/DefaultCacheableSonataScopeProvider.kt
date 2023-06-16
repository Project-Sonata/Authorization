package com.odeyalo.sonata.authorization.support.scope

import com.odeyalo.sonata.authorization.support.scope.loader.ScopeLoader
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Flux

/**
 * Load the scopes and cache it.
 * Provide the method to refresh the scopes(load it again).
 * It delegates the job to [ScopeLoader] to load the scopes
 */
@Component
class DefaultCacheableSonataScopeProvider : CacheableSonataScopeProvider {
    private val scopeLoader: ScopeLoader
    private val cacheByRole: MultiValueMap<String, Scope>
    private val allScopes: MutableList<Scope>

    constructor(scopeLoader: ScopeLoader) {
        this.scopeLoader = scopeLoader
        this.cacheByRole = LinkedMultiValueMap()
        this.allScopes = doLoadScopes().toMutableList()
    }


    override fun getScopes(): Flux<Scope> {
        return Flux.fromIterable(allScopes)
    }

    override fun getScopesByRole(role: String): Flux<Scope> {
        return cacheByRole[role]?.let { scopes -> Flux.fromIterable(scopes) }
            ?: Flux.fromIterable(allScopes)
                .filter { scope -> scope.supportsRole(role) }
                .doOnNext { scope -> cacheByRole.add(role, scope) }
    }

    override fun refresh() {
        allScopes.clear()
        cacheByRole.clear()

        allScopes.addAll(doLoadScopes())
    }

    private fun doLoadScopes(): List<Scope> {
         return scopeLoader.loadScopes().toIterable().toList()
    }
}