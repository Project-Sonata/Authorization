package com.odeyalo.sonata.authorization.service.registration.manager;

import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static com.odeyalo.sonata.authorization.service.registration.RegistrationResult.Type.*;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class RegistrationAnswer {
    RegistrationResult.Type registrationStatus;
    // Null if registrationStatus is PENDING_CONFIRMATION or FAILED
    AuthenticationPayload authenticationPayload;
    // Null if registrationStatus isn't FAILED
    ErrorDetails errorDetails;

    public static RegistrationAnswer completed(AuthenticationPayload payload) {
        return of(COMPLETED, payload, null);
    }

    public static RegistrationAnswer pendingConfirmation() {
        return of(PENDING_CONFIRMATION, null, null);
    }

    public static RegistrationAnswer failed(ErrorDetails errorDetails) {
        return of(FAILED, null, errorDetails);
    }
}

