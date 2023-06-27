package com.odeyalo.sonata.authorization.service.registration;

import com.odeyalo.sonata.common.shared.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class RegistrationResult {
    // Unique registration ID for user session. May be null
    String registrationId;
    BasicUserInfo userInfo;
    ErrorDetails errorDetails;
    Type registrationType;

    public boolean isCompleted() {
        return registrationType == Type.COMPLETED;
    }

    public boolean isPendingConfirmation() {
        return registrationType == Type.PENDING_CONFIRMATION;
    }

    public boolean isFailed() {
        return registrationType == Type.FAILED;
    }

    public static RegistrationResult completed(BasicUserInfo info) {
        return completed(null, info);
    }

    public static RegistrationResult completed(String registrationId, BasicUserInfo info) {
        return of(registrationId, info, null, Type.COMPLETED);
    }

    public static RegistrationResult pendingConfirmation() {
        return pendingConfirmation(null, null);
    }

    public static RegistrationResult pendingConfirmation(String registrationId, BasicUserInfo info) {
        return of(registrationId, info, null, Type.PENDING_CONFIRMATION);
    }

    public static RegistrationResult failed(ErrorDetails details) {
        return of(null, details, Type.FAILED);
    }

    public static RegistrationResult failed(String registrationId, ErrorDetails details) {
        return of(registrationId, null, details, Type.FAILED);
    }

    public static RegistrationResult of(BasicUserInfo info, ErrorDetails errorDetails, Type registrationType) {
        return new RegistrationResult(null, info, errorDetails, registrationType);
    }

    public enum Type {
        COMPLETED,
        PENDING_CONFIRMATION,
        FAILED
    }
}
