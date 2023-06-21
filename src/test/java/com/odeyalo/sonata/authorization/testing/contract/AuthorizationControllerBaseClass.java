package com.odeyalo.sonata.authorization.testing.contract;

import com.odeyalo.sonata.authorization.AuthorizationApplication;
import com.odeyalo.sonata.authorization.controller.AuthorizationController;
import com.odeyalo.sonata.authorization.service.authentication.SonataAuthentication;
import com.odeyalo.sonata.authorization.service.authentication.SonataAuthenticationProvider;
import com.odeyalo.sonata.authorization.service.token.access.GeneratedAccessToken;
import com.odeyalo.sonata.authorization.service.token.access.generator.AccessTokenGenerator;
import com.odeyalo.sonata.common.authentication.dto.UserInfo;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.odeyalo.sonata.authorization.service.token.access.ScopeBasedDelegatingPersistentAccessTokenManager.SCOPES_CLAIM_NAME;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Base class for Spring contract that using {@link AuthorizationController}
 */
@AutoConfigureMessageVerifier
@SpringBootTest(classes = AuthorizationApplication.class, webEnvironment = RANDOM_PORT)
@TestInstance(PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthorizationControllerBaseClass {

    @MockBean
    SonataAuthenticationProvider authenticationProvider;

    @SpyBean
    AccessTokenGenerator accessTokenGenerator;

    @LocalServerPort
    int port;

    String EMAIL = "mikunakano@gmail.com";
    String PASSWORD = "MikuNakano1488";
    String USER_ID = "1";

    String TOKEN_VALUE = "MikuNakanoIsMyLove";

    List<String> SCOPES = List.of("read write");


    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
        UserInfo info = UserInfo.of(USER_ID, "");

        when(authenticationProvider.obtainAuthentication(EMAIL, PASSWORD))
                .thenReturn(Mono.just(
                        new SonataAuthentication(info, PASSWORD, info, Set.of(new SimpleGrantedAuthority("USER")))
                ));

        GeneratedAccessToken token = accessTokenGenerator.generateAccessToken(USER_ID, Map.of(SCOPES_CLAIM_NAME, SCOPES)).block();

        when(accessTokenGenerator.generateAccessToken(anyString(), anyMap()))
                .thenReturn(
                        Mono.just(
                                GeneratedAccessToken.of(TOKEN_VALUE,
                                        token.getCreationTimeMs(),
                                        token.getExpiresInMs(),
                                        token.getUserId(),
                                        token.getClaims())
                        )
                );
    }
}
