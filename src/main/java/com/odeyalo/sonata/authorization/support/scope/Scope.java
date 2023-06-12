package com.odeyalo.sonata.authorization.support.scope;

/**
 * Represent the scope in the Sonata project
 */
public interface Scope {
    /**
     * @return name of the role
     */
    String getName();

    /**
     * @return description of the role
     */
    String getDescription();

    /**
     * @param roleName - name of the role to check
     * @return - true if the scope supported by this role, false otherwise
     */
    boolean supportsRole(String roleName);
}
