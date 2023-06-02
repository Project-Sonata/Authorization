package com.odeyalo.sonata.authorization.repository;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Base interface for all repositories that work with {@link AccessToken}
 */
public interface ReactiveAccessTokenRepository<T extends AccessToken, ID> {

    Mono<T> findAccessTokenByTokenValue(String tokenValue);

    Mono<T> save(T token);

    Flux<T> findAllByUserId(String userId);

    Mono<Void> deleteAllByUserId(String userId);

    Mono<Void> deleteById(ID id);

    Flux<T> saveAll(Iterable<T> tokens);

}