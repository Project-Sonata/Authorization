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
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntityImpl {
    private String username;
    private Set<GrantedAuthority> grantedAuthorities;
    private String role;

    public User(Long id, String businessKey, Long creationTime, String username, Set<GrantedAuthority> authorities, String role) {
        super(id, businessKey, creationTime);
        this.grantedAuthorities = authorities;
        this.username = username;
        this.role = role;
    }

    public static User from(User user) {
        return User.builder()
                .id(user.getId())
                .businessKey(user.getBusinessKey())
                .username(user.getUsername())
                .creationTime(user.getCreationTime())
                .grantedAuthorities(user.getGrantedAuthorities())
                .role(user.getRole())
                .build();

    }
}
