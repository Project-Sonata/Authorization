package com.odeyalo.sonata.authorization.entity;

import java.util.Map;

/**
 * Entity that represents the access token
 */
public interface AccessToken extends BaseEntity {
    /**
     * Token value, never null
     * @return token value
     */
    String getTokenValue();

    /**
     * Time when the token will be expired
     * It can be equal or be lower than creation time!
     * @return - token expiration time in timestamp format.
     */
    Long getExpirationTime();

    /**
     * Id of the user that associated with this token
     * @return - user's id associated with this token
     */
    String getUserId();

    /**
     * Claims associated with this body.
     * Key - claim name, value - claim value.
     * Used to return info about token's body.
     * @return - claims associated with this token
     */
    Map<String, Object> getClaims();

}
