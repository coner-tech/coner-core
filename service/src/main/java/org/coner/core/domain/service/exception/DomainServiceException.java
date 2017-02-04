package org.coner.core.domain.service.exception;

public abstract class DomainServiceException extends Exception {

    public DomainServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainServiceException(String message) {
        super(message);
    }
}
