package com.odeyalo.sonata.authorization.entity;

public interface BaseEntity {
    /**
     * Id of the entity
     * @return id of the entity
     */
    Long getId();

    /**
     * An unique identifier that generates by code and does not depend on database
     * @return - unique string
     */
    String getBusinessKey();

    /**
     * Time when the entity was created
     * @return time when the entity was created in timestamp format
     */
    Long getCreationTime();
}
