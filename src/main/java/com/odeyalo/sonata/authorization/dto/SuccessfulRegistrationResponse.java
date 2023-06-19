package com.odeyalo.sonata.authorization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represent response that can be sent after successfully registration
 */
@Data
@Builder
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(staticName = "empty")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuccessfulRegistrationResponse {
    @JsonProperty("access_token")
    String accessToken;
}
