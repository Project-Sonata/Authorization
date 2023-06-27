package com.odeyalo.sonata.authorization.service.registration;

import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationResult;
import reactor.core.publisher.Mono;

/**
 * {@link RegistrationProvider} child interface that adds ability to confirm the registration
 */
public interface ConfirmableRegistrationProvider extends RegistrationProvider {

    Mono<RegistrationConfirmationResult> confirmRegistration(RegistrationConfirmationData confirmationData);

}
