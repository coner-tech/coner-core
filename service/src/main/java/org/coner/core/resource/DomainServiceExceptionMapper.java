package org.coner.core.resource;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.DomainServiceException;
import org.coner.core.domain.service.exception.EntityMismatchException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.eclipse.jetty.http.HttpStatus;

import com.google.common.collect.ImmutableMap;
import io.dropwizard.jersey.errors.ErrorMessage;

public class DomainServiceExceptionMapper implements ExceptionMapper<DomainServiceException> {

    private final Map<Class<? extends DomainServiceException>, Integer> status;

    @Inject
    public DomainServiceExceptionMapper() {
        status = new ImmutableMap.Builder<Class<? extends DomainServiceException>, Integer>()
                .put(EntityNotFoundException.class, HttpStatus.NOT_FOUND_404)
                .put(AddEntityException.class, HttpStatus.UNPROCESSABLE_ENTITY_422)
                .put(EntityMismatchException.class, HttpStatus.CONFLICT_409)
                .build();
    }

    @Override
    public Response toResponse(DomainServiceException exception) {
        final int status = this.status.get(exception.getClass());
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorMessage(status, exception.getMessage()))
                .build();
    }
}
