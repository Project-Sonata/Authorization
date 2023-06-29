package com.odeyalo.sonata.authorization.service.registration.remote;

import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import com.odeyalo.sonata.authorization.testing.asserts.ErrorDetailsAssert;
import com.odeyalo.sonata.authorization.testing.faker.RegistrationFormFaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import static com.odeyalo.sonata.authorization.service.registration.RegistrationResult.Type.FAILED;
import static com.odeyalo.sonata.authorization.service.registration.RegistrationResult.Type.PENDING_CONFIRMATION;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link ReactiveFeignClientRemoteServiceUserRegistrar}
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWireMock(port = 1111)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReactiveFeignClientRemoteServiceUserRegistrarTest {

    @Autowired
    ReactiveFeignClientRemoteServiceUserRegistrar remoteServiceUserRegistrar;

    @Nested
    @Import(SharedReference.class)
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
    @Import(SharedReference.class)
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

    @Nested
    @Import(SharedReference.class)
    class ConfirmationWithValidInfo {
        public static final String VALID_CONFIRMATION_DATA = "123";

        @Test
        @DisplayName("Expect not null as result")
        void expectNotNull() {
            RegistrationConfirmationData data = prepareValidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(data).block();
            assertNotNull(result, "Result should never be null!");
        }

        @Test
        @DisplayName("Expect confirmed flag set to true")
        void expectConfirmedFlagIsTrue() {
            RegistrationConfirmationData data = prepareValidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(data).block();
            assertTrue(result.isConfirmed(), "If data was valid then confirmed flag must be set to true!");
        }

        @Test
        @DisplayName("Expect failed flag set to false")
        void expectFailedFlagIsFalse() {
            RegistrationConfirmationData data = prepareValidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(data).block();
            assertFalse(result.isFailed(), "If data was valid then 'failed' flag must be set to false!");
        }

        @Test
        @DisplayName("Expect null error details")
        void expectNullErrorDetails() {
            RegistrationConfirmationData form = prepareValidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(form).block();
            assertNull(result.getErrorDetails(), "Error details must be null, if registration was success!");
        }

        @Test
        @DisplayName("Expect not null user info")
        void expectNotNullUserInfo() {
            RegistrationConfirmationData form = prepareValidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(form).block();
            assertNotNull(result.getUserInfo(), "User info cannot be null if confirmation was success!");
        }

        @Test
        @DisplayName("Expect ID in user info")
        void expectIDInUserInfo() {
            RegistrationConfirmationData form = prepareValidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(form).block();
            assertEquals("1", result.getUserInfo().id(), "User info must contain ID if confirmation was success!");
        }

        @Test
        @DisplayName("Expect ID in user info")
        void expectUsernameInUserInfo() {
            RegistrationConfirmationData form = prepareValidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(form).block();
            assertEquals("mikunakano@gmail.com", result.getUserInfo().username(), "User info must contain username if confirmation was success!");
        }

        private RegistrationConfirmationData prepareValidConfirmationData() {
            return RegistrationConfirmationData.of(VALID_CONFIRMATION_DATA);
        }
    }

    @Nested
    @Import(SharedReference.class)
    class ConfirmationWithInvalidInfo {
        public static final String INVALID_CONFIRMATION_DATA = "900";

        @Test
        @DisplayName("Expect not null as result")
        void expectNotNull() {
            RegistrationConfirmationData data = prepareInvalidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(data).block();
            assertNotNull(result, "Result should never be null!");
        }

        @Test
        @DisplayName("Expect confirmed flag set to true")
        void expectConfirmedFlagIsTrue() {
            RegistrationConfirmationData data = prepareInvalidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(data).block();
            assertFalse(result.isConfirmed(), "If data was invalid then confirmed flag must be set to false!");
        }

        @Test
        @DisplayName("Expect failed flag set to false")
        void expectFailedFlagIsTrue() {
            RegistrationConfirmationData data = prepareInvalidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(data).block();
            assertTrue(result.isFailed(), "If data was invalid then 'failed' flag must be set to true!");
        }

        @Test
        @DisplayName("Expect null user info")
        void expectNullUserInfo() {
            RegistrationConfirmationData form = prepareInvalidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(form).block();
            assertNull(result.getUserInfo(), "User info must be null if data is invalid!");
        }

        @Test
        @DisplayName("Expect error details to be not null")
        void expectNotNullErrorDetails() {
            RegistrationConfirmationData form = prepareInvalidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(form).block();
            assertNotNull(result.getErrorDetails(), "Error details must be presented if invalid data was used!");
        }

        @Test
        @DisplayName("Expect error details with 'invalid_confirmation_data' code")
        void expectErrorCodeToBeValid() {
            RegistrationConfirmationData form = prepareInvalidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(form).block();
            assertEquals("invalid_confirmation_data", result.getErrorDetails().getCode(), "Error code must be valid!!");
        }

        @Test
        @DisplayName("Expect error details with valid description")
        void expectErrorDescriptionToBeValid() {
            RegistrationConfirmationData form = prepareInvalidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(form).block();
            assertEquals("Data is invalid or expired", result.getErrorDetails().getDescription(), "Error description must be valid!!");
        }

        @Test
        @DisplayName("Expect error details with valid solution")
        void expectErrorSolutionToBeValid() {
            RegistrationConfirmationData form = prepareInvalidConfirmationData();
            RemoteRegistrationConfirmationResult result = remoteServiceUserRegistrar.confirmRegistration(form).block();
            assertEquals("To fix the problem - input correct confirmation data or regenerate it", result.getErrorDetails().getPossibleSolution(), "Error possible solution must be valid!!");
        }

        private RegistrationConfirmationData prepareInvalidConfirmationData() {
            return RegistrationConfirmationData.of(INVALID_CONFIRMATION_DATA);
        }
    }
}

/**
 * Used to share same spring context between @Nested classes
 */
@Component
class SharedReference {}