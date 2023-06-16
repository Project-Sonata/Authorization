package com.odeyalo.sonata.authorization.repository.storage

import com.odeyalo.sonata.authorization.entity.AccessToken
import com.odeyalo.sonata.authorization.entity.InMemoryAccessToken
import com.odeyalo.sonata.authorization.repository.ReactiveAccessTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Delegate the job to the repository
 */
@Component
class DelegatingAccessTokenStorage : AccessTokenStorage {
    private var tokenRepository: ReactiveAccessTokenRepository<AccessToken>

    @Autowired
    constructor(repos: List<ReactiveAccessTokenRepository<out AccessToken>>) {
        if (repos.isEmpty()) {
            throw IllegalArgumentException("Cannot use empty collection to provide ReactiveAccessTokenRepository!")
        }
        this.tokenRepository = repos.first() as ReactiveAccessTokenRepository<AccessToken>
    }

    override fun findAccessTokenByTokenValue(tokenValue: String): Mono<AccessToken> {
        return tokenRepository.findAccessTokenByTokenValue(tokenValue)

    }

    override fun findAllByUserId(userId: String): Flux<AccessToken> {
        return tokenRepository.findAllByUserId(userId)
    }

    override fun deleteAllByUserId(userId: String): Mono<Void> {
        return tokenRepository.deleteAllByUserId(userId)
    }

    override fun saveAll(tokens: Iterable<AccessToken>): Flux<AccessToken> {
        return tokenRepository.saveAll(tokens)
    }

    override fun findById(id: Long): Mono<AccessToken> {
        return tokenRepository.findById(id)
    }

    override fun findAll(): Flux<AccessToken> {
        return tokenRepository.findAll()
    }

    override fun save(token: AccessToken): Mono<AccessToken> {
        return Mono.just(token)
            .map { t -> InMemoryAccessToken.copyFrom(t) }
            .flatMap { t -> tokenRepository.save(t) }
    }

    override fun deleteById(id: Long): Mono<Void> {
        return tokenRepository.deleteById(id)
    }
}