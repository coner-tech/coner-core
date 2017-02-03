package org.coner.core.domain.service.exception;

public class AddEntityException extends DomainServiceException {
    public AddEntityException(Exception e) {
        super("Failed to add entity", e);
    }
}
