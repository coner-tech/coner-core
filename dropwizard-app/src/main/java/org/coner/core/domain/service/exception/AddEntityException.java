package org.coner.core.domain.service.exception;

public class AddEntityException extends Exception {
    public AddEntityException(Exception e) {
        super("Failed to add entity", e);
    }
}
