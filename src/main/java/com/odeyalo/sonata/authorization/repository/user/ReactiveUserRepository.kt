package com.odeyalo.sonata.authorization.repository.user

import com.odeyalo.sonata.authorization.entity.User
import com.odeyalo.sonata.authorization.repository.RepositoryType

/**
 * Repository to work with User entity
 */
interface ReactiveUserRepository<T : User> : PersistentUserOperations<T> {

    fun getRepositoryType(): RepositoryType

}