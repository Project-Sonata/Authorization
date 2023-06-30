package com.odeyalo.sonata.authorization.service.authentication;

import com.odeyalo.sonata.authorization.entity.Role;
import com.odeyalo.sonata.authorization.repository.storage.PersistableUser;
import com.odeyalo.sonata.authorization.service.registration.BasicUserInfo;
import com.odeyalo.sonata.common.authentication.dto.UserInfo;
import com.odeyalo.sonata.common.authentication.dto.response.AuthenticationResultResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ServerWebExchange;

import java.nio.file.attribute.AclEntry;
import java.util.Collection;

/**
 * Represent the authentication in Sonata Project.
 */
public class SonataAuthentication extends AbstractAuthenticationToken {
    private final Object principal;
    private final Object credentials;
    private final BasicUserInfo userInfo;
    private final String role;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     * @param credentials - user's credentials, in most cases simple string with password
     * @param userInfo    - generic info about user
     * @param principal   - user's principal object. In most cases just a UserDetails implementation
     */
    public SonataAuthentication(Object principal, Object credentials, BasicUserInfo userInfo, Role role,
                                Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.userInfo = userInfo;
        this.role = role.getRoleValue();
    }


    public static SonataAuthentication of(Object principal, Object credentials, BasicUserInfo userInfo,
                                          Collection<? extends GrantedAuthority> authorities, Role role) {
        return new SonataAuthentication(principal, credentials, userInfo, role, authorities);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public BasicUserInfo getUserInfo() {
        return userInfo;
    }

    public String getRole() {
        return role;
    }

    public static class Builder {
        private Object principal;
        private Object credentials;
        private BasicUserInfo userInfo;
        private Collection<? extends GrantedAuthority> authorities;
        private Role role;


        public Builder principal(Object principal) {
            this.principal = principal;
            return this;
        }

        public Builder credentials(Object credentials) {
            this.credentials = credentials;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder role(String roleValue) {
            this.role = Role.valueOf(roleValue);
            return this;
        }

        public Builder userInfo(BasicUserInfo userInfo) {
            this.userInfo = userInfo;
            return this;
        }

        public SonataAuthentication build() {
            safeNullCheck();
            return SonataAuthentication.of(principal, credentials, userInfo, authorities, role);
        }

        private void safeNullCheck() {
            if (principal == null) {
                throw new IllegalArgumentException("The argument: <principal> is null. Use Builder#principal(Object) to set the value");
            }
            if (credentials == null) {
                throw new IllegalArgumentException("The argument: <credentials> is null. Use Builder#credentials(Object) to set the value");
            }
            if (role == null) {
                throw new IllegalArgumentException("The argument: <role> is null. Use Builder#role(String) or Builder#role(Role) to set the value");
            }
            if (userInfo == null) {
                throw new IllegalArgumentException("The argument: <userInfo> is null. Use Builder#userInfo(BasicUserInfo) to set the value");
            }
        }
    }
}
