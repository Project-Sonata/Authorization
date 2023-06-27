package com.odeyalo.sonata.authorization.service.registration.confirmation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class RegistrationConfirmationData {
    String data;
}
