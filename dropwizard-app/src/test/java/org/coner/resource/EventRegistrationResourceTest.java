package org.coner.resource;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.Registration;
import org.coner.core.exception.EventRegistrationMismatchException;
import org.coner.util.*;

import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class EventRegistrationResourceTest {

    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final RegistrationBoundary registrationBoundary = mock(RegistrationBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventRegistrationResource(eventBoundary, registrationBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(eventBoundary, registrationBoundary, conerCoreService);
    }

    @Test
    public void itShouldGetRegistration() throws EventRegistrationMismatchException {
        org.coner.core.domain.Event domainEvent = DomainEntityTestUtils.fullDomainEvent();
        Registration domainRegistration = DomainEntityTestUtils.fullDomainRegistration();
        RegistrationApiEntity apiRegistration = ApiEntityTestUtils.fullApiRegistration();

        // sanity check test
        assertThat(domainEvent.getId()).isSameAs(TestConstants.EVENT_ID);
        assertThat(domainRegistration.getId()).isSameAs(TestConstants.REGISTRATION_ID);
        assertThat(apiRegistration.getId()).isSameAs(TestConstants.REGISTRATION_ID);

        when(conerCoreService.getRegistration(TestConstants.EVENT_ID, TestConstants.REGISTRATION_ID))
                .thenReturn(domainRegistration);
        when(registrationBoundary.toApiEntity(domainRegistration)).thenReturn(apiRegistration);

        Response responseContainer = resources.client()
                .target("/events/" + TestConstants.EVENT_ID + "/registrations/" + TestConstants.REGISTRATION_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getRegistration(TestConstants.EVENT_ID, TestConstants.REGISTRATION_ID);
        verify(registrationBoundary).toApiEntity(domainRegistration);
        verifyNoMoreInteractions(conerCoreService, registrationBoundary);
        verifyZeroInteractions(eventBoundary);

        assertThat(responseContainer).isNotNull();
        assertThat(responseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        RegistrationApiEntity responseEntity = responseContainer.readEntity(RegistrationApiEntity.class);
        assertThat(responseEntity)
                .isNotNull()
                .isEqualToComparingFieldByField(apiRegistration);
    }

    @Test
    public void whenEventIdDoesNotExistItShouldReturnNotFound() throws EventRegistrationMismatchException {
        when(conerCoreService.getRegistration(TestConstants.EVENT_ID, TestConstants.REGISTRATION_ID)).thenReturn(null);

        Response registrationResponseContainer = resources.client()
                .target("/events/" + TestConstants.EVENT_ID + "/registrations/" + TestConstants.REGISTRATION_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getRegistration(TestConstants.EVENT_ID, TestConstants.REGISTRATION_ID);
        verifyNoMoreInteractions(conerCoreService);
        verifyZeroInteractions(eventBoundary, registrationBoundary);

        assertThat(registrationResponseContainer).isNotNull();
        assertThat(registrationResponseContainer.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    public void whenRegistrationIdDoesNotExistItShouldReturnNotFound() throws EventRegistrationMismatchException {
        when(conerCoreService.getRegistration(TestConstants.EVENT_ID, TestConstants.REGISTRATION_ID)).thenReturn(null);

        Response registrationResponseContainer = resources.client()
                .target("/events/" + TestConstants.EVENT_ID + "/registrations/" + TestConstants.REGISTRATION_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getRegistration(TestConstants.EVENT_ID, TestConstants.REGISTRATION_ID);
        verifyNoMoreInteractions(conerCoreService);
        verifyZeroInteractions(eventBoundary, registrationBoundary);

        assertThat(registrationResponseContainer).isNotNull();
        assertThat(registrationResponseContainer.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

    @Test
    public void whenRegistrationEventIdDoesNotMatchEventIdItShouldReturnConflict()
            throws EventRegistrationMismatchException {
        when(conerCoreService.getRegistration(TestConstants.EVENT_ID, TestConstants.REGISTRATION_ID))
                .thenThrow(new EventRegistrationMismatchException());

        Response registrationResponseContainer = resources.client()
                .target("/events/" + TestConstants.EVENT_ID + "/registrations/" + TestConstants.REGISTRATION_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getRegistration(TestConstants.EVENT_ID, TestConstants.REGISTRATION_ID);
        verifyNoMoreInteractions(conerCoreService);
        verifyZeroInteractions(eventBoundary, registrationBoundary);

        assertThat(registrationResponseContainer).isNotNull();
        assertThat(registrationResponseContainer.getStatus()).isEqualTo(HttpStatus.CONFLICT_409);
    }
}
