package com.odeyalo.sonata.authorization.testing.asserts;

import com.odeyalo.sonata.authorization.entity.AccessToken;
import org.assertj.core.api.AbstractAssert;

import java.util.Map;

public class AccessTokenAssert extends AbstractAssert<AccessTokenAssert, AccessToken> {

    public AccessTokenAssert(AccessToken accessToken) {
        this(accessToken, AccessTokenAssert.class);
    }

    protected AccessTokenAssert(AccessToken accessToken, Class<?> selfType) {
        super(accessToken, selfType);
    }

    public static AccessTokenAssert from(AccessToken token) {
        return new AccessTokenAssert(token);
    }

    public AccessTokenAssert everythingIsNull() {
        return state(actual.getTokenValue() == null, "Token value is not null!")
                .state(actual.getId() == null, "Token id is not null!")
                .state(actual.getUserId() == null, "User id is not null!")
                .state(actual.getCreationTime() == null, "Token creation time must be null!")
                .state(actual.getExpirationTime() == null, "Token expiration time must be null!")
                .state(actual.getBusinessKey() == null, "Token business key must be null!")
                .state(actual.getClaims() == null || actual.getClaims().isEmpty(), "Token claims must be null or at least empty!");
    }

    public AccessTokenAssert everythingIsNotNull() {
        return state(actual.getTokenValue() != null, "Token value is null!")
                .state(actual.getId() != null, "Token id is null!")
                .state(actual.getUserId() != null, "User id is null!")
                .state(actual.getCreationTime() != null, "Token creation time must be not null!")
                .state(actual.getExpirationTime() != null, "Token expiration time must be not null!")
                .state(actual.getBusinessKey() != null, "Token business key must be not null!")
                .state(actual.getClaims() != null, "Token claims must be not null!");
    }

    public AccessTokenAssert id(String expected) {
        return id(Long.valueOf(expected));
    }

    public AccessTokenAssert id(Long expected) {
        return state(expected.equals(actual.getId()), "The ids are not equal! Expected: %s\n Actual: %s", expected, actual.getId());
    }

    public AccessTokenAssert tokenValue(String expected) {
        String tokenValue = actual.getTokenValue();
        return state(expected.equals(tokenValue), "The tokens have different values!. Expected: %s,\n actual%s", expected, tokenValue);
    }

    public AccessTokenAssert tokenValueLength(long expected) {
        int actualLength = actual.getTokenValue().length();
        return state(actualLength == expected, "Length mismatch! Expected: %s,\n actual: %s", expected, actualLength);
    }

    public AccessTokenAssert userId(String expected) {
        return state(expected.equals(actual.getUserId()), "The tokens have different user id!. Expected: %s,\n actual%s", expected, actual.getUserId());
    }

    public AccessTokenAssert userId(Long expected) {
        return userId(String.valueOf(expected));
    }

    public AccessTokenAssert expirationTime(long expected) {
        return state(expected == actual.getExpirationTime(), "Time isn't equal! Expected: %s,\n actual: %s", expected, actual.getExpirationTime());
    }

    public AccessTokenAssert creationTime(long expected) {
        return state(expected == actual.getCreationTime(), "Time isn't equal! Expected: %s,\n actual: %s", expected, actual.getCreationTime());
    }

    public AccessTokenAssert claims(Map<String, Object> expectedClaims) {
        Map<String, Object> actualClaims = actual.getClaims();
        return state(expectedClaims.equals(actualClaims), "Claims are not equal! Expected: %s,\n Actual: %s", expectedClaims, actualClaims);
    }

    public AccessTokenAssert containsClaim(String claimName) {
        Map<String, Object> claims = actual.getClaims();
        return state(claims.containsKey(claimName), "Claim with name: [%s] does not exist in token claims!", claimName);
    }

    public AccessTokenAssert claimEqual(String claimName, Object expectedValue) {
        Object claimValue = actual.getClaims().get(claimName);
        return state(claimValue != null, "The claim does not have key: %s", claimName)
                .state(expectedValue.equals(claimValue), "The claim values are not equal! Expected: %s,\n Actual: %s", expectedValue, claimValue);
    }

    public AccessTokenAssert state(boolean condition, String failMessage, Object... arguments) {
        if (condition) {
            return this;
        }
        failWithMessage(failMessage, arguments);
        return this;
    }
}
