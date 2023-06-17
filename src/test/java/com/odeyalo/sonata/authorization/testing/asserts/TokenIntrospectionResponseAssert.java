package com.odeyalo.sonata.authorization.testing.asserts;

import com.odeyalo.sonata.authorization.dto.TokenIntrospectionResponse;
import com.odeyalo.sonata.authorization.support.scope.Scope;
import org.assertj.core.api.AbstractAssert;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TokenIntrospectionResponseAssert extends AbstractAssert<TokenIntrospectionResponseAssert, TokenIntrospectionResponse> {

    private static final String SPACE = " ";

    public TokenIntrospectionResponseAssert(TokenIntrospectionResponse response) {
        super(response, TokenIntrospectionResponseAssert.class);
    }

    public static TokenIntrospectionResponseAssert from(TokenIntrospectionResponse response) {
        return new TokenIntrospectionResponseAssert(response);
    }

    public TokenIntrospectionResponseAssert isValid() {
        if (!actual.isValid()) {
            failWithMessage("Expected token introspection response to be valid, but it is invalid");
        }
        return this;
    }

    public TokenIntrospectionResponseAssert isInvalid() {
        if (actual.isValid()) {
            failWithMessage("Expected token introspection response to be invalid, but it is valid");
        }
        return this;
    }

    public TokenIntrospectionResponseAssert hasUserId(String expectedUserId) {
        if (!expectedUserId.equals(actual.getUserId())) {
            failWithMessage("Expected user ID to be <%s> but was <%s>", expectedUserId, actual.getUserId());
        }
        return this;
    }

    public TokenIntrospectionResponseAssert hasScope(String expectedScope) {
        Set<String> scopes = Arrays.stream(actual.getScope().split(SPACE)).collect(Collectors.toSet());
        if (!scopes.contains(expectedScope)) {
            failWithMessage("Expected scope to be <%s> but was <%s>", expectedScope, actual.getScope());
        }
        return this;
    }

    public TokenIntrospectionResponseAssert hasIssuedAt(long expectedIssuedAt) {
        if (expectedIssuedAt != actual.getIssuedAt()) {
            failWithMessage("Expected issued at timestamp to be <%s> but was <%s>", expectedIssuedAt, actual.getIssuedAt());
        }
        return this;
    }

    public TokenIntrospectionResponseAssert hasExpiresIn(long expectedExpiresIn) {
        if (expectedExpiresIn != actual.getExpiresIn()) {
            failWithMessage("Expected expires in value to be <%s> but was <%s>", expectedExpiresIn, actual.getExpiresIn());
        }
        return this;
    }

    public TokenIntrospectionResponseAssert scopesMatches(Scope... scopes) {
        String[] parsedScopes = Arrays.stream(scopes)
                .map(Scope::getName)
                .toArray(String[]::new);

        return scopesMatches(parsedScopes);
    }

    public TokenIntrospectionResponseAssert scopesMatches(String... scopes) {
        return doScopesMatches(scopes);
    }

    private  TokenIntrospectionResponseAssert doScopesMatches(String[] scopes) {
        Set<String> scopeNamesSet = Arrays.stream(scopes).collect(Collectors.toSet());
        String[] parsedActualScopes = actual.getScope().split(SPACE);

        for (String actualScope : parsedActualScopes) {
            if (!scopeNamesSet.contains(actualScope)) {
                failWithMessage("Token does not contain following scope: <%s>", actualScope);
            }
        }
        return this;
    }
}

