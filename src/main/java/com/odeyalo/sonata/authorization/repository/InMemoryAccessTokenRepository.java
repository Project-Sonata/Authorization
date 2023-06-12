package com.odeyalo.sonata.authorization.repository;

import com.odeyalo.sonata.authorization.entity.InMemoryAccessToken;
import com.odeyalo.sonata.authorization.support.util.MultiValueConcurrentMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ReactiveAccessTokenRepository that stores tokens in memory.
 * Useful for development and tests
 */
@Component
public class InMemoryAccessTokenRepository implements ReactiveAccessTokenRepository<InMemoryAccessToken> {
    private final ConcurrentMap<Long, InMemoryAccessToken> cachedById;
    // Used to store the token by token value. key - token value, value - token id
    private final ConcurrentMap<String, Long> cachedByTokenValue;
    // Used to store the tokens by user id. key - user id, value - token id
    private final MultiValueConcurrentMap<String, Long> cachedByUserId;
    private final AtomicLong idHolder = new AtomicLong();


    public InMemoryAccessTokenRepository() {
        this.cachedById = new ConcurrentHashMap<>();
        this.cachedByTokenValue = new ConcurrentHashMap<>();
        this.cachedByUserId = new MultiValueConcurrentMap<>();
    }

    @NotNull
    @Override
    public Mono<InMemoryAccessToken> findById(Long id) {
        return Mono.just(cachedById.get(id));
    }

    @NotNull
    @Override
    public Flux<InMemoryAccessToken> findAll() {
        return Flux.fromStream(cachedById.values().stream());
    }

    @NotNull
    @Override
    public Mono<InMemoryAccessToken> save(InMemoryAccessToken token) {
        return Mono.just(token)
                .map(t -> {
                    InMemoryAccessToken newToken = InMemoryAccessToken.copyFrom(token);
                    newToken.setId(idHolder.incrementAndGet());
                    newToken.setBusinessKey(UUID.randomUUID().toString());
                    return newToken;
                })
                .doOnNext(t -> {
                    cachedById.put(t.getId(), t);
                    cachedByTokenValue.put(t.getTokenValue(), t.getId());
                    cachedByUserId.add(t.getUserId(), t.getId());
                })
                .checkpoint(String.format("InMemoryAccessTokenRepository#save(%s)!", token));
    }

    @NotNull
    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.just(id)
                .doOnNext(cachedById::remove)
                .then();
    }

    @NotNull
    @Override
    public Mono<InMemoryAccessToken> findAccessTokenByTokenValue(@NotNull String tokenValue) {
        return Mono.justOrEmpty(cachedByTokenValue.get(tokenValue))
                .map(cachedById::get)
                .log("findAccessTokenByTokenValue");
    }

    @NotNull
    @Override
    public Flux<InMemoryAccessToken> findAllByUserId(@NotNull String userId) {
        return Mono.justOrEmpty(cachedByUserId.get(userId))
                .flatMapMany(Flux::fromIterable)
                .map(cachedById::get);
    }

    @NotNull
    @Override
    public Mono<Void> deleteAllByUserId(@NotNull String userId) {
        return Mono.just(userId)
                .flatMap(internalUserId -> Mono.justOrEmpty(cachedByUserId.get(internalUserId)))
                .flatMapMany(Flux::fromIterable)
                .concatMap(id -> Mono.just(cachedById.get(id)))
                .doOnNext(token -> {
                    cachedByTokenValue.remove(token.getTokenValue());
                    cachedByUserId.remove(token.getUserId());
                    cachedById.remove(token.getId());
                })
                .then();
    }

    @NotNull
    @Override
    public Flux<InMemoryAccessToken> saveAll(@NotNull Iterable<? extends InMemoryAccessToken> tokens) {
        return Flux.fromIterable(tokens)
                .flatMap(this::save);
    }

    @NotNull
    @Override
    public RepositoryType getRepositoryType() {
        return RepositoryType.IN_MEMORY;
    }
}
