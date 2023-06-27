package com.odeyalo.sonata.authorization.service.registration.remote;

import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import com.odeyalo.sonata.authorization.testing.asserts.ErrorDetailsAssert;
import com.odeyalo.sonata.authorization.testing.faker.RegistrationFormFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

import static com.odeyalo.sonata.authorization.service.registration.RegistrationResult.Type.FAILED;
import static com.odeyalo.sonata.authorization.service.registration.RegistrationResult.Type.PENDING_CONFIRMATION;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link ReactiveFeignClientRemoteServiceUserRegistrar}
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWireMock(port = 0)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReactiveFeignClientRemoteServiceUserRegistrarTest {

    @Autowired
    ReactiveFeignClientRemoteServiceUserRegistrar remoteServiceUserRegistrar;


    @Nested
    class RegistrationWithValidInfo {
        @Test
        @DisplayName("Expect not null registration result")
        void expectNotNullResult() {
            // given
            RegistrationForm form = prepareRegistrationFormWithValidInfo();
            // when
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(form).block();
            // then
            assertNotNull(result, "Null cannot be returned if registration was success!");
        }

        @Test
        @DisplayName("Expect null as error details")
        void expectNullErrorDetails() {
            // given
            RegistrationForm form = prepareRegistrationFormWithValidInfo();
            // when
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(form).block();
            // then
            assertNull(result.getErrorDetails(), "If registration was success, then null error details must be null!");
        }

        @Test
        @DisplayName("Expect PENDING_CONFIRMATION as registration type")
        void expectPendingConfirmationType() {
            RegistrationForm validForm = prepareRegistrationFormWithValidInfo();
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(validForm).block();
            assertEquals(PENDING_CONFIRMATION, result.getRegistrationType(), "PENDING_CONFIRMATION must be returned if registration was success");
        }

        @Test
        @DisplayName("Expect basic info not null")
        void expectNotNullBasicInfo() {
            RegistrationForm validForm = prepareRegistrationFormWithValidInfo();
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(validForm).block();
            assertNotNull(result.getUserInfo(), "User info cannot be null!");
        }

        @Test
        @DisplayName("Expect basic info with null ID")
        void expectBasicInfoWithNullId() {
            RegistrationForm validForm = prepareRegistrationFormWithValidInfo();
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(validForm).block();
            assertNull(result.getUserInfo().id(), "Id must be null if PENDING_CONFIRMATION type is used!");
        }

        @Test
        @DisplayName("Expect basic info with email equal to provided in registration form")
        void expectBasicInfoWithNotNullEmail() {
            RegistrationForm validForm = prepareRegistrationFormWithValidInfo();
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(validForm).block();
            assertEquals(validForm.getUsername(), result.getUserInfo().username(), "Email must be equal to provided in registration form!");
        }

        private RegistrationForm prepareRegistrationFormWithValidInfo() {
            String email = "mikunakano@gmail.com", password = "HelloWorld123";
            return RegistrationFormFaker.getInstance().overrideUsername(email).overridePassword(password).build();
        }
    }

    @Nested
    class RegistrationWithInvalidInfo {
        @Test
        @DisplayName("Expect not null result")
        void expectNotNullResult() {
            RegistrationForm form = prepareRegistrationFormWithInvalidInfo();
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(form).block();
            assertNotNull(result);
        }

        @Test
        @DisplayName("Expect FAILED in result type")
        void expectFailedInResultType() {
            RegistrationForm form = prepareRegistrationFormWithInvalidInfo();
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(form).block();
            assertEquals(FAILED, result.getRegistrationType(), "If registration cannot be performed, then FAILED must be returned!");
        }

        @Test
        @DisplayName("Expect null basic user info")
        void expectNullForUserInfo() {
            RegistrationForm form = prepareRegistrationFormWithInvalidInfo();
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(form).block();
            assertNull(result.getUserInfo(), "User info must be null if registration was failed!");
        }

        @Test
        @DisplayName("Expect not null error details")
        void expectNotNullErrorDetails() {
            RegistrationForm form = prepareRegistrationFormWithInvalidInfo();
            RegistrationResult result = remoteServiceUserRegistrar.registerUser(form).block();
            assertNotNull(result.getErrorDetails(), "If registration has been failed, then error details must be returned!");
        }

        @Test
        @DisplayName("Expect 'invalid_password' error code")
        void expectInvalidPasswordErrorCode() {
            RegistrationForm form = prepareRegistrationFormWithInvalidInfo();

            RegistrationResult result = remoteServiceUserRegistrar.registerUser(form).block();

            ErrorDetailsAssert.from(result.getErrorDetails())
                    .codeEqualTo("invalid_password");
        }

        @Test
        @DisplayName("Expect description about error")
        void expectDescriptionAboutError() {
            RegistrationForm form = prepareRegistrationFormWithInvalidInfo();

            RegistrationResult result = remoteServiceUserRegistrar.registerUser(form).block();

            ErrorDetailsAssert.from(result.getErrorDetails())
                    .descriptionEqualTo("The password is invalid, password must contain at least 8 characters and 1 number");
        }

        @Test
        @DisplayName("Expect possible solution to fix the error")
        void expectSolutionForTheError() {
            RegistrationForm form = prepareRegistrationFormWithInvalidInfo();

            RegistrationResult result = remoteServiceUserRegistrar.registerUser(form).block();

            ErrorDetailsAssert.from(result.getErrorDetails())
                    .solutionEqualTo("To fix the problem - input the correct password with required format");
        }

        private RegistrationForm prepareRegistrationFormWithInvalidInfo() {
            String email = "invalid@gmail.com", password = "invalid";
            return RegistrationFormFaker.getInstance().overrideUsername(email).overridePassword(password).build();
        }
    }

}