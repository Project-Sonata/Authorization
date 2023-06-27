package com.odeyalo.sonata.authorization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationResultResponseDto {
    @JsonProperty("registration_id")
    String registrationId;
    @JsonProperty("error")
    ErrorDetails errorDetails;
    @JsonProperty("result_type")
    RegistrationResult.Type registrationType;
}
