package com.odeyalo.sonata.authorization.service.token.access.generator;

import com.odeyalo.sonata.authorization.service.token.access.GeneratedAccessToken;
import com.odeyalo.sonata.authorization.testing.asserts.GeneratedAccessTokenAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.in;

class RandomOpaqueAccessTokenGeneratorTest {
    private final int LIFETIME_15_MINUTES = 900000;

    @Test
    @DisplayName("Generate access token with 15 minutes token lifetime and expect full valid token as result")
    void generateAccessTokenWithDefaultConstructor_andExpectValidToken() {
        // given
        String userId = "1";
        Map<String, Object> claims = Map.of("scope", "playlist:read playlist:write profile:read");
        RandomOpaqueAccessTokenGenerator generator = new RandomOpaqueAccessTokenGenerator(LIFETIME_15_MINUTES);
        // when
        Mono<GeneratedAccessToken> monoToken = generator.generateAccessToken(userId, claims);
        //then
        GeneratedAccessToken token = monoToken.block();

        GeneratedAccessTokenAssert.create(token)
                .notNull()
                .hasTokenSize(256)
                .userId(userId)
                .tokenBody(claims)
                .hasLifetimeMs(LIFETIME_15_MINUTES);
    }

    @Test
    @DisplayName("Generate access token with 15 minutes token lifetime and custom length and expect full valid token as result")
    void generateAccessTokenWithCustomTokenLength_andExpectTokenToBeValid() {
        // given
        String userId = "1";
        Map<String, Object> claims = Map.of("scope", "playlist:read playlist:write profile:read");
        int tokenLength = 20;
        RandomOpaqueAccessTokenGenerator generator = new RandomOpaqueAccessTokenGenerator(tokenLength, LIFETIME_15_MINUTES);
        // when
        Mono<GeneratedAccessToken> accessTokenMono = generator.generateAccessToken(userId, claims);
        // then
        GeneratedAccessToken token = accessTokenMono.block();

        GeneratedAccessTokenAssert.create(token)
                .notNull()
                .hasTokenSize(tokenLength)
                .userId(userId)
                .tokenBody(claims)
                .hasLifetimeMs(LIFETIME_15_MINUTES);
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -5, -2, 0})
    @DisplayName("Pass invalid values to constructor with lifetime field only and expect IllegalStateException as result")
    public void passInvalidValuesToSingleFieldConstructor_andExpectException(int invalidLifetimeMs) {
        assertThatThrownBy(() -> new RandomOpaqueAccessTokenGenerator(invalidLifetimeMs))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -5, -2, 0})
    @DisplayName("Pass invalid values to constructor with lifetime and token size with invalid lifetime and expect IllegalStateException as result")
    public void passInvalidValuesToTwoFieldsConstructorWithInvalidLifetime_andExpectException(int invalidLifetimeMs) {
        int validTokenLength = 20;
        assertThatThrownBy(() -> new RandomOpaqueAccessTokenGenerator(validTokenLength, invalidLifetimeMs))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -5, -2, 0})
    @DisplayName("Pass invalid values to constructor with lifetime and token size with invalid length and expect IllegalStateException as result")
    public void passInvalidValuesToTwoFieldsConstructorWithInvalidTokenLength_andExpectException(int invalidLength) {
        assertThatThrownBy(() -> new RandomOpaqueAccessTokenGenerator(invalidLength, LIFETIME_15_MINUTES))
                .isInstanceOf(IllegalStateException.class);
    }

}