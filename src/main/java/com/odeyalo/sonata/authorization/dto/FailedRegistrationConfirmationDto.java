package com.odeyalo.sonata.authorization.dto;

public class FailedRegistrationConfirmationDto extends RegistrationConfirmationDto {

    public FailedRegistrationConfirmationDto() {
        super(false);
    }

    public static FailedRegistrationConfirmationDto failed() {
        return new FailedRegistrationConfirmationDto();
    }
}
