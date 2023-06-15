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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractScope

        if (name != other.name) return false
        if (description != other.description) return false
        if (supportedRoles != other.supportedRoles) return false
        return true
    }
}
