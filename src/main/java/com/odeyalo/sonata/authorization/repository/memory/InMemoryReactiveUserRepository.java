package com.odeyalo.sonata.authorization.repository.memory;

import com.odeyalo.sonata.authorization.entity.BaseEntityImpl;
import com.odeyalo.sonata.authorization.entity.User;
import com.odeyalo.sonata.authorization.repository.ReactiveUserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ReactiveUserRepository impl that stores users in Map
 */
@Repository
public class InMemoryReactiveUserRepository implements ReactiveUserRepository {
    private final ConcurrentMap<Long, User> storeById;
    private final ConcurrentMap<String, Long> storeByUsername;
    private final AtomicLong idHolder = new AtomicLong();

    public InMemoryReactiveUserRepository() {
        this.storeById = new ConcurrentHashMap<>(256);
        this.storeByUsername = new ConcurrentHashMap<>(256);
    }

    public InMemoryReactiveUserRepository(List<User> store) {
        this.storeById = new ConcurrentHashMap<>(store.stream().collect(Collectors.toMap(BaseEntityImpl::getId, Function.identity())));
        this.storeByUsername = new ConcurrentHashMap<>(256);

        for (User user : store) {
            createInMemoryIndexes(user);
        }
        idHolder.set(store.size());
    }

    public InMemoryReactiveUserRepository(ConcurrentMap<Long, User> store) {
        this.storeById = store;
        this.storeByUsername = new ConcurrentHashMap<>(256);

        for (User user : store.values()) {
            createInMemoryIndexes(user);
        }
        idHolder.set(store.size());
    }

    @Override
    @NotNull
    public Mono<User> findUserByUsername(@NotNull String username) {
        return Mono.justOrEmpty(storeByUsername.get(username))
                .flatMap(id -> Mono.justOrEmpty(storeById.get(id)));
    }

    @Override
    @NotNull
    public Mono<Void> deleteUserByUsername(@NotNull String username) {
        return Mono.justOrEmpty(storeByUsername.get(username))
                .doOnNext(storeById::remove)
                .then();
    }

    @Override
    @NotNull
    public Mono<User> findById(@NotNull Long id) {
        return Mono.justOrEmpty(storeById.remove(id));
    }

    @Override
    @NotNull
    public Flux<User> findAll() {
        return Flux.fromIterable(storeById.values());
    }

    @Override
    @NotNull
    public Mono<Void> deleteById(@NotNull Long id) {
        return Mono.justOrEmpty(storeById.remove(id))
                .then();
    }

    @Override
    @NotNull
    public Mono<User> save(@NotNull User entity) {
        return Mono.just(entity)
                .map(user -> {
                    if (user.getId() == null) {
                        long id = idHolder.incrementAndGet();
                        user.setId(id);
                    }
                    return user;
                })
                .doOnNext(user -> {
                    storeById.put(user.getId(), user);
                    createInMemoryIndexes(user);
                });
    }

    private void createInMemoryIndexes(User user) {
        storeByUsername.put(user.getUsername(), user.getId());
    }
}
