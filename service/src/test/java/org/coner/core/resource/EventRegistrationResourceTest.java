package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.TestConstants.EVENT_ID;
import static org.coner.core.util.TestConstants.REGISTRATION_ID;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.boundary.RegistrationApiDomainBoundary;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.service.EventRegistrationService;
import org.coner.core.domain.service.exception.EntityMismatchException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.ResourceTestRule;

public class EventRegistrationResourceTest {

    private final RegistrationApiDomainBoundary registrationBoundary = mock(RegistrationApiDomainBoundary.class);
    private final EventRegistrationService eventRegistrationService = mock(EventRegistrationService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventRegistrationResource(
                    eventRegistrationService,
                    registrationBoundary
            ))
            .addResource(new DomainServiceExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(registrationBoundary, eventRegistrationService);
    }

    @Test
    public void itShouldGetRegistration() throws Exception {
        Registration domainEntity = DomainEntityTestUtils.fullDomainRegistration();
        RegistrationApiEntity apiEntity = ApiEntityTestUtils.fullApiRegistration();

        when(eventRegistrationService.getByEventIdAndRegistrationId(EVENT_ID, REGISTRATION_ID))
                .thenReturn(domainEntity);
        when(registrationBoundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);

        Response responseContainer = resources.client()
                .target("/events/" + EVENT_ID + "/registrations/" + REGISTRATION_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(eventRegistrationService).getByEventIdAndRegistrationId(EVENT_ID, REGISTRATION_ID);
        verifyNoMoreInteractions(eventRegistrationService);

        assertThat(responseContainer).isNotNull();
        assertThat(responseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        RegistrationApiEntity responseEntity = responseContainer.readEntity(RegistrationApiEntity.class);
        assertThat(responseEntity)
                .isNotNull()
                .isEqualToComparingFieldByField(apiEntity);
    }

    @Test
    public void whenEventIdDoesNotExistItShouldRespondNotFound() throws Exception {
        when(eventRegistrationService.getByEventIdAndRegistrationId(EVENT_ID, REGISTRATION_ID))
                .thenThrow(EntityNotFoundException.class);

        Response registrationResponseContainer = resources.client()
                .target("/events/" + EVENT_ID + "/registrations/" + REGISTRATION_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(eventRegistrationService).getByEventIdAndRegistrationId(EVENT_ID, REGISTRATION_ID);
        verifyNoMoreInteractions(eventRegistrationService);

        assertThat(registrationResponseContainer.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    public void whenRegistrationIdDoesNotExistItShouldReturnNotFound() throws Exception {
        when(eventRegistrationService.getByEventIdAndRegistrationId(EVENT_ID, REGISTRATION_ID))
                .thenThrow(EntityNotFoundException.class);

        Response registrationResponseContainer = resources.client()
                .target("/events/" + EVENT_ID + "/registrations/" + REGISTRATION_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(eventRegistrationService).getByEventIdAndRegistrationId(eq(EVENT_ID), eq(REGISTRATION_ID));
        verifyNoMoreInteractions(eventRegistrationService);

        assertThat(registrationResponseContainer.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    public void whenRegistrationEventIdDoesNotMatchEventIdItShouldReturnConflict()
            throws Exception {
        when(eventRegistrationService.getByEventIdAndRegistrationId(EVENT_ID, REGISTRATION_ID))
                .thenThrow(new EntityMismatchException());

        Response registrationResponseContainer = resources.client()
                .target("/events/" + EVENT_ID + "/registrations/" + REGISTRATION_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(eventRegistrationService).getByEventIdAndRegistrationId(EVENT_ID, REGISTRATION_ID);
        verifyNoMoreInteractions(eventRegistrationService);

        assertThat(registrationResponseContainer).isNotNull();
        assertThat(registrationResponseContainer.getStatus()).isEqualTo(HttpStatus.CONFLICT_409);
    }
}
