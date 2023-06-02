package com.odeyalo.sonata.authorization.service.token.access.generator;

import com.odeyalo.sonata.authorization.service.token.access.GeneratedAccessToken;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Service
public class RandomOpaqueAccessTokenGenerator implements OpaqueAccessTokenGenerator {
    private final int tokenLength;
    private final long lifetimeMs;

    @Autowired
    public RandomOpaqueAccessTokenGenerator(@Value("${sonata.security.token.opaque.lifetime.ms}") long lifetimeMs) {
        Assert.state(lifetimeMs > 0, "Token lifetime cannot be less or equal to 0!");
        this.tokenLength = 256;
        this.lifetimeMs = lifetimeMs;
    }

    public RandomOpaqueAccessTokenGenerator(@Value("${sonata.security.token.opaque.length}") int tokenLength,
                                            @Value("${sonata.security.token.opaque.lifetime.ms}") long lifetimeMs) {
        Assert.state(tokenLength > 0, "Token length be less or equal to 0!");
        Assert.state(lifetimeMs > 0, "Token lifetime be less or equal to 0!");
        this.tokenLength = tokenLength;
        this.lifetimeMs = lifetimeMs;
    }

    @Override
    public Mono<GeneratedAccessToken> generateAccessToken(String userId, Map<String, Object> body) {
        String value = RandomStringUtils.random(tokenLength);
        long creationTimeMs = System.currentTimeMillis();

        GeneratedAccessToken token = GeneratedAccessToken.builder()
                .tokenValue(value)
                .creationTimeMs(creationTimeMs)
                .expiresInMs(creationTimeMs + lifetimeMs)
                .userId(userId)
                .claims(body)
                .build();

        return Mono.just(token);
    }
}
