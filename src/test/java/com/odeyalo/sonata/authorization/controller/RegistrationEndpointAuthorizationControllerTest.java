package com.odeyalo.sonata.authorization.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.sonata.authorization.dto.RegistrationFormDto;
import com.odeyalo.sonata.authorization.dto.RegistrationResultResponseDto;
import com.odeyalo.sonata.authorization.testing.asserts.ErrorDetailsAssert;
import com.odeyalo.sonata.authorization.testing.asserts.RegistrationResultResponseDtoAssert;
import com.odeyalo.sonata.authorization.testing.faker.RegistrationFormFaker;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.Function;

/**
 * Tests for '/authorization/register' endpoint
 */
@AutoConfigureWireMock(port = 0)
class RegistrationEndpointAuthorizationControllerTest extends AuthorizationControllerTest {
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ObjectMapper objectMapper;

    String REGISTER_USER_ENDPOINT = "/authorization/register";

    @Nested
    class UserRegistrationWithValidInfo {

        @Test
        @DisplayName("Register the user with valid registration info and expect HTTP 200 status")
        void registerUserWithValidRegistrationInfo_andExpectStatusOK() throws Exception {
            // when
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationInfo();
            // then
            exchange.expectStatus().isOk();
        }

        @Test
        @DisplayName("Register user with valid registration info and expect 'application/json' content type")
        void registerUserWithValidRegistrationInfo_andExpectContentTypeApplicationJson() throws Exception {
            // when
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationInfo();
            // then
            exchange.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        @DisplayName("Register the user with valid registration info and null error details since there was no errors")
        void registerUserWithValidRegistrationInfo_andExpectAccessTokenInResponse() throws Exception {
            // when
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationInfo();

            // then
            RegistrationResultResponseDto response = exchange.expectBody(RegistrationResultResponseDto.class)
                    .returnResult().getResponseBody();

            RegistrationResultResponseDtoAssert.from(response)
                    .errorDetails()
                    .equalToNull();
        }

        @Test
        @DisplayName("Register the user with valid registration info and expect PENDING_CONFIRMATION result type")
        void registerWithValidRegistrationInfo_andExpectPendingConfirmationResultType() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendValidRegistrationInfo();
            // then
            RegistrationResultResponseDto responseBody = exchange.expectBody(RegistrationResultResponseDto.class)
                    .returnResult().getResponseBody();

            RegistrationResultResponseDtoAssert.from(responseBody)
                    .pendingConfirmation();
        }

        @NotNull
        private WebTestClient.ResponseSpec prepareAndSendValidRegistrationInfo() throws JsonProcessingException {
            String email = "mikunakano@gmail.com", password = "HelloWorld123";

            RegistrationFormDto registrationInfo = RegistrationFormDto.builder()
                    .username(email)
                    .password(password)
                    .gender(RegistrationFormFaker.Gender.FEMALE)
                    .birthdate(LocalDate.of(2002, Month.DECEMBER, 3))
                    .enableNotification(false)
                    .build();

            String jsonBody = objectMapper.writeValueAsString(registrationInfo);

            return sendRequestToRegistrationEndpoint(jsonBody);
        }
    }

    @Nested
    class UserRegistrationWithInvalidInfo {

        @Test
        @DisplayName("Register user with invalid info and expect HTTP 400")
        void expectStatusHttp400() throws Exception {
            WebTestClient.ResponseSpec exchange = prepareAndSendInvalidRegistrationInfoRequest();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("Register user with invalid info and expect 'application/json' content type")
        void expectContentTypeApplicationJson() throws Exception {
            WebTestClient.ResponseSpec response = prepareAndSendInvalidRegistrationInfoRequest();

            response.expectHeader().contentType(MediaType.APPLICATION_JSON);
        }

        @Test
        @DisplayName("Register user with invalid info(password) and expect error details with 'invalid_password' in error code")
        void expectErrorDetailsWithInvalidPasswordCode_IfPasswordIsInvalid() throws Exception {
            WebTestClient.ResponseSpec response = prepareAndSendInvalidRegistrationInfoRequest();
            // then
            ErrorDetails details = response.expectBody(ErrorDetails.class).returnResult().getResponseBody();

            ErrorDetailsAssert.from(details)
                    .codeEqualTo("invalid_password");
        }

        @Test
        @DisplayName("Register user with invalid info(password) and expect error details with description in error code")
        void expectErrorDetailsWithDescription_IfPasswordIsInvalid() throws Exception {
            WebTestClient.ResponseSpec response = prepareAndSendInvalidRegistrationInfoRequest();
            // then
            ErrorDetails details = response.expectBody(ErrorDetails.class).returnResult().getResponseBody();

            ErrorDetailsAssert.from(details)
                    .descriptionEqualTo("The password is invalid, password must contain at least 8 characters and 1 number");
        }

        @Test
        @DisplayName("Register user with invalid info(password) and expect error details with description in error code")
        void expectErrorDetailsWithSolution_IfPasswordIsInvalid() throws Exception {
            WebTestClient.ResponseSpec response = prepareAndSendInvalidRegistrationInfoRequest();
            // then
            ErrorDetails details = response.expectBody(ErrorDetails.class)
                    .returnResult().getResponseBody();

            ErrorDetailsAssert.from(details)
                    .solutionEqualTo("To fix the problem - input the correct password with required format");
        }

        private WebTestClient.ResponseSpec prepareAndSendInvalidRegistrationInfoRequest() throws JsonProcessingException {
            return prepareAndSendInvalidRegistrationInfoRequest(info -> info);
        }

        /**
         * Prepares the invalid registration info and send the request to server
         *
         * @param enhancer - function to customize the UserRegistrationInfo
         * @return - result of the request
         * @throws JsonProcessingException - if json can't be build
         */
        private WebTestClient.ResponseSpec prepareAndSendInvalidRegistrationInfoRequest(Function<RegistrationFormDto, RegistrationFormDto> enhancer) throws JsonProcessingException {
            String email = "invalid@gmail.com", password = "invalid";

            RegistrationFormDto registrationInfo = RegistrationFormDto.builder()
                    .username(email)
                    .password(password)
                    .birthdate(LocalDate.of(2002, Month.DECEMBER, 3))
                    .gender(RegistrationFormFaker.Gender.MALE)
                    .enableNotification(false)
                    .build();

            registrationInfo = enhancer.apply(registrationInfo);

            String jsonBody = objectMapper.writeValueAsString(registrationInfo);

            return sendRequestToRegistrationEndpoint(jsonBody);
        }
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