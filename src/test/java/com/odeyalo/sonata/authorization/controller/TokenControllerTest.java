package com.odeyalo.sonata.authorization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.sonata.authorization.dto.AuthorizationResponse;
import com.odeyalo.sonata.authorization.dto.TokenIntrospectionRequest;
import com.odeyalo.sonata.authorization.dto.TokenIntrospectionResponse;
import com.odeyalo.sonata.authorization.testing.asserts.TokenIntrospectionResponseAssert;
import com.odeyalo.sonata.common.authentication.dto.LoginCredentials;
import com.odeyalo.sonata.common.authentication.dto.UserInfo;
import com.odeyalo.sonata.common.authentication.dto.response.AuthenticationResultResponse;
import com.odeyalo.sonata.suite.reactive.client.ReactiveAuthenticationClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static com.odeyalo.sonata.common.authentication.dto.AuthenticationProcessType.LOGIN_COMPLETED;
import static com.odeyalo.sonata.common.authentication.dto.LoginCredentials.of;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureWebClient
class TokenControllerTest {
    @MockBean
    private ReactiveAuthenticationClient authenticationClient;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;

    private String validAccessToken;

    private final UserInfo userinfo = UserInfo.of("1", "mikunakano@gmail.com");

    private final Logger logger = LoggerFactory.getLogger(TokenControllerTest.class);

    public static final String TOKEN_INFO_ENDPOINT = "/token/info";

    @BeforeAll
    void setUp() throws Exception {
        AuthenticationResultResponse response = new AuthenticationResultResponse(true, userinfo, LOGIN_COMPLETED, Collections.emptySet(), null);
        LoginCredentials credentials = of("Miku", "Nakano");
        Mockito.when(authenticationClient.login(credentials))
                .thenReturn(
                        Mono.just(
                                ResponseEntity.ok(Mono.just(response))
                        ));
        String body = objectMapper.writeValueAsString(credentials);

        logger.info("Obtained an access token: {}", validAccessToken);
    }
}