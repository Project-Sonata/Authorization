package com.odeyalo.sonata.authorization.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.sonata.authorization.dto.FailedRegistrationConfirmationDto;
import com.odeyalo.sonata.authorization.dto.SuccessfulRegistrationConfirmationDto;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.odeyalo.sonata.authorization.dto.SuccessfulRegistrationConfirmationDto.ACCESS_TOKEN_KEY;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWireMock(port = 0)
public class RegistrationConfirmationEndpointAuthorizationControllerTest extends AuthorizationControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    class ConfirmationWithValidCode {

        @Test
        @DisplayName("Expect HTTP 200 OK status")
        void expectHttpOkStatus() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationConfirmationData();
            exchange.expectStatus().isOk();
        }

        @Test
        @DisplayName("Expect 'application/json' content type ")
        void expectApplicationJsonContentType() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationConfirmationData();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        @DisplayName("Expect response body that can be parsed")
        void expectBodyToBeParsed() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationConfirmationData();
            exchange.expectBody(SuccessfulRegistrationConfirmationDto.class);
        }

        @Test
        @DisplayName("Expect true in 'confirmed' field")
        void expectTrueInConfirmationResult() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationConfirmationData();
            SuccessfulRegistrationConfirmationDto body = exchange.expectBody(SuccessfulRegistrationConfirmationDto.class)
                    .returnResult().getResponseBody();
            assertTrue(body.isConfirmed(), "'confirmed' must be true if confirmation was success!");
        }

        @Test
        @DisplayName("Expect access token in response body")
        void expectAccessTokenInResponseBody() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationConfirmationData();
            SuccessfulRegistrationConfirmationDto body = exchange.expectBody(SuccessfulRegistrationConfirmationDto.class)
                    .returnResult().getResponseBody();
            assertTrue(body.contains(ACCESS_TOKEN_KEY), "Tokens must contain access token!");
        }

        @Test
        @DisplayName("Expect access token with not empty value in response body")
        void expectAccessTokenWithNotEmptyValueInResponseBody() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationConfirmationData();

            SuccessfulRegistrationConfirmationDto body = exchange.expectBody(SuccessfulRegistrationConfirmationDto.class)
                    .returnResult().getResponseBody();

            assertNotNull(body.getToken(ACCESS_TOKEN_KEY).getValue(), "Access token must contain token value!");
        }

        @Test
        @DisplayName("Expect access token with positive token lifetime in response body")
        void expectAccessTokenWithPositiveLifetimeInResponseBody() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationConfirmationData();

            SuccessfulRegistrationConfirmationDto body = exchange.expectBody(SuccessfulRegistrationConfirmationDto.class)
                    .returnResult().getResponseBody();

            assertTrue(body.getToken(ACCESS_TOKEN_KEY).getLifetime() > 0, "Token lifetime must be positive number!");
        }

        private WebTestClient.ResponseSpec prepareAndSendValidRegistrationConfirmationData() throws JsonProcessingException {
            RegistrationConfirmationData data = RegistrationConfirmationData.of("123");
            String body = objectMapper.writeValueAsString(data);
            return sendRequestToRegistrationConfirmationEndpoint(body);
        }
    }

    @Nested
    class ConfirmationWithInvalidCode {

        @Test
        @DisplayName("Expect 400 status")
        void expectBadRequestStatus() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendInvalidConfirmationData();
            exchange.expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("Expect 'application/json' content type")
        void expectApplicationJsonContentType() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendInvalidConfirmationData();
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        @DisplayName("Expect response body to be parsable")
        void expectParsableBodyInResponse() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendInvalidConfirmationData();
            exchange.expectBody(FailedRegistrationConfirmationDto.class);
        }

        public WebTestClient.ResponseSpec prepareAndSendInvalidConfirmationData() throws JsonProcessingException {
            RegistrationConfirmationData data = RegistrationConfirmationData.of("900");
            String body = objectMapper.writeValueAsString(data);
            return sendRequestToRegistrationConfirmationEndpoint(body);
        }

    }

    @NotNull
    private WebTestClient.ResponseSpec sendRequestToRegistrationConfirmationEndpoint(String body) {
        return webTestClient.post()
                .uri("/authorization/register/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange();
    }
}
