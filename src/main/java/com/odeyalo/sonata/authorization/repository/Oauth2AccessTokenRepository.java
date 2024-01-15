package com.odeyalo.sonata.authorization.repository;

import com.odeyalo.sonata.authorization.entity.Oauth2AccessTokenEntity;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface Oauth2AccessTokenRepository extends BasicPersistentOperations<Oauth2AccessTokenEntity, Long>{

    @NotNull
    Mono<Oauth2AccessTokenEntity> findByTokenValue(@NotNull String tokenValue);

    @NotNull
    Mono<Void> deleteAll();

}
