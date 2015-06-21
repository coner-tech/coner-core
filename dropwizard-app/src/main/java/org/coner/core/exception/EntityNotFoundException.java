package org.coner.core.exception;

import org.coner.core.domain.entity.DomainEntity;

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
