package com.odeyalo.sonata.authorization.support;

import com.odeyalo.sonata.authorization.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Provide the {@link org.springframework.security.core.GrantedAuthority} for specific role
 */
public interface GrantedAuthoritiesProvider {

    default Mono<Set<GrantedAuthority>> getAuthorities(Role role) {
        return getAuthorities(role.getRoleValue());
    }

    Mono<Set<GrantedAuthority>> getAuthorities(String role);
}
