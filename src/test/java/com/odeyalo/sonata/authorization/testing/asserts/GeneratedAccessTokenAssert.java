package com.odeyalo.sonata.authorization.testing.asserts;

import com.odeyalo.sonata.authorization.service.token.access.GeneratedAccessToken;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.MapAssert;

import java.util.Map;

public class GeneratedAccessTokenAssert extends AbstractAssert<GeneratedAccessTokenAssert, GeneratedAccessToken> {

    public GeneratedAccessTokenAssert(GeneratedAccessToken actual) {
        super(actual, GeneratedAccessTokenAssert.class);
    }

    // todo: Create asserts and finish tests
    protected GeneratedAccessTokenAssert(GeneratedAccessToken generatedAccessToken, Class<?> selfType) {
        super(generatedAccessToken, selfType);
    }

    public static GeneratedAccessTokenAssert create(GeneratedAccessToken token) {
        return new GeneratedAccessTokenAssert(token);
    }

    public GeneratedAccessTokenAssert notNull() {
        if (actual == null) {
            failWithMessage("Token is null!");
        }
        return this;
    }

    public GeneratedAccessTokenAssert hasTokenSize(int requiredSize) {
        boolean matches = actual.getTokenValue().length() == requiredSize;
        if (!matches) {
            failWithMessage("Token value does not match the size condition. Required: %s, actual: %s", requiredSize, actual.getTokenValue().length());
        }
        return this;
    }

    public GeneratedAccessTokenAssert userId(String requiredUserId) {
        String actualUserId = actual.getUserId();
        if (requiredUserId != null && !requiredUserId.equals(actualUserId)) {
            failWithMessage("User id mismatch! Required: %s, actual: %s", requiredUserId, actualUserId);
        }
        return this;
    }

    public GeneratedAccessTokenAssert tokenBody(Map<String, Object> requiredBody) {
        MapAssert.assertThatMap(requiredBody).isEqualTo(actual.getClaims());
        return this;
    }

    public GeneratedAccessTokenAssert hasLifetimeMs(long requiredLifetimeMs) {
        long actualLifetimeMs = actual.getExpiresInMs() - actual.getCreationTimeMs();
        if (requiredLifetimeMs != actualLifetimeMs) {
            failWithMessage("Token lifetime mismatch! Required: %s, actual: %s", requiredLifetimeMs, actualLifetimeMs);
        }
        return this;
    }
}
