package com.odeyalo.sonata.authorization.support.scope

import com.odeyalo.sonata.authorization.entity.Role
import com.odeyalo.sonata.authorization.support.scope.loader.SimpleScopeLoader
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito
import org.mockito.Mockito.times
import kotlin.test.assertEquals

/**
 * Tests for [DefaultCacheableSonataScopeProvider]
 */
class DefaultCacheableSonataScopeProviderTest {
    var scopes = listOf<Scope>(
        CommonScope("read", "Read info", setOf(Role.USER.roleValue, Role.ADMIN.roleValue), Scope.Type.PUBLIC),
        CommonScope("write", "Write info", setOf(Role.USER.roleValue, Role.ADMIN.roleValue), Scope.Type.PRIVATE),
        CommonScope("delete", "Delete info", setOf(Role.ADMIN.roleValue), Scope.Type.PUBLIC)
    )

    @Test
    @DisplayName("Get all scopes and expect not null")
    fun getAllScopes_AndExpectNotNullAsResult() {
        val provider = DefaultCacheableSonataScopeProvider(SimpleScopeLoader(scopes))

        val actualScopes = provider.scopes.toIterable().toList()

        assertEquals(scopes, actualScopes, "Scopes must be equal!")
    }

    @ParameterizedTest
    @EnumSource(Role::class)
    @DisplayName("Get all scopes for specific role")
    fun getScopesByRole_andExpectScopesOnlyForSpecificRole(role: Role) {
        val provider = DefaultCacheableSonataScopeProvider(SimpleScopeLoader(scopes))

        val expectedScopes = getScopesFor(role)

        val actualScopes = provider.getScopesByRole(role.roleValue).toIterable().toList()

        assertEquals(expectedScopes, actualScopes, "Scopes for role: $role must be loaded properly!")
    }

    @Test
    @DisplayName("get all scopes and expect scopes to be loaded from cache")
    fun getAllScopes_adnExpectScopesToBeLoadedFromCache() {
        val loader = Mockito.spy(SimpleScopeLoader(scopes))
        val provider = DefaultCacheableSonataScopeProvider(loader)
        // Call the methods to get scopes
        provider.getScopesByRole(Role.USER.roleValue).toIterable().toList()
        provider.getScopesByRole(Role.USER.roleValue).toIterable().toList()
        // assert that loader was called only once on bootstrap
        Mockito.verify(loader, times(1)).loadScopes()
    }

    private fun getScopesFor(role: Role): List<Scope> {
        return scopes.stream().filter { sc -> sc.supportsRole(role.roleValue) }.toList()
    }
}