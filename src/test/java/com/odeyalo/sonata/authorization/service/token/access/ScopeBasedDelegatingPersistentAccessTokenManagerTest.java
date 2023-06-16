package com.odeyalo.sonata.authorization.service.token.access;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.entity.InMemoryUser;
import com.odeyalo.sonata.authorization.support.scope.CommonScope;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import com.odeyalo.sonata.authorization.testing.asserts.AccessTokenAssert;
import com.odeyalo.sonata.authorization.testing.factory.AccessTokenManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

import static com.odeyalo.sonata.authorization.service.token.access.ScopeBasedDelegatingPersistentAccessTokenManager.SCOPES_CLAIM_NAME;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link ScopeBasedDelegatingPersistentAccessTokenManager}
 */
class ScopeBasedDelegatingPersistentAccessTokenManagerTest {

    private final InMemoryUser user = InMemoryUser.builder()
            .id(1L)
            .creationTime(System.currentTimeMillis())
            .authorities(Set.of(new SimpleGrantedAuthority("USER")))
            .role("USER")
            .businessKey("user-key")
            .build();

    private final InMemoryUser admin = InMemoryUser.builder()
            .id(2L)
            .creationTime(System.currentTimeMillis())
            .authorities(Set.of(new SimpleGrantedAuthority("ADMIN")))
            .role("ADMIN")
            .businessKey("admin-key")
            .build();

    private final List<Scope> scopes = List.of(
            new CommonScope("name1", "desc", Set.of("user", "admin")),
            new CommonScope("name2", "desc2", Set.of("user", "admin")),
            new CommonScope("name3", "desc3", Set.of("admin"))
    );

    private final List<Scope> onlyUserScopes = scopes.stream().filter(scope -> scope.supportsRole("user")).toList();

    private final List<Scope> onlyAdminScopes = scopes.stream().filter(scope -> scope.supportsRole("admin")).toList();

    @Test
    @DisplayName("Generate an access token for user and expect token with user scopes to be generated")
    void createToken_andExpectTokenToBeGenerated() {
        // given
        ScopeBasedDelegatingPersistentAccessTokenManager manager = AccessTokenManagerFactory.scopeBasedManager(scopes);
        // when
        AccessToken token = manager.createToken(user).block();
        //then
        AccessTokenAssert.from(token)
                .everythingIsNotNull()
                .userId(user.getId())
                .containsClaim(SCOPES_CLAIM_NAME)
                .claimEqual(SCOPES_CLAIM_NAME, onlyUserScopes);
    }

    @Test
    @DisplayName("Generate an access token for admin user and expect token with only admin scopes")
    void generateAccessTokenForAdmin_andExpectTokenWithAdminScopes() {
        // given
        ScopeBasedDelegatingPersistentAccessTokenManager manager = AccessTokenManagerFactory.scopeBasedManager(scopes);
        // when
        AccessToken token = manager.createToken(admin).block();
        // then
        AccessTokenAssert.from(token)
                .everythingIsNotNull()
                .userId(admin.getId())
                .containsClaim(SCOPES_CLAIM_NAME)
                .claimEqual(SCOPES_CLAIM_NAME, onlyAdminScopes);
    }

    @Test
    @DisplayName("Generate token and expect token to be saved to persistent storage, check if returned token and saved are equal")
    void generateToken_andExpectTokenToBeSavedInStorageAndEqualWithGenerated() {
        // given
        ScopeBasedDelegatingPersistentAccessTokenManager manager = AccessTokenManagerFactory.scopeBasedManager(scopes);
        // when
        AccessToken generatedToken = manager.createToken(user).block();
        // then
        AccessToken fromStorage = manager.verifyToken(generatedToken.getTokenValue()).block();

        AccessTokenAssert.from(generatedToken)
                .as("The generated and saved tokens must be equal to each other!")
                .isEqualTo(fromStorage);
    }

    @Test
    @DisplayName("Verify non-existing token and expect null to be returned")
    void verifyTokenThatDoesNotExist_andExpectNullAsResult() {
        // given
        ScopeBasedDelegatingPersistentAccessTokenManager manager = AccessTokenManagerFactory.scopeBasedManager(scopes);
        // when
        AccessToken token = manager.verifyToken("mikuiloveyou<3").block();
        // then
        assertNull(token, "Token must be null if not-existing value was used!");
    }
}