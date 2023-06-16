package com.odeyalo.sonata.authorization.repository.storage

import com.odeyalo.sonata.authorization.entity.AccessToken
import com.odeyalo.sonata.authorization.repository.PersistentAccessTokenOperations

/**
 * More common interface that will be used as additional layer for PersistentAccessTokenOperations.
 *
 * @see DelegatingAccessTokenStorage
 */
interface AccessTokenStorage : PersistentAccessTokenOperations<AccessToken>