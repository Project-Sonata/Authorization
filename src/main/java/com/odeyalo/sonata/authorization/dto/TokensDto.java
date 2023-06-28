package com.odeyalo.sonata.authorization.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokensDto {
    @Singular
    Map<String, TokenDto> tokens;

    public void addToken(String tokenName, TokenDto tokenDto) {
        if (tokens == null) {
            tokens = new LinkedHashMap<>();
        }
        this.tokens.put(tokenName, tokenDto);
    }

    public boolean contains(String tokenName) {
        return tokens.containsKey(tokenName);
    }

    public TokenDto getToken(String tokenName) {
        return tokens.get(tokenName);
    }
}
