package org.coner.exception;

import org.coner.api.response.ErrorsResponse;

import com.google.common.base.Strings;
import java.util.Arrays;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;

public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
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
