package com.odeyalo.sonata.authorization.testing;

import com.odeyalo.sonata.authorization.dto.GeneratedInternalAccessTokenResponseDto;
import org.assertj.core.api.AbstractAssert;

public class GeneratedInternalAccessTokenResponseDtoAssert extends AbstractAssert<GeneratedInternalAccessTokenResponseDtoAssert, GeneratedInternalAccessTokenResponseDto> {

    protected GeneratedInternalAccessTokenResponseDtoAssert(GeneratedInternalAccessTokenResponseDto generatedInternalAccessTokenResponseDto) {
        super(generatedInternalAccessTokenResponseDto, GeneratedInternalAccessTokenResponseDtoAssert.class);
    }

    public static GeneratedInternalAccessTokenResponseDtoAssert assertThat(GeneratedInternalAccessTokenResponseDto actual) {
        return new GeneratedInternalAccessTokenResponseDtoAssert(actual);
    }

    public TokenAssert accessToken() {
        return new TokenAssert(actual.getAccessToken(), this);
    }


    public static class TokenAssert extends AbstractAssert<TokenAssert, String> {
        private final GeneratedInternalAccessTokenResponseDtoAssert and;

        protected TokenAssert(String actual, GeneratedInternalAccessTokenResponseDtoAssert and) {
            super(actual, TokenAssert.class);
            this.and = and;
        }

        public GeneratedInternalAccessTokenResponseDtoAssert and() {
            return and;
        }
    }
}
