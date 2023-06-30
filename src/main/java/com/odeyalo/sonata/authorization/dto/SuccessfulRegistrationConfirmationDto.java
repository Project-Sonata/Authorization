package com.odeyalo.sonata.authorization.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuccessfulRegistrationConfirmationDto extends RegistrationConfirmationDto {
    @JsonProperty("tokens")
    TokensDto tokens;

    public static final String ACCESS_TOKEN_KEY = "access_token";

    public SuccessfulRegistrationConfirmationDto(TokensDto tokens) {
        super(true);
        this.tokens = tokens;
    }

    public static SuccessfulRegistrationConfirmationDto of(TokensDto dto) {
        return new SuccessfulRegistrationConfirmationDto(dto);
    }

    public void addToken(String tokenName, TokenDto tokenDto) {
        this.tokens.addToken(tokenName, tokenDto);
    }

    public boolean contains(String tokenName) {
        return tokens.contains(tokenName);
    }

    public TokenDto getToken(String tokenName) {
        return tokens.getToken(tokenName);
    }
}
