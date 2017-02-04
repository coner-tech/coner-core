package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityMismatchException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.util.TestConstants;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class DomainServiceExceptionMapperTest {

    private DomainServiceExceptionMapper mapper;

    @Before
    public void setup() {
        mapper = new DomainServiceExceptionMapper();
    }

    @Test
    public void itShouldMapEntityNotFoundException() {
        EntityNotFoundException entityNotFoundException = new EntityNotFoundException(
                Event.class,
                TestConstants.EVENT_ID
        );

        Response actual = mapper.toResponse(entityNotFoundException);

        assertThat(actual.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    public void itShouldMapAddEntityException() {
        AddEntityException addEntityException = new AddEntityException(new EntityNotFoundException(
                Event.class,
                TestConstants.EVENT_ID
        ));

        Response actual = mapper.toResponse(addEntityException);

        assertThat(actual.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    @Test
    public void itShouldMapEntityMismatchException() {
        EntityMismatchException entityMismatchException = new EntityMismatchException();

        Response actual = mapper.toResponse(entityMismatchException);

        assertThat(actual.getStatus()).isEqualTo(HttpStatus.CONFLICT_409);
    }

}
