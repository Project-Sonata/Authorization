package com.odeyalo.sonata.authorization.repository;

/**
 * Type of the storage that used to provide some info about repository
 */
public enum RepositoryType {
    /**
     * Repository is used to work with Redis
     */
    REDIS,
    /**
     * Repository is used to work with SQL databases
     */
    SQL,
    /**
     * Repository is used to work with in-memory repository
     */
    IN_MEMORY
}
