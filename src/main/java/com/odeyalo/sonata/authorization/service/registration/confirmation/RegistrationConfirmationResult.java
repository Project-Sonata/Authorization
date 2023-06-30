package com.odeyalo.sonata.authorization.service.registration.confirmation;

import com.odeyalo.sonata.authorization.service.registration.RegisteredUser;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class RegistrationConfirmationResult {
    // Flag that indicates about
    boolean confirmed;
    // User that was registered, null if confirmed is set to false
    RegisteredUser registeredUser;
    // Details about error, it is null if confirmed was success
    ErrorDetails errorDetails;

    public static RegistrationConfirmationResult confirmedSuccessfully(RegisteredUser user) {
        return of(true, user, null);
    }

    public static RegistrationConfirmationResult confirmationFailed(ErrorDetails errorDetails) {
        return of(false, null, errorDetails);
    }

    public boolean isFailed() {
        return !isConfirmed();
    }
}