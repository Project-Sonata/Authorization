package com.odeyalo.sonata.authorization.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Represent the basic operations that can be done using persistent storage
 */
interface BasicPersistentOperations<T, ID> {

    fun findById(id: ID): Mono<T>;

    fun findAll(): Flux<T>

    fun save(entity: T): Mono<T>

    fun deleteById(id: ID): Mono<Void>

}