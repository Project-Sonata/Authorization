package com.odeyalo.sonata.authorization.testing.asserts;

import com.odeyalo.sonata.authorization.dto.SuccessfulRegistrationResponse;
import org.assertj.core.api.AbstractAssert;

public class SuccessfulRegistrationResponseAssert extends AbstractAssert<SuccessfulRegistrationResponseAssert, SuccessfulRegistrationResponse> {

    public SuccessfulRegistrationResponseAssert(SuccessfulRegistrationResponse successfulRegistrationResponse) {
        super(successfulRegistrationResponse, SuccessfulRegistrationResponseAssert.class);
    }

    protected SuccessfulRegistrationResponseAssert(SuccessfulRegistrationResponse successfulRegistrationResponse, Class<?> selfType) {
        super(successfulRegistrationResponse, selfType);
    }

    public static SuccessfulRegistrationResponseAssert from(SuccessfulRegistrationResponse actual) {
        return new SuccessfulRegistrationResponseAssert(actual);
    }

    public SuccessfulRegistrationResponseAssert accessTokenNotNull() {
        if (actual.getAccessToken() != null){
            failWithMessage("The access token is null!");
        }
        return this;
    }
}
