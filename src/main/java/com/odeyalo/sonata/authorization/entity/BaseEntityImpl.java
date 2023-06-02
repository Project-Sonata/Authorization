package com.odeyalo.sonata.authorization.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class BaseEntityImpl implements BaseEntity {
    protected Long id;
    protected String businessKey;
    protected Long creationTime;

    /**
     * Create a new BaseEntityImpl and auto-generate business key and creation time
     */
    public BaseEntityImpl() {
        this.businessKey = UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getBusinessKey() {
        return businessKey;
    }

    @Override
    public Long getCreationTime() {
        return creationTime;
    }
}
