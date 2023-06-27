package com.odeyalo.sonata.authorization.support;

import com.odeyalo.sonata.authorization.entity.Role;
import com.odeyalo.sonata.authorization.support.scope.CommonScope;
import com.odeyalo.sonata.authorization.support.scope.DefaultCacheableSonataScopeProvider;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import com.odeyalo.sonata.authorization.support.scope.loader.SimpleScopeLoader;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for {@link DelegatingScopeBasedGrantedAuthoritiesProvider]
 */
class DelegatingScopeBasedGrantedAuthoritiesProviderTest {
    List<Scope> scopes = List.of(
            new CommonScope("read", "Read info", Set.of(Role.USER.getRoleValue(), Role.ADMIN.getRoleValue())),
            new CommonScope("write", "Write info", Set.of(Role.USER.getRoleValue(), Role.ADMIN.getRoleValue())),
            new CommonScope("delete", "Delete info", Set.of(Role.ADMIN.getRoleValue()))
    );

    DelegatingScopeBasedGrantedAuthoritiesProvider provider = new DelegatingScopeBasedGrantedAuthoritiesProvider(
            new DefaultCacheableSonataScopeProvider(new SimpleScopeLoader(scopes))
    );

    @Test
    @DisplayName("Get authorities for user and expect not null authorities")
    void getAuthoritiesForUserRole_andExpectNotNullAuthoritiesToLoad() {
        Set<GrantedAuthority> authorities = provider.getAuthorities(Role.USER).block();
        assertNotNull(authorities, "Authorities cannot be null!");
    }

    @Test
    @DisplayName("Get authorities for user and expect authorities to be returned")
    void getAuthoritiesForUserRole_andExpectAuthoritiesAsResult() {
        int expectedAuthoritiesSize = 2;
        Set<GrantedAuthority> authorities = provider.getAuthorities(Role.USER).block();
        assertEquals(expectedAuthoritiesSize, authorities.size(), String.format("Authorities must be loaded for user role and expect size is: %s", expectedAuthoritiesSize));
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    @DisplayName("Get authorities for specific role and expect authorities only for this role to be returned")
    void getAuthoritiesForUserRole_andExpectAuthorityNamesOnlyForUserAsResult(Role role) {
        List<String> onlyUserAuthorities = getAuthorityNamesFor(role);

        List<String> authorities = provider.getAuthorities(role).block()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();

        assertEquals(onlyUserAuthorities, new ArrayList<>(authorities), String.format("Authorities must contain only <%s> authorities", role));
    }

    @NotNull
    private List<String> getAuthorityNamesFor(Role role) {
        return scopes.stream()
                .filter(scope -> scope.supportsRole(role.getRoleValue()))
                .map(Scope::getName).toList();
    }

}