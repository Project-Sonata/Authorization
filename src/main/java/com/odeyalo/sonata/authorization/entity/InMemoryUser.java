package com.odeyalo.sonata.authorization.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Represent the user that can be stored in memory.
 */
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InMemoryUser extends BaseEntityImpl implements User {
    private Set<GrantedAuthority> authorities;
    private String role;

    public InMemoryUser(Long id, String businessKey, Long creationTime, Set<GrantedAuthority> authorities, String role) {
        super(id, businessKey, creationTime);
        this.authorities = authorities;
        this.role = role;
    }

    @Override
    public Set<GrantedAuthority> getGrantedAuthorities() {
        return authorities;
    }

    @Override
    public String getRole() {
        return role;
    }
}
