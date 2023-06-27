package com.odeyalo.sonata.authorization.service;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.entity.InMemoryUser;
import com.odeyalo.sonata.authorization.service.authentication.SonataAuthenticationProvider;
import com.odeyalo.sonata.authorization.service.token.access.AccessTokenManager;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;

/**
 * Default impl of TokenBasedAuthenticatorFacade
 */
@Component
public class DefaultTokenBasedAuthenticatorFacade implements TokenBasedAuthenticatorFacade {
    private final SonataAuthenticationProvider authenticationProvider;
    private final AccessTokenManager manager;

    public DefaultTokenBasedAuthenticatorFacade(
            SonataAuthenticationProvider authenticationProvider,
            AccessTokenManager manager) {
        this.authenticationProvider = authenticationProvider;
        this.manager = manager;
    }

    @Override
    public Mono<AccessToken> authenticateAndCreateToken(String username, String password) {
        return authenticationProvider.obtainAuthentication(username, password)
                .flatMap(result -> manager.createToken(new InMemoryUser(Long.valueOf(result.getUserInfo().getId()), "key",
                        System.currentTimeMillis(), result.getUserInfo().getEmail(), new HashSet<>(result.getAuthorities()), "user")))
                .onErrorMap(ex -> true, ex -> ex);
    }
}
