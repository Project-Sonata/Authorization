package com.odeyalo.sonata.authorization.repository.storage

import com.odeyalo.sonata.authorization.entity.Role
import com.odeyalo.sonata.authorization.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Simple wrapper for User that used to make CRUD operation in [UserStorage] without depending on repository-specific [User] implementation
 */
class PersistableUser : User {
    private val id: Long?
    private val businessKey: String?
    private val creationTime: Long?
    private val username: String?
    private val authorities: MutableSet<GrantedAuthority>?
    private val role: String?

    constructor(
        id: Long?,
        businessKey: String?,
        creationTime: Long?,
        username: String?,
        authorities: MutableSet<GrantedAuthority>?,
        role: String?
    ) {
        this.id = id
        this.businessKey = businessKey
        this.creationTime = creationTime
        this.username = username
        this.authorities = authorities
        this.role = role
    }

    /**
     * If using this constructor, then ID and businessKey will be generated automatically
     */
    constructor(
        username: String,
        authorities: MutableSet<GrantedAuthority>,
        creationTime: Long,
        role: String
    ) : this(null, null, creationTime, username, authorities, role)

    companion object PersistentUserCreator {
        @JvmStatic
        fun of(
            id: Long,
            username: String,
            authorities: MutableSet<GrantedAuthority>,
            role: String
        ): PersistableUser {
            return PersistableUser(id, null, System.currentTimeMillis(), username, authorities, role)
        }

        @JvmStatic
        fun of(
            id: Long,
            businessKey: String,
            creationTime: Long,
            username: String,
            authorities: MutableSet<GrantedAuthority>,
            role: String
        ): PersistableUser {
            return PersistableUser(id, businessKey, creationTime, username, authorities, role)
        }

        @JvmStatic
        fun of(
            username: String,
            authorities: MutableSet<GrantedAuthority>,
            role: String
        ): PersistableUser {
            return PersistableUser(username, authorities, System.currentTimeMillis(), role)
        }

        @JvmStatic
        fun from(parent: User): PersistableUser {
            return PersistableUser(parent.id, parent.businessKey, parent.creationTime, parent.username, parent.grantedAuthorities, parent.role)
        }

        @JvmStatic
        fun builder(): Builder {
            return Builder();
        }
    }

    override fun getId(): Long? {
        return id
    }

    override fun getBusinessKey(): String? {
        return businessKey
    }

    override fun getCreationTime(): Long? {
        return creationTime
    }

    override fun getUsername(): String? {
        return username
    }

    override fun getGrantedAuthorities(): MutableSet<GrantedAuthority>? {
        return authorities
    }

    override fun getRole(): String? {
        return role
    }

    class Builder() {
        private var id: Long? = null
        private var businessKey: String? = null
        private var username: String? = null
        private var role: String? = null
        private var creationTime: Long = System.currentTimeMillis()
        private var authorities: MutableSet<GrantedAuthority> = mutableSetOf()

        fun id(id: Long?): Builder {
            this.id = id
            return this
        }

        fun businessKey(businessKey: String?): Builder {
            this.businessKey = businessKey
            return this
        }

        fun role(role: String): Builder {
            this.role = role
            return this
        }

        fun role(role: Role): Builder {
            this.role = role.name
            return this
        }

        fun username(username: String): Builder {
            this.username = username
            return this
        }

        fun creationTime(creationTime: Long): Builder {
            this.creationTime = creationTime
            return this
        }

        fun authority(authority: String): Builder {
            authorities.add(SimpleGrantedAuthority(authority))
            return this
        }

        fun authority(authority: GrantedAuthority): Builder {
            authorities.add(authority)
            return this
        }

        fun authorities(authorities: MutableSet<GrantedAuthority>): Builder {
            this.authorities = authorities
            return this
        }

        fun build(): PersistableUser {
            return PersistableUser(id, businessKey, creationTime, username, authorities, role)
        }
    }
}
