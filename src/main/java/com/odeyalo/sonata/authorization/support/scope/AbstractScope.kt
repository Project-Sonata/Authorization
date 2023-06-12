package com.odeyalo.sonata.authorization.support.scope

abstract class AbstractScope(
    private val name: String,
    private val description: String,
    private val supportedRoles: Set<String>
) : Scope {

    override fun getName(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }

    override fun supportsRole(roleName: String): Boolean {
        return supportedRoles.contains(roleName)
    }

    override fun toString(): String {
        return "AbstractScope(name='$name', description='$description', supportedRoles=$supportedRoles)"
    }
}
