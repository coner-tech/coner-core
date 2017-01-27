package org.coner.core.domain.service;

import java.util.List;

import org.coner.core.api.entity.DomainEntity;
import org.coner.core.domain.payload.DomainAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.Gateway;

public abstract class AbstractEntityService<
        DE extends DomainEntity,
        AP extends DomainAddPayload,
        G extends Gateway<DE, AP>> {

    private final Class<DE> domainEntityClass;
    protected final G gateway;

    protected AbstractEntityService(Class<DE> domainEntityClass, G gateway) {
        this.domainEntityClass = domainEntityClass;
        this.gateway = gateway;
    }

    public DE add(AP addPayload) throws AddEntityException {
        return gateway.add(addPayload);
    }

    public DE getById(String id) throws EntityNotFoundException {
        DE entity = gateway.findById(id);
        if (entity == null) {
            throw new EntityNotFoundException(domainEntityClass, id);
        }
        return entity;
    }

    public List<DE> getAll() {
        return gateway.getAll();
    }
}
