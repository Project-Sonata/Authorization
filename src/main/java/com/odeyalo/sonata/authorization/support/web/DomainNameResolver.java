package com.odeyalo.sonata.authorization.support.web;

import org.springframework.web.server.ServerWebExchange;

/**
 * Resolve domain name from the given request
 */
public interface DomainNameResolver {

    String resolveDomainName(ServerWebExchange exchange);

}