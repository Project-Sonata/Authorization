package com.odeyalo.sonata.authorization.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.sonata.authorization.entity.RedisAccessToken;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ReactiveAccessTokenRepository impl that uses Redis as token store.
 */
@Repository
public class RedisAccessTokenRepository implements ReactiveAccessTokenRepository<RedisAccessToken> {
    public static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN";
    private static final String ACCESS_TOKEN_ID_SEQUENCE_KEY = "access_token_id_seq";
    public static final String ACCESS_TOKEN_USER_ID_INDEX_PREFIX = "token:user:id:";
    public static final String TOKEN_VALUE_INDEX_PREFIX = "token:value:";

    private final ReactiveRedisOperations<String, Object> template;
    private final ObjectMapper redisObjectMapper;

    public RedisAccessTokenRepository(ReactiveRedisOperations<String, Object> template, ObjectMapper redisObjectMapper) {
        this.template = template;
        this.redisObjectMapper = redisObjectMapper;
    }

    @Override
    public Mono<RedisAccessToken> findAccessTokenByTokenValue(String tokenValue) {
        return template.opsForSet().members(TOKEN_VALUE_INDEX_PREFIX + tokenValue)
                .flatMap(tokenId -> template.opsForHash().get(ACCESS_TOKEN_KEY, tokenId))
                .flatMap(bodyMap -> {
                    RedisAccessToken token = redisObjectMapper.convertValue(bodyMap, RedisAccessToken.class);
                    return Mono.justOrEmpty(token);
                })
                .next();
    }

    @Override
    public Mono<RedisAccessToken> save(RedisAccessToken token) {
        return template.opsForValue().get(ACCESS_TOKEN_ID_SEQUENCE_KEY)
                .defaultIfEmpty(0L)
                // Increment the index to next value
                .map(value -> Long.valueOf((Integer) value) + 1)
                .flatMap(id -> {
                    RedisAccessToken newToken = RedisAccessToken.copyFrom(token).setId(id);
                    return saveToken(newToken);
                })
                .flatMap(this::updateIdSequence)
                .flatMap(this::createIndexes);
    }

    @Override
    public Mono<RedisAccessToken> findById(Long id) {
        return template.opsForHash().get(ACCESS_TOKEN_KEY, id)
                .map(bodyMap -> redisObjectMapper.convertValue(bodyMap, RedisAccessToken.class));
    }

    @Override
    public Flux<RedisAccessToken> findAll() {
        return template.opsForHash().keys(ACCESS_TOKEN_KEY)
                .map(id -> (Long) id)
                .flatMap(this::findById);
    }

    @Override
    public Flux<RedisAccessToken> findAllByUserId(@NotNull String userId) {
        return template.opsForSet()
                .members(ACCESS_TOKEN_USER_ID_INDEX_PREFIX + userId)
                .flatMap(id -> template.opsForHash().get(ACCESS_TOKEN_KEY, id))
                .map(mapBody -> redisObjectMapper.convertValue(mapBody, RedisAccessToken.class));

    }

    @Override
    public Mono<Void> deleteAllByUserId(@NotNull String userId) {
        return findAllByUserId(userId)
                .flatMap(token -> template.opsForHash().remove(ACCESS_TOKEN_ID_SEQUENCE_KEY, token.getId()))
                .then();
    }

    @NotNull
    @Override
    public Flux<RedisAccessToken> saveAll(@NotNull Iterable<? extends RedisAccessToken> tokens) {
        return Flux.fromIterable(tokens)
                .flatMap(this::save);
    }

    @NotNull
    @Override
    public Mono<Void> deleteById(Long id) {
        return template.opsForHash().remove(ACCESS_TOKEN_KEY, id)
                .then();
    }

    @NotNull
    @Override
    public RepositoryType getRepositoryType() {
        return RepositoryType.REDIS;
    }


    private Mono<RedisAccessToken> updateIdSequence(RedisAccessToken t) {
        return template.opsForValue().set(ACCESS_TOKEN_ID_SEQUENCE_KEY, t.getId()).map(b -> t);
    }

    private Mono<RedisAccessToken> saveToken(RedisAccessToken token) {
        return template.opsForHash().put(ACCESS_TOKEN_KEY, token.getId(), token).map(b -> token);
    }

    private Mono<RedisAccessToken> createIndexes(RedisAccessToken token) {
        Mono<Long> userIdIndex = template.opsForSet()
                .add(ACCESS_TOKEN_USER_ID_INDEX_PREFIX + token.getUserId(), token.getId());

        Mono<Long> tokenValueIndex = template.opsForSet()
                .add(TOKEN_VALUE_INDEX_PREFIX + token.getTokenValue(), token.getTokenValue());

        return Mono.zip(userIdIndex, tokenValueIndex)
                .map(t -> token);
    }
}
