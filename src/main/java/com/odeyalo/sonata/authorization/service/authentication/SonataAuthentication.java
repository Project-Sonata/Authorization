package com.odeyalo.sonata.authorization.service.authentication;

import com.odeyalo.sonata.common.authentication.dto.UserInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Represent the authentication in Sonata Project.
 */
public class SonataAuthentication extends AbstractAuthenticationToken {
    private final Object principal;
    private final Object credentials;
    private final UserInfo userInfo;
    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     * @param credentials - user's credentials, in most cases simple string with password
     * @param userInfo - generic info about user
     * @param principal   - user's principal object. In most cases just a UserDetails implementation
     */
    public SonataAuthentication(Object principal, Object credentials, UserInfo userInfo, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.userInfo = userInfo;
    }

    public static SonataAuthentication of(Object principal, Object credentials, UserInfo userInfo,
                                          Collection<? extends GrantedAuthority> authorities) {
        return new SonataAuthentication(principal, credentials, userInfo, authorities);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
