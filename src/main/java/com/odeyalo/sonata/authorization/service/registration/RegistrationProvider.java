package com.odeyalo.sonata.authorization.service.registration;

import reactor.core.publisher.Mono;

/**
 * Base interface that handles user's registration process
 */
public interface RegistrationProvider {

    Mono<RegistrationResult> registerUser(RegistrationForm form);

}
