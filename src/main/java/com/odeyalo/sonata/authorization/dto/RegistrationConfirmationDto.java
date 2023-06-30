package com.odeyalo.sonata.authorization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public class RegistrationConfirmationDto {
    @JsonProperty("confirmed")
    boolean confirmed;

    public static RegistrationConfirmationDto of(boolean flag) {
        return new RegistrationConfirmationDto(flag);
    }
}
