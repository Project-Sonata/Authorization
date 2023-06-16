package com.odeyalo.sonata.authorization.repository

import com.odeyalo.sonata.authorization.entity.AccessToken

/**
 * Base interface for all repositories that work with [AccessToken]
 */
interface ReactiveAccessTokenRepository<T : AccessToken> : PersistentAccessTokenOperations<T> {

    /**
     * Returns the repository type for the implementation
     */
    fun getRepositoryType(): RepositoryType

}