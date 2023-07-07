package com.odeyalo.sonata.authorization.support.scope

abstract class AbstractScope(
    private val name: String,
    private val description: String,
    supportedRoles: Set<String>,
    private val type: Scope.Type
) : Scope {

    private val supportedRoles: Set<String>

    init {
        this.supportedRoles = supportedRoles.map { sc -> sc.lowercase() }.toSet()
    }

    override fun getName(): String {
        return name
    }

    override fun getDescription(): String {
        return description
    }


    override fun supportsRole(roleName: String): Boolean {
        return supportedRoles.contains(roleName.lowercase())
    }

    override fun getScopeType(): Scope.Type {
        return type;
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

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + supportedRoles.hashCode()
        return result
    }
}
