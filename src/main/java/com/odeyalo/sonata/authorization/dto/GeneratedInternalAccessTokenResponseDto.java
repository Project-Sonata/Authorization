package com.odeyalo.sonata.authorization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Dto to return when internal(generated for Sonata services only to request date as user)
 * access token has been created
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneratedInternalAccessTokenResponseDto {
    @JsonProperty("access_token")
    String accessToken;
}
