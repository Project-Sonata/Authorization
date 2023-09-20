package com.odeyalo.sonata.authorization.support.web;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * Always return static domain name
 */
@Component
@Profile("!prod")
public class StaticDomainNameResolver implements DomainNameResolver {
    private static final String DOMAIN_NAME_DEFAULT = "sonata.com";
    private String domainName = DOMAIN_NAME_DEFAULT;

    public StaticDomainNameResolver() {
    }

    public StaticDomainNameResolver(String domainName) {
        this.domainName = domainName;
    }

    @Override
    public String resolveDomainName(ServerWebExchange exchange) {
        return domainName;
    }
}
