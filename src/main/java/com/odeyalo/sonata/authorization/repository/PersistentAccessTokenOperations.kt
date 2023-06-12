package com.odeyalo.sonata.authorization.repository

import com.odeyalo.sonata.authorization.entity.AccessToken
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Basic operations to work with [AccessToken]
 */
interface PersistentAccessTokenOperations<T : AccessToken> : BasicPersistentOperations<T, Long> {

    fun findAccessTokenByTokenValue(tokenValue: String): Mono<T>

    fun findAllByUserId(userId: String): Flux<T>

    fun deleteAllByUserId(userId: String): Mono<Void>

    fun saveAll(tokens: Iterable<T>): Flux<T>
}