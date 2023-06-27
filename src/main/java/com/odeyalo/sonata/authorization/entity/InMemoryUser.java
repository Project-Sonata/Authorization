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
    private String username;
    private Set<GrantedAuthority> authorities;
    private String role;

    public InMemoryUser(Long id, String businessKey, Long creationTime, String username, Set<GrantedAuthority> authorities, String role) {
        super(id, businessKey, creationTime);
        this.authorities = authorities;
        this.username = username;
        this.role = role;
    }

    public static InMemoryUser from(User user) {
        return InMemoryUser.builder()
                .id(user.getId())
                .businessKey(user.getBusinessKey())
                .username(user.getUsername())
                .creationTime(user.getCreationTime())
                .authorities(user.getGrantedAuthorities())
                .role(user.getRole())
                .build();

    }

    @Override
    public String getUsername() {
        return username;
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
