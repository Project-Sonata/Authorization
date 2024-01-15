package com.odeyalo.sonata.authorization.repository;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Represents the basic operations that can be done using persistent storage
 */
public interface BasicPersistentOperations<T, ID> {

    @NotNull
    Mono<T> findById(@NotNull ID id);

    @NotNull
    Flux<T> findAll();

    @NotNull
    Mono<T> save(@NotNull T entity);

    @NotNull
    Mono<Void> deleteById(@NotNull ID id);

}
