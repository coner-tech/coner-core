package org.coner.core.resource;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.coner.core.domain.service.exception.DomainServiceException;
import org.eclipse.jetty.http.HttpStatus;


import io.dropwizard.jersey.errors.ErrorMessage;

public class RuntimeExceptionUnwrappingMapper implements ExceptionMapper<RuntimeException> {

    private final DomainServiceExceptionMapper domainServiceExceptionMapper;

    @Inject
    public RuntimeExceptionUnwrappingMapper(DomainServiceExceptionMapper domainServiceExceptionMapper) {
        this.domainServiceExceptionMapper = domainServiceExceptionMapper;
    }

    @Override
    public Response toResponse(RuntimeException exception) {
        Throwable cause = exception.getCause();
        if (cause instanceof DomainServiceException) {
            return domainServiceExceptionMapper.toResponse((DomainServiceException) cause);
        }
        int status = HttpStatus.INTERNAL_SERVER_ERROR_500;
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorMessage(status, exception.getMessage()))
                .build();
    }
}
