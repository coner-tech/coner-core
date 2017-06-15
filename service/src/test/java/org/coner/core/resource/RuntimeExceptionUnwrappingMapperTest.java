package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.coner.core.domain.service.exception.DomainServiceException;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.dropwizard.jersey.errors.ErrorMessage;

@RunWith(MockitoJUnitRunner.class)
public class RuntimeExceptionUnwrappingMapperTest {

    @InjectMocks
    RuntimeExceptionUnwrappingMapper mapper;

    @Mock
    DomainServiceExceptionMapper domainServiceExceptionMapper;

    @Test
    public void itShouldMapRuntimeExceptionCausedByDomainServiceException() {
        DomainServiceException domainServiceException = mock(DomainServiceException.class);
        RuntimeException runtimeException = new RuntimeException(domainServiceException);
        Response expected = mock(Response.class);
        when(domainServiceExceptionMapper.toResponse(domainServiceException)).thenReturn(expected);

        Response actual = mapper.toResponse(runtimeException);

        verify(domainServiceExceptionMapper).toResponse(domainServiceException);
        assertThat(actual).isSameAs(expected);
    }

    @Test
    public void itShouldMapUnknownRuntimeException() {
        String message = "message";
        RuntimeException runtimeException = new RuntimeException(message);

        Response actual = mapper.toResponse(runtimeException);

        assertThat(actual.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR_500);
        assertThat((ErrorMessage) actual.getEntity())
                .extracting(ErrorMessage::getCode, ErrorMessage::getMessage, ErrorMessage::getDetails)
                .containsExactly(HttpStatus.INTERNAL_SERVER_ERROR_500, message, null);
    }

}
