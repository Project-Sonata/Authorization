package com.odeyalo.sonata.authorization.service.token.oauth2;

import com.odeyalo.sonata.authorization.entity.Oauth2AccessTokenEntity;
import com.odeyalo.sonata.authorization.repository.Oauth2AccessTokenRepository;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import com.odeyalo.sonata.authorization.support.scope.ScopeContainer;
import org.apache.commons.lang.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class PersistentOauth2AccessTokenManager implements Oauth2AccessTokenManager {
    private final Oauth2AccessTokenRepository oauth2AccessTokenRepository;

    private static final Duration TOKEN_LIFETIME = Duration.ofMinutes(15);
    private static final int TOKEN_GENERATION_LENGTH = 256;

    @Autowired
    public PersistentOauth2AccessTokenManager(Oauth2AccessTokenRepository oauth2AccessTokenRepository) {
        this.oauth2AccessTokenRepository = oauth2AccessTokenRepository;
    }

    @Override
    @NotNull
    public Mono<Oauth2AccessToken> generateAccessToken(@NotNull String userId,
                                                       @NotNull List<Scope> scopes) {

        long issuedAt = Instant.now().getEpochSecond();

        Oauth2AccessToken token = Oauth2AccessToken.builder()
                .tokenValue(RandomStringUtils.randomAlphanumeric(TOKEN_GENERATION_LENGTH))
                .issuedAt(issuedAt)
                .expireTime(issuedAt + TOKEN_LIFETIME.getSeconds())
                .scopes(ScopeContainer.fromCollection(scopes))
                .userId(userId)
                .build();

        Oauth2AccessTokenEntity entity = toOauth2AccessTokenEntity(token);

        return oauth2AccessTokenRepository.save(entity)
                .map(e -> token);
    }

    @Override
    @NotNull
    public Mono<Oauth2AccessToken> verifyToken(@NotNull String tokenValue) {
        return oauth2AccessTokenRepository.findByTokenValue(tokenValue)
                .filter(Oauth2AccessTokenEntity::isValid)
                .map(PersistentOauth2AccessTokenManager::toOauth2AccessToken);
    }

    private static Oauth2AccessTokenEntity toOauth2AccessTokenEntity(Oauth2AccessToken token) {
        return Oauth2AccessTokenEntity.builder()
                .tokenValue(token.getTokenValue())
                .issuedAtSeconds(token.getIssuedAt())
                .expiresInSeconds(token.getExpireTime())
                .scopes(token.getScopes())
                .userId(token.getUserId())
                .build();
    }

    private static Oauth2AccessToken toOauth2AccessToken(Oauth2AccessTokenEntity entity) {
        return Oauth2AccessToken.builder()
                .tokenValue(entity.getTokenValue())
                .issuedAt(entity.getIssuedAtSeconds())
                .expireTime(entity.getExpiresInSeconds())
                .scopes(entity.getScopes())
                .userId(entity.getUserId())
                .build();
    }
}
