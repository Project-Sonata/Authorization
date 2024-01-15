package com.odeyalo.sonata.authorization.repository

import com.odeyalo.sonata.authorization.entity.AccessToken
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Base interface for all repositories that work with [AccessToken]
 */
interface ReactiveAccessTokenRepository : BasicPersistentOperations<AccessToken, Long> {

    fun findAccessTokenByTokenValue(tokenValue: String): Mono<AccessToken>

    fun findAllByUserId(userId: String): Flux<AccessToken>

    fun deleteAllByUserId(userId: String): Mono<Void>

    fun saveAll(tokens: Iterable<AccessToken>): Flux<AccessToken>

}