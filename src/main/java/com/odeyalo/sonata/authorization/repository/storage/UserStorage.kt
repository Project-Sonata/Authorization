package com.odeyalo.sonata.authorization.repository.storage

import com.odeyalo.sonata.authorization.repository.user.PersistentUserOperations

/**
 * Higher layer abstraction between service and repository layers
 */
interface UserStorage : PersistentUserOperations<PersistableUser>