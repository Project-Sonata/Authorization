package com.odeyalo.sonata.authorization.support.web.enhancer;

import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import com.odeyalo.sonata.authorization.support.web.DomainNameResolver;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload.AuthenticationStrategy.SESSION;

/**
 *
 */
@Component
public class SessionAuthenticationStrategyLoginEndpointResponseEnhancer implements LoginEndpointResponseEnhancer {
    private final DomainNameResolver domainNameResolver;
    private final Logger logger = LoggerFactory.getLogger(SessionAuthenticationStrategyLoginEndpointResponseEnhancer.class);
    public static final String SSO_COOKIE_NAME = "sntsso";

    public SessionAuthenticationStrategyLoginEndpointResponseEnhancer(DomainNameResolver domainNameResolver) {
        this.domainNameResolver = domainNameResolver;
    }

    @Override
    public Mono<ServerWebExchange> enhance(AuthenticationPayload authenticationPayload, ServerWebExchange webExchange) {
        if (authenticationPayload.authenticationStrategy() != SESSION) {
            logger.info("Authentication strategy does not match. Using: {}", authenticationPayload.authenticationStrategy());
            return Mono.just(webExchange);
        }
        return Mono.fromCallable(() -> {
            logger.info("Using SESSION authentication strategy. Enhance the current request with Set-Cookie header.");
            ResponseCookie cookie = createCookie(authenticationPayload, webExchange);
            webExchange.getResponse().addCookie(cookie);
            return webExchange;
        });
    }

    @NotNull
    private ResponseCookie createCookie(AuthenticationPayload authenticationPayload, ServerWebExchange webExchange) {
        return ResponseCookie.from(SSO_COOKIE_NAME, authenticationPayload.uniqueAuthenticationIdentifier())
                .domain(domainNameResolver.resolveDomainName(webExchange))
                .secure(true)
                .httpOnly(true)
                .build();
    }
}
