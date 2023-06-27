package com.odeyalo.sonata.authorization.service.registration;

import com.odeyalo.sonata.authorization.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class RegisteredUser {
    private Long id;
    private String username;
    private String role;
    @Singular
    private Set<GrantedAuthority> grantedAuthorities;

    public static RegisteredUser from(User user) {
        return of(user.getId(), user.getUsername(), user.getRole(), user.getGrantedAuthorities());
    }
}
