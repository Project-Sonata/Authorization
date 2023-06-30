package com.odeyalo.sonata.authorization.service.authentication;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Subject to generate authentication to
 */
@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class Subject {
    String id;
    String username;
    String role;
    Set<GrantedAuthority> authorities;
}
