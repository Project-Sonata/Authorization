package com.odeyalo.sonata.authorization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RegistrationFormDto {
    String username;
    String password;
    String gender;
    LocalDate birthdate;
    @JsonProperty("enable_notification")
    boolean enableNotification;
}
