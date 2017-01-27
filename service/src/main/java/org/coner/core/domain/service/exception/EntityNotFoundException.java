package org.coner.core.domain.service.exception;

import org.coner.core.api.entity.DomainEntity;

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(Class<? extends DomainEntity> domainEntityClass, String id) {
        super(
                new StringBuilder()
                        .append(domainEntityClass.getSimpleName())
                        .append(" entity not found with id ")
                        .append(id)
                        .toString()
        );
    }
}
