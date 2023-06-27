package com.odeyalo.sonata.authorization.service.registration;

import lombok.Builder;

@Builder
public record BasicUserInfo(String id, String username) {

    public static BasicUserInfo of(String id, String username) {
        return new BasicUserInfo(id, username);
    }
}
