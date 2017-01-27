package org.coner.core.exception;

import java.util.Arrays;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.coner.core.api.response.ErrorsResponse;

import com.google.common.base.Strings;

public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Inject
    public WebApplicationExceptionMapper() {
    }

    @Override
    public Response toResponse(WebApplicationException e) {
        Response.StatusType status = e.getResponse().getStatusInfo();

        ErrorsResponse entity = new ErrorsResponse();

        if (Strings.isNullOrEmpty(e.getMessage())) {
            entity.setErrors(Arrays.asList(status.getReasonPhrase()));
        } else {
            entity.setErrors(Arrays.asList(e.getMessage()));
        }

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(entity)
                .build();
    }
}
