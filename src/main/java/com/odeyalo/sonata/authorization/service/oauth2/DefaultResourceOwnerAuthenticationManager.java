package com.odeyalo.sonata.authorization.service.oauth2;

import com.odeyalo.sonata.authorization.service.authentication.SonataAuthentication;
import com.odeyalo.sonata.authorization.service.authentication.SonataAuthenticationProvider;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticationManager;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.UsernamePasswordAuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * Custom authentication manager that send requests to authentication microservice
 */
@Component
public class DefaultResourceOwnerAuthenticationManager implements ResourceOwnerAuthenticationManager {
    private final SonataAuthenticationProvider sonataAuthenticationProvider;

    public DefaultResourceOwnerAuthenticationManager(SonataAuthenticationProvider sonataAuthenticationProvider) {
        this.sonataAuthenticationProvider = sonataAuthenticationProvider;
    }

    @Override
    public @NotNull Mono<AuthenticatedResourceOwnerAuthentication> attemptAuthentication(@NotNull ServerWebExchange webExchange) {
        return webExchange.getFormData()
                .flatMap(formData -> {
                    String username = formData.getFirst("username");
                    String password = formData.getFirst("password");
                    return sonataAuthenticationProvider.obtainAuthentication(username, password);
                })
                .onErrorMap(ex -> ResourceOwnerAuthenticationException.withMessageAndCause("Failed to authenticate the user", ex))
                .map(DefaultResourceOwnerAuthenticationManager::asResourceOwnerAuthentication);
    }

    private static UsernamePasswordAuthenticatedResourceOwnerAuthentication asResourceOwnerAuthentication(SonataAuthentication sonataAuthentication) {
        ResourceOwner resourceOwner = ResourceOwner.builder()
                .principal(sonataAuthentication.getName())
                .credentials(sonataAuthentication.getCredentials())
                .build();

        return UsernamePasswordAuthenticatedResourceOwnerAuthentication.builder()
                .principal(sonataAuthentication.getName())
                .credentials(sonataAuthentication.getCredentials())
                .resourceOwner(resourceOwner)
                .build();
    }
}
