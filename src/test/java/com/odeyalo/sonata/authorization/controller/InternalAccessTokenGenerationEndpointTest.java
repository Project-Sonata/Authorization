package com.odeyalo.sonata.authorization.controller;


import com.odeyalo.sonata.authorization.dto.GeneratedInternalAccessTokenResponseDto;
import com.odeyalo.sonata.authorization.testing.GeneratedInternalAccessTokenResponseDtoAssert;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.odeyalo.sonata.authorization.testing.GeneratedInternalAccessTokenResponseDtoAssert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureWebClient
public class InternalAccessTokenGenerationEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void shouldReturnOkStatus() {
        WebTestClient.ResponseSpec responseSpec = sendValidRequest();

        responseSpec.expectStatus().isOk();
    }

    @Test
    void shouldReturnApplicationJsonContentType() {
        WebTestClient.ResponseSpec responseSpec = sendValidRequest();

        responseSpec.expectHeader().contentType(APPLICATION_JSON);
    }

    @Test
    void shouldReturnParseableBody() {
        WebTestClient.ResponseSpec responseSpec = sendValidRequest();

        var responseBody = responseSpec.expectBody(GeneratedInternalAccessTokenResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
    }

    @Test
    void shouldReturnBodyWithToken() {
        WebTestClient.ResponseSpec responseSpec = sendValidRequest();

        var responseBody = responseSpec.expectBody(GeneratedInternalAccessTokenResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).accessToken().isNotNull();
    }

    @NotNull
    private WebTestClient.ResponseSpec sendValidRequest() {
        return webTestClient.post()
                .uri(builder -> builder.path("/internal/oauth/token/access")
                        .queryParam("user_id", 123)
                        .queryParam("scope", "read write profile").build()).exchange();
    }
}
