package com.odeyalo.sonata.authorization.support.web.enhancer;

import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Enhance the ServerWebExchange for endpoint that responsive for login
 */
public interface LoginEndpointResponseEnhancer {

    Mono<ServerWebExchange> enhance(AuthenticationPayload authenticationPayload, ServerWebExchange serverWebExchange);

}
