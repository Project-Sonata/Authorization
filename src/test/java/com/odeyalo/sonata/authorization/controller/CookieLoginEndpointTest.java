package com.odeyalo.sonata.authorization.controller;

import com.odeyalo.sonata.common.authentication.dto.LoginCredentials;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Test the /login endpoint that should set cookie in headers
 */
@AutoConfigureWireMock(port = 9010)
@TestPropertySource(value = "classpath:session-authentication.properties")
public class CookieLoginEndpointTest extends AuthorizationControllerTest {

    public static final String COOKIE_SSO_NAME = "sntsso";
    @Autowired
    WebTestClient webTestClient;

    static final String VALID_USERNAME = "mikunakano@gmail.com";
    static final String VALID_PASSWORD = "HelloWorld123";

    @Test
    void shouldReturn200OkStatusCode() {
        WebTestClient.ResponseSpec responseSpec = prepareValidAndSend();

        responseSpec.expectStatus().isOk();
    }

    @Test
    void shouldReturnCookieInResponse() {
        WebTestClient.ResponseSpec responseSpec = prepareValidAndSend();

        responseSpec.expectCookie().exists(COOKIE_SSO_NAME);
    }

    @Test
    void shouldReturnCookieWithDomainName() {
        WebTestClient.ResponseSpec responseSpec = prepareValidAndSend();

        responseSpec.expectCookie().domain(COOKIE_SSO_NAME, "sonata.com");
    }

    @Test
    void shouldReturnSecuredCookie() {
        WebTestClient.ResponseSpec responseSpec = prepareValidAndSend();

        responseSpec.expectCookie().secure(COOKIE_SSO_NAME, true);
    }

    @Test
    void shouldReturnHttpOnlyCookie() {
        WebTestClient.ResponseSpec responseSpec = prepareValidAndSend();

        responseSpec.expectCookie().httpOnly(COOKIE_SSO_NAME, true);
    }

    @NotNull
    private WebTestClient.ResponseSpec prepareValidAndSend() {
        LoginCredentials creds = LoginCredentials.of(VALID_USERNAME, VALID_PASSWORD);

        return sendRequest(creds);
    }

    @NotNull
    private WebTestClient.ResponseSpec sendRequest(LoginCredentials creds) {
        return webTestClient.post()
                .uri("/authorization/login")
                .contentType(APPLICATION_JSON)
                .bodyValue(creds)
                .exchange();
    }
}
