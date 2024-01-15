package com.odeyalo.sonata.authorization.service.token.oauth2

import com.odeyalo.sonata.authorization.entity.Role
import com.odeyalo.sonata.authorization.repository.memory.InMemoryOauth2AccessTokenRepository
import com.odeyalo.sonata.authorization.support.scope.CommonScope
import com.odeyalo.sonata.authorization.support.scope.Scope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

internal class PersistentOauth2AccessTokenManagerTest {
    val testable = PersistentOauth2AccessTokenManager(
        InMemoryOauth2AccessTokenRepository()
    )

    val DEFAULT_TOKEN_LIFETIME = Duration.ofMinutes(15)

    @Test
    fun tokenShouldBeGenerated() {
        val userId = "miku"
        val scopes = listOf<Scope>(
            CommonScope("write", "write something", setOf(Role.USER.name), Scope.Type.PUBLIC)
        )

        testable.generateAccessToken(userId, scopes)
            .`as`(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    fun differentTokensShouldBeGenerated() {
        val userId = "miku"
        val scopes = listOf<Scope>(
            CommonScope("write", "write something", setOf(Role.USER.name), Scope.Type.PUBLIC)
        )

        val tokens = Flux.range(0, 3)
            .flatMap { testable.generateAccessToken(userId, scopes) }
            .collectList()
            .map { it.toSet() }
            .block()

        assertThat(tokens).hasSize(3);
    }

    @Test
    fun shouldReturnIssueTime() {
        val userId = "miku"
        val scopes = listOf<Scope>(
            CommonScope("write", "write something", setOf(Role.USER.name), Scope.Type.PUBLIC)
        )

        testable.generateAccessToken(userId, scopes)
            .`as`(StepVerifier::create)
            .expectNextMatches { it.issuedAt > 0 }
            .verifyComplete()
    }

    @Test
    fun shouldReturnExpirationTime() {
        val userId = "miku"
        val scopes = listOf<Scope>(
            CommonScope("write", "write something", setOf(Role.USER.name), Scope.Type.PUBLIC)
        )

        testable.generateAccessToken(userId, scopes)
            .`as`(StepVerifier::create)
            .expectNextMatches { it.expireTime > it.issuedAt }
            .verifyComplete()
    }

    @Test
    fun tokenDurationShouldBeValid() {
        val userId = "miku"
        val scopes = listOf<Scope>(
            CommonScope("write", "write something", setOf(Role.USER.name), Scope.Type.PUBLIC)
        )

        testable.generateAccessToken(userId, scopes)
            .`as`(StepVerifier::create)
            .expectNextMatches { Duration.ofSeconds(it.expireTime - it.issuedAt) == DEFAULT_TOKEN_LIFETIME }
            .verifyComplete()
    }

    @Test
    fun shouldContainScopes() {
        val userId = "miku"
        val scopes = listOf<Scope>(
            CommonScope("write", "write something", setOf(Role.USER.name), Scope.Type.PUBLIC)
        )

        val result = testable.generateAccessToken(userId, scopes).block()

        assertThat(result).isNotNull

        assertThat(result.scopes).containsAll(scopes)
    }

    @Test
    fun shouldContainUserId() {

        val userId = "miku"
        val scopes = listOf<Scope>(
            CommonScope("write", "write something", setOf(Role.USER.name), Scope.Type.PUBLIC)
        )

        val result = testable.generateAccessToken(userId, scopes).block()

        assertThat(result).isNotNull

        assertThat(result.userId).isEqualTo(userId)
    }

    @Test
    fun existingTokenShouldBeVerified() {
        val userId = "miku"
        val scopes = listOf<Scope>(
            CommonScope("write", "write something", setOf(Role.USER.name), Scope.Type.PUBLIC)
        )

        val generatedToken = testable.generateAccessToken(userId, scopes).block()

        assertThat(generatedToken).isNotNull

        val tokenValue = generatedToken.tokenValue

        testable.verifyToken(tokenValue)
            .`as`(StepVerifier::create)
            .expectNext(generatedToken)
            .verifyComplete()
    }
}