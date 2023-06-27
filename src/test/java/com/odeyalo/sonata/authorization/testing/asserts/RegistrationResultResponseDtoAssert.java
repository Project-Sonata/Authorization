package com.odeyalo.sonata.authorization.testing.asserts;

import com.odeyalo.sonata.authorization.dto.RegistrationResultResponseDto;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import org.assertj.core.api.AbstractAssert;

/**
 * Asserts for {@link  RegistrationResultResponseDto}
 */
public class RegistrationResultResponseDtoAssert extends AbstractAssert<RegistrationResultResponseDtoAssert, RegistrationResultResponseDto> {

    public RegistrationResultResponseDtoAssert(RegistrationResultResponseDto registrationResultResponseDto) {
        super(registrationResultResponseDto, RegistrationResultResponseDtoAssert.class);
    }

    protected RegistrationResultResponseDtoAssert(RegistrationResultResponseDto registrationResultResponseDto, Class<?> selfType) {
        super(registrationResultResponseDto, selfType);
    }

    public static RegistrationResultResponseDtoAssert from(RegistrationResultResponseDto actual) {
        return new RegistrationResultResponseDtoAssert(actual);
    }

    public RegistrationResultResponseDtoAssert nullRegistrationId() {
        if (actual.getRegistrationId() != null) {
            failWithMessage("Registration id must be null!");
        }
        return this;
    }

    public RegistrationResultResponseDtoAssert notNullRegistrationId() {
        if (actual.getRegistrationId() == null) {
            failWithMessage("Registration id must not be null!");
        }
        return this;
    }

    public ErrorDetailsAssert errorDetails() {
        return new ErrorDetailsAssert(actual.getErrorDetails());
    }

    public RegistrationResultResponseDtoAssert resultTypeEqualTo(RegistrationResult.Type expectedType) {
        if (actual.getRegistrationType() != expectedType) {
            failWithMessage("The types are not equal! Expected: <%s>, actual: <%s>", actual.getRegistrationType(), expectedType);
        }
        return this;
    }

    public RegistrationResultResponseDtoAssert completed() {
        return resultTypeEqualTo(RegistrationResult.Type.COMPLETED);
    }

    public RegistrationResultResponseDtoAssert pendingConfirmation() {
        return resultTypeEqualTo(RegistrationResult.Type.PENDING_CONFIRMATION);
    }

    public RegistrationResultResponseDtoAssert failed() {
        return resultTypeEqualTo(RegistrationResult.Type.FAILED);
    }
}
