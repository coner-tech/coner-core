package org.axrunner.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 */
public class ResourceException extends WebApplicationException {
    public ResourceException(Response.Status status) {
        super(status);
    }

    public ResourceException(String message, Response.Status status) {
        super(message, status);
    }

    public ResourceException(String message, Throwable cause, Response.Status status) throws IllegalArgumentException {
        super(message, cause, status);
    }
}
