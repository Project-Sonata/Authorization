package com.odeyalo.sonata.authorization.service.registration.manager;

import com.odeyalo.sonata.authorization.service.authentication.AuthenticationPayload;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class RegistrationConfirmationAnswer {
    public static final boolean CONFIRMED = true;
    public static final boolean FAILED = false;

    boolean isConfirmed;
    // Set only if the isConfirmed is true
    AuthenticationPayload authenticationPayload;
    // Set only if the isConfirmed is false
    ErrorDetails errorDetails;

    public static RegistrationConfirmationAnswer successfullyConfirmed(AuthenticationPayload authenticationPayload){
        return of(CONFIRMED, authenticationPayload, null);
    }

    public static RegistrationConfirmationAnswer confirmationFailed(ErrorDetails errorDetails){
        return of(FAILED, null, errorDetails);
    }
}
