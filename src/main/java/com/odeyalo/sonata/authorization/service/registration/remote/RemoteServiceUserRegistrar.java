package com.odeyalo.sonata.authorization.service.registration.remote;

import com.odeyalo.sonata.authorization.service.registration.BasicUserInfo;
import com.odeyalo.sonata.authorization.service.registration.RegistrationForm;
import com.odeyalo.sonata.authorization.service.registration.RegistrationResult;
import com.odeyalo.sonata.authorization.service.registration.confirmation.RegistrationConfirmationData;
import com.odeyalo.sonata.common.shared.ErrorDetails;
import reactor.core.publisher.Mono;

/**
 * Interface to register the user in other microservice, in other words, delegate registration process to another microservice and just wrap the result
 */
public interface RemoteServiceUserRegistrar {
    /**
     * Register the user in remote service
     *
     * @param registrationForm - form provided by user
     * @return - wrapped result of the registration in Mono
     */
    Mono<RegistrationResult> registerUser(RegistrationForm registrationForm);

    /**
     * Confirms the registration process and register the user if data is valid
     * This method can throw {@link UnsupportedOperationException} if the registration provider does not support confirmation
     * @param data - data that contains value to confirm registration
     * @return - {@link RemoteRegistrationConfirmationResult#confirmedSuccessfully(BasicUserInfo)} if data is valid and user has been registered
     * or {@link RemoteRegistrationConfirmationResult#confirmationFailed(ErrorDetails)} if data is invalid
     */
    Mono<RemoteRegistrationConfirmationResult> confirmRegistration(RegistrationConfirmationData data);
}