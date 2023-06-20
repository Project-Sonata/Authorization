package com.odeyalo.sonata.authorization.repository.user

import com.odeyalo.sonata.authorization.entity.InMemoryUser
import com.odeyalo.sonata.authorization.entity.User
import com.odeyalo.sonata.authorization.repository.RepositoryType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * ReactiveUserRepository impl that stores users in Map
 */
class InMemoryReactiveUserRepository : ReactiveUserRepository<InMemoryUser> {

    override fun getRepositoryType(): RepositoryType {
        return RepositoryType.IN_MEMORY
    }

    override fun findUserByUsername(username: String): Mono<User> {
        TODO("Not yet implemented")
    }

    override fun deleteUserByUsername(username: String): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Mono<InMemoryUser> {
        TODO("Not yet implemented")
    }

    override fun findAll(): Flux<InMemoryUser> {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun save(token: InMemoryUser): Mono<InMemoryUser> {
        TODO("Not yet implemented")
    }

}