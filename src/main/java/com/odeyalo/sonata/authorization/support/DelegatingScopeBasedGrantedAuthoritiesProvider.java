package com.odeyalo.sonata.authorization.support;

import com.odeyalo.sonata.authorization.support.scope.SonataScopeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link GrantedAuthoritiesProvider} impl that provide {@link GrantedAuthority} from {@link SonataScopeProvider}
 *
 * In this case, authority is equal to scope name
 */
@Component
public class DelegatingScopeBasedGrantedAuthoritiesProvider implements GrantedAuthoritiesProvider {
    private final SonataScopeProvider scopeProvider;

    @Autowired
    public DelegatingScopeBasedGrantedAuthoritiesProvider(SonataScopeProvider scopeProvider) {
        this.scopeProvider = scopeProvider;
    }

    @Override
    public Mono<Set<GrantedAuthority>> getAuthorities(String role) {
        return scopeProvider.getScopesByRole(role)
                .map(scope -> new SimpleGrantedAuthority(scope.getName()))
                .collectList()
                .map(HashSet::new);
    }
}
