package com.odeyalo.sonata.authorization.service.registration.manager;

import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import reactor.core.publisher.Mono;

/**
 * Facade to register the user and confirm registration and return token(or other stuff) to make user authenticated
 */
public interface RegistrationManager {

    Mono<RegistrationAnswer> registerUser(RegistrationForm registrationForm);

    Mono<RegistrationConfirmationAnswer> confirmRegistration(RegistrationConfirmationData data);
}
