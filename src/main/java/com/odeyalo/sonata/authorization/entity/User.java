package com.odeyalo.sonata.authorization.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Represent the entity of user
 */
public interface User extends BaseEntity {
    /**
     * An unique username. In most cases email
     * @return - unique username
     */
    String getUsername();

    /**
     * Granted authorities associated with this user
     * @return - granted authorities
     */
    Set<GrantedAuthority> getGrantedAuthorities();

    /**
     * @return user's role
     */
    String getRole();
}
