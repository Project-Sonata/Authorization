package com.odeyalo.sonata.authorization.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.sonata.authorization.dto.SuccessfulRegistrationResponse;
import com.odeyalo.sonata.authorization.service.authentication.SonataAuthenticationProvider;
import com.odeyalo.sonata.authorization.testing.asserts.SuccessfulRegistrationResponseAssert;
import com.odeyalo.sonata.common.authentication.dto.request.UserRegistrationInfo;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureWebClient
class AuthorizationControllerTest {
    @MockBean
    SonataAuthenticationProvider sonataAuthenticationProvider;
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ObjectMapper objectMapper;

    String REGISTER_USER_ENDPOINT = "/authorization/register";

    @Test
    @DisplayName("Register the user with valid registration info and expect HTTP 200 status")
    void registerUserWithValidRegistrationInfo_andExpectStatusOK() throws Exception {
        // when
        WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationInfo();
        // then
        exchange.expectStatus().isOk();
    }

    @Test
    @DisplayName("Register the user with valid registration info and expect access token in response body")
    void registerUserWithValidRegistrationInfo_andExpectAccessTokenInResponse() throws Exception {
        // when
        WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationInfo();

        // then
        SuccessfulRegistrationResponse response = exchange.expectBody(SuccessfulRegistrationResponse.class)
                .returnResult().getResponseBody();

        SuccessfulRegistrationResponseAssert.from(response)
                .accessTokenNotNull();
    }

    @NotNull
    private WebTestClient.ResponseSpec prepareAndSendValidRegistrationInfo() throws JsonProcessingException {
        String email = "mikunakano@gmail.com", password = "HelloWorld123";

        UserRegistrationInfo registrationInfo = UserRegistrationInfo.builder()
                .email(email)
                .password(password)
                .birthdate(LocalDate.of(2002, Month.DECEMBER, 3))
                .notificationEnabled(false)
                .build();

        String jsonBody = objectMapper.writeValueAsString(registrationInfo);

        return sendRequestToRegistrationEndpoint(jsonBody);
    }

    @NotNull
    private WebTestClient.ResponseSpec sendRequestToRegistrationEndpoint(String jsonBody) {
        return webTestClient.post()
                .uri(REGISTER_USER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .exchange();
    }
}