package com.odeyalo.sonata.authorization.repository;

import com.odeyalo.sonata.authorization.entity.Oauth2AccessTokenEntity;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryOauth2AccessTokenRepository implements Oauth2AccessTokenRepository {
    private final Map<Long, Oauth2AccessTokenEntity> store = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @NotNull
    @Override
    public Mono<Oauth2AccessTokenEntity> findById(Long id) {
        return Mono.just(store.get(id));
    }

    @NotNull
    @Override
    public Flux<Oauth2AccessTokenEntity> findAll() {
        return Flux.fromIterable(store.values());
    }

    @NotNull
    @Override
    public Mono<Oauth2AccessTokenEntity> save(Oauth2AccessTokenEntity entity) {
        if (entity.getId() == null) {
            entity.setId(idCounter.incrementAndGet());
        }
        store.put(entity.getId(), entity);
        return Mono.just(entity);
    }

    @NotNull
    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.just(store.remove(id))
                .then();
    }

    @Override
    public Mono<Oauth2AccessTokenEntity> findByTokenValue(String tokenValue) {
        if (tokenValue == null) {
            return Mono.empty();
        }
        return Flux.fromIterable(store.values())
                .filter(entity -> Objects.equals(entity.getTokenValue(), tokenValue))
                .next();
    }

    @Override
    public Mono<Void> deleteAll() {
        store.clear();
        return Mono.empty();
    }
}
