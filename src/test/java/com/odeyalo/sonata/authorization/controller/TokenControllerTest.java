package com.odeyalo.sonata.authorization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.sonata.authorization.entity.AccessToken;
import com.odeyalo.sonata.authorization.entity.Role;
import com.odeyalo.sonata.authorization.service.authentication.Subject;
import com.odeyalo.sonata.authorization.service.token.access.AccessTokenManager;
import com.odeyalo.sonata.authorization.support.GrantedAuthoritiesProvider;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionRequest;
import com.odeyalo.sonata.common.authorization.TokenIntrospectionResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureWebClient
class TokenControllerTest {
    public static final String USER_ID = "1";
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccessTokenManager accessTokenManager;
    @Autowired
    GrantedAuthoritiesProvider authoritiesProvider;

    private String validAccessToken;

    private final Logger logger = LoggerFactory.getLogger(TokenControllerTest.class);

    public static final String TOKEN_INFO_ENDPOINT = "/token/info";

    @BeforeAll
    void setUp() throws Exception {
        Set<GrantedAuthority> authorities = authoritiesProvider.getAuthorities(Role.USER).block();
        Subject subject = Subject.of(USER_ID, "mikunakano@gmail.com", Role.USER.getRoleValue(), authorities);

        AccessToken accessToken = accessTokenManager.createToken(subject).block();

        validAccessToken = accessToken.getTokenValue();

        logger.info("Obtained an access token: {}", validAccessToken);
    }

    @Nested
    class TokenInfoForInvalidToken {
        @Test
        @DisplayName("Expect 200 OK status")
        void expectOkStatus() {
            WebTestClient.ResponseSpec exchange = prepareAndSendInvalidRequest();
            exchange.expectStatus().isOk();
        }

        @Test
        @DisplayName("Expect 'application/json' content type")
        void expectApplicationJsonContentType() {
            WebTestClient.ResponseSpec exchange = prepareAndSendInvalidRequest();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        @DisplayName("Expect parsable body in response")
        void expectParsableBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendInvalidRequest();
            TokenIntrospectionResponse body = exchange.expectBody(TokenIntrospectionResponse.class).returnResult().getResponseBody();
            assertNotNull(body, "Body cannot be null!");
        }

        @Test
        @DisplayName("Expect false value in 'valid' field")
        void expectFalseInValidField() {
            WebTestClient.ResponseSpec exchange = prepareAndSendInvalidRequest();
            TokenIntrospectionResponse body = exchange.expectBody(TokenIntrospectionResponse.class).returnResult().getResponseBody();
            assertFalse(body.isValid(), "If token is not valid, then false must be set in 'valid' field!");
        }


        private WebTestClient.ResponseSpec prepareAndSendInvalidRequest() {
            TokenIntrospectionRequest invalidToken = TokenIntrospectionRequest.of("InMyRestlessDreamsISeeThatTown");
            return sendTokenIntrospectionRequest(invalidToken);
        }
    }

    @Nested
    class TokenInfoForValidToken {
        @Test
        @DisplayName("Expect 200 OK status")
        void expectOkStatus() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRequest();
            exchange.expectStatus().isOk();
        }

        @Test
        @DisplayName("Expect 'application/json' content type")
        void expectApplicationJsonContentType() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRequest();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        @DisplayName("Expect parsable body in response")
        void expectParsableBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRequest();
            TokenIntrospectionResponse body = exchange.expectBody(TokenIntrospectionResponse.class).returnResult().getResponseBody();
            assertNotNull(body, "Body cannot be null!");
        }

        @Test
        @DisplayName("Expect 'valid' field set to true")
        void expectValidFieldSetToTrue() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRequest();
            TokenIntrospectionResponse body = exchange.expectBody(TokenIntrospectionResponse.class).returnResult().getResponseBody();
            assertTrue(body.isValid(), "If token is valid, then true must be set in 'valid' field");
        }

        @Test
        @DisplayName("Expect 'user_id' field set to proper value")
        void expectUserIdFieldSetToProperValue() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRequest();
            TokenIntrospectionResponse body = exchange.expectBody(TokenIntrospectionResponse.class).returnResult().getResponseBody();
            assertEquals(USER_ID, body.getUserId(), "If token is valid, then true must be set in 'valid' field");
        }

        @Test
        @DisplayName("Expect 'expires_in' is greater than 0")
        void expectExpiresInGreaterThan0() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRequest();
            TokenIntrospectionResponse body = exchange.expectBody(TokenIntrospectionResponse.class).returnResult().getResponseBody();
            assertTrue(body.getExpiresIn() > 0, "Expires in must be greater than 0!");
        }

        @Test
        @DisplayName("Expect 'iat' is greater than 0")
        void expectIssuedAtGreaterThan0() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRequest();
            TokenIntrospectionResponse body = exchange.expectBody(TokenIntrospectionResponse.class).returnResult().getResponseBody();
            assertTrue(body.getIssuedAt() > 0, "Issued at must be greater than 0!");
        }


        @Test
        @DisplayName("Expect not null 'scopes' in response body")
        void expectNotNullScopesInResponseBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRequest();
            TokenIntrospectionResponse body = exchange.expectBody(TokenIntrospectionResponse.class).returnResult().getResponseBody();
            assertNotNull(body.getScope(), "Scopes must be not null!");
        }

        @Test
        @DisplayName("Expect 'scopes' in response body")
        void expectScopesInResponseBody() {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRequest();
            TokenIntrospectionResponse body = exchange.expectBody(TokenIntrospectionResponse.class).returnResult().getResponseBody();
            String[] scopes = body.getScope().split(" ");
            assertTrue(scopes.length > 0, "Scopes must be presented in response!");
        }

        private WebTestClient.ResponseSpec prepareAndSendValidRequest() {
            TokenIntrospectionRequest introspectionRequest = TokenIntrospectionRequest.of(validAccessToken);
            return sendTokenIntrospectionRequest(introspectionRequest);
        }
    }

    @NotNull
    private WebTestClient.ResponseSpec sendTokenIntrospectionRequest(TokenIntrospectionRequest token) {
        return webTestClient.post()
                .uri(TOKEN_INFO_ENDPOINT)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(token)
                .exchange();
    }
}