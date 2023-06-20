package com.odeyalo.sonata.authorization.repository.user

import com.odeyalo.sonata.authorization.entity.User
import com.odeyalo.sonata.authorization.repository.BasicPersistentOperations
import reactor.core.publisher.Mono

/**
 * Operations that can be used to work with user
 */
interface PersistentUserOperations<T : User> : BasicPersistentOperations<T, Long> {

    fun findUserByUsername(username: String): Mono<User>

    fun deleteUserByUsername(username: String): Mono<Void>
}

