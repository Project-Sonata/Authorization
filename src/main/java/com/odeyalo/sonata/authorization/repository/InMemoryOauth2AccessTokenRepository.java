package com.odeyalo.sonata.authorization.repository;

import com.odeyalo.sonata.authorization.entity.Oauth2AccessTokenEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryOauth2AccessTokenRepository implements Oauth2AccessTokenRepository {
    private final Map<Long, Oauth2AccessTokenEntity> store = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    @NotNull
    @Override
    public Mono<Oauth2AccessTokenEntity> findById(@NotNull Long id) {
        return Mono.justOrEmpty(store.get(id));
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
    public Mono<Void> deleteById(@NotNull Long id) {
        return Mono.just(store.remove(id))
                .then();
    }

    @Override
    @NotNull
    public Mono<Oauth2AccessTokenEntity> findByTokenValue(@NotNull String tokenValue) {
        return Flux.fromIterable(store.values())
                .filter(entity -> Objects.equals(entity.getTokenValue(), tokenValue))
                .next();
    }

    @Override
    @NotNull
    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(
                store::clear
        );
    }
}
