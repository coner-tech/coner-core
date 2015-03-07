package org.coner.exception;

import com.google.common.base.Strings;
import org.coner.api.response.ErrorsResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Arrays;

/**
 * Class for mapping Web Application Exceptions and create appropriate responses.
 */
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
