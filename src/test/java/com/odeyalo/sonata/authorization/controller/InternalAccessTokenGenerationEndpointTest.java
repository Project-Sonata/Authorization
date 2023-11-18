package com.odeyalo.sonata.authorization.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

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
        WebTestClient.ResponseSpec responseSpec = webTestClient.post()
                .uri(builder -> builder.path("/internal/oauth/token/access")
                        .queryParam("user_id", 123)
                        .queryParam("scope", "read write profile").build()).exchange();


        responseSpec.expectStatus().isOk();
    }
}
