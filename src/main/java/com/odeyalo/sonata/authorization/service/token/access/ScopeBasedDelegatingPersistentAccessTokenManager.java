package com.odeyalo.sonata.authorization.service.token.access;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.entity.User;
import com.odeyalo.sonata.authorization.repository.storage.AccessTokenStorage;
import com.odeyalo.sonata.authorization.service.authentication.Subject;
import com.odeyalo.sonata.authorization.service.token.access.generator.AccessTokenGenerator;
import com.odeyalo.sonata.authorization.support.scope.SonataScopeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Implementation of {@link AccessTokenManager} that uses {@link AccessTokenStorage}
 * to save the issued tokens by {@link AccessTokenGenerator}
 * <p>
 * Note: The implementation issued tokens with ALL scopes for {@link User#getRole()}.
 * <p>
 * For example, if user has role USER, then ALL scopes for USER ROLE will be generated, such as playlist:read, profile:read, etc.
 * It also important that this implementation does not restrict any scopes in token even if ROLE is ADMIN or ARTIST, etc.
 * That means, if user has role ADMIN, then ALL scopes for ADMIN ROLE will be generated, such as user:block(add ability to block the user)
 */
@Component
public class ScopeBasedDelegatingPersistentAccessTokenManager implements AccessTokenManager {
    private final AccessTokenGenerator accessTokenGenerator;
    private final SonataScopeProvider scopeProvider;
    private final AccessTokenStorage storage;

    public static final String SCOPES_CLAIM_NAME = "scopes";

    @Autowired
    public ScopeBasedDelegatingPersistentAccessTokenManager(AccessTokenGenerator accessTokenGenerator, SonataScopeProvider scopeProvider, AccessTokenStorage storage) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.scopeProvider = scopeProvider;
        this.storage = storage;
    }

    @Override
    public Mono<AccessToken> createToken(Subject subject) {
        return scopeProvider.getScopesByRole(subject.getRole()).collectList()
                .flatMap(scopes -> {
                    Map<String, Object> claims = Map.of(SCOPES_CLAIM_NAME, scopes);
                    return accessTokenGenerator.generateAccessToken(String.valueOf(subject.getId()), claims);
                })
                .map(GeneratedAccessTokenAdapter::new)
            .flatMap(storage::save);
    }

    @Override
    public Mono<AccessToken> verifyToken(String tokenValue) {
        return storage.findAccessTokenByTokenValue(tokenValue)
                .filter(this::isTokenValid);
    }

    private boolean isTokenValid(AccessToken token) {
        return System.currentTimeMillis() <= token.getExpirationTime();
    }
}
