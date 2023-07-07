package com.odeyalo.sonata.authorization.testing.contract;

import com.odeyalo.sonata.authorization.AuthorizationApplication;
import com.odeyalo.sonata.authorization.controller.TokenController;
import com.odeyalo.sonata.authorization.entity.InMemoryAccessToken;
import com.odeyalo.sonata.authorization.service.token.access.AccessTokenManager;
import com.odeyalo.sonata.authorization.service.token.access.ScopeBasedDelegatingPersistentAccessTokenManager;
import com.odeyalo.sonata.authorization.support.scope.CommonScope;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Base class for Spring contract that using {@link TokenController}
 */
@AutoConfigureMessageVerifier
@SpringBootTest(classes = AuthorizationApplication.class, webEnvironment = RANDOM_PORT)
@TestInstance(PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TokenControllerBaseClass {

    public static final String VALID_TOKEN_VALUE = "mikunakanoisthebestgirl";

    @MockBean
    AccessTokenManager tokenManager;

    @Autowired
    TokenController tokenController;

    @LocalServerPort
    int port;

    List<CommonScope> scopes = List.of(
            new CommonScope("user-account-modify", "Read and modify user account", Set.of("user"), Scope.Type.PRIVATE),
            new CommonScope("write", "write something", Set.of("user"), Scope.Type.PUBLIC)
    );

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;

        Mockito.when(tokenManager.verifyToken(VALID_TOKEN_VALUE))
                .thenReturn(
                        Mono.just(
                                InMemoryAccessToken.builder()
                                        .claim(ScopeBasedDelegatingPersistentAccessTokenManager.SCOPES_CLAIM_NAME, scopes)
                                        .userId("1")
                                        .creationTime(System.currentTimeMillis() - 60000)
                                        .expirationTime(System.currentTimeMillis() + 12000)
                                        .tokenValue(VALID_TOKEN_VALUE)
                                        .build()
                        )
                );
        Mockito.when(tokenManager.verifyToken(not(eq(VALID_TOKEN_VALUE))))
                .thenReturn(Mono.empty());
    }
}
