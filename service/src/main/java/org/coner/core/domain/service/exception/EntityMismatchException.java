package org.coner.core.domain.service.exception;

/**
 * Indicates an entity does not belong to the entity it says it does.
 */
public class EntityMismatchException extends DomainServiceException {

    public EntityMismatchException() {
        super("The entity relationship is invalid");
    }
}
