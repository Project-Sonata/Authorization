package com.odeyalo.sonata.authorization.repository;

import com.odeyalo.sonata.authorization.entity.Oauth2AccessTokenEntity;
import reactor.core.publisher.Mono;

public interface Oauth2AccessTokenRepository extends BasicPersistentOperations<Oauth2AccessTokenEntity, Long>{

    Mono<Oauth2AccessTokenEntity> findByTokenValue(String tokenValue);

    Mono<Void> deleteAll();

}
