package com.odeyalo.sonata.authorization.service.registration.remote;

import com.odeyalo.sonata.authorization.service.registration.BasicUserInfo;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class RemoteRegistrationConfirmationResult {
    // Flag that indicates about
    boolean confirmed;
    // Basic info about user that was registered in another service, null if confirmed is set to false
    BasicUserInfo userInfo;
    // Details about error, it is null if confirmed was success
    ErrorDetails errorDetails;

    public boolean isFailed() {
        return !isConfirmed();
    }

    public static RemoteRegistrationConfirmationResult confirmedSuccessfully(BasicUserInfo user) {
        return of(true, user, null);
    }

    public static RemoteRegistrationConfirmationResult confirmationFailed(ErrorDetails errorDetails) {
        return of(true, null, errorDetails);
    }
}
