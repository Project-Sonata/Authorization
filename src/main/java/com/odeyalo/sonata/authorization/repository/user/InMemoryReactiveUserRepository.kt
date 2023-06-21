package com.odeyalo.sonata.authorization.repository.user

import com.odeyalo.sonata.authorization.entity.InMemoryUser
import com.odeyalo.sonata.authorization.entity.User
import com.odeyalo.sonata.authorization.repository.RepositoryType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicLong
import java.util.function.Function
import java.util.stream.Collectors

/**
 * ReactiveUserRepository impl that stores users in Map
 */
class InMemoryReactiveUserRepository : ReactiveUserRepository<InMemoryUser> {
    private val storeById: ConcurrentMap<Long, InMemoryUser>
    private val storeByUsername: ConcurrentMap<String, Long>
    private val idHolder: AtomicLong = AtomicLong()

    constructor() {
        this.storeById = ConcurrentHashMap(256)
        this.storeByUsername = ConcurrentHashMap(256)
    }

    constructor(store: List<InMemoryUser>) : this(ConcurrentHashMap(store.associateBy { it.id })) {

    }

    constructor(store: ConcurrentMap<Long, InMemoryUser>) {
        this.storeById = store
        this.storeByUsername = ConcurrentHashMap(256)

        store.forEach {
            createInMemoryIndexes(it.value)
        }
        idHolder.set(store.size.toLong())
    }

    override fun findUserByUsername(username: String): Mono<User> {
        return Mono.justOrEmpty(storeByUsername[username])
            .flatMap { id ->
                return@flatMap Mono.justOrEmpty(storeById[id])
            }
    }

    override fun deleteUserByUsername(username: String): Mono<Void> {
        return Mono.justOrEmpty(storeByUsername[username])
            .doOnNext { id -> storeById.remove(id) }
            .then();
    }

    override fun findById(id: Long): Mono<InMemoryUser> {
        return Mono.justOrEmpty(storeById.remove(id))
    }

    override fun findAll(): Flux<InMemoryUser> {
        return Flux.fromIterable(storeById.values)
    }

    override fun deleteById(id: Long): Mono<Void> {
        return Mono.justOrEmpty(storeById.remove(id))
            .then();
    }

    override fun save(entity: InMemoryUser): Mono<InMemoryUser> {
        return Mono.just(entity)
            .map { user ->
                if (user.id == null) {
                    val id = idHolder.incrementAndGet();
                    return@map InMemoryUser(
                        id,
                        user.businessKey,
                        user.creationTime,
                        user.username,
                        user.grantedAuthorities,
                        user.role
                    )
                }
                return@map user
            }.doOnNext { user ->
                storeById[user.id] = user
                createInMemoryIndexes(user)
            }
    }

    override fun getRepositoryType(): RepositoryType {
        return RepositoryType.IN_MEMORY
    }

    private fun createInMemoryIndexes(user: InMemoryUser) {
        storeByUsername[user.username] = user.id
    }
}