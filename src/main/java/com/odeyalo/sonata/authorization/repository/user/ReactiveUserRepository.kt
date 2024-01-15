package com.odeyalo.sonata.authorization.repository.user

import com.odeyalo.sonata.authorization.entity.User
import com.odeyalo.sonata.authorization.repository.BasicPersistentOperations
import reactor.core.publisher.Mono

/**
 * Repository to work with User entity
 */
interface ReactiveUserRepository : BasicPersistentOperations<User, Long> {

    fun findUserByUsername(username: String): Mono<User>

    fun deleteUserByUsername(username: String): Mono<Void>

}