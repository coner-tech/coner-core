package org.coner.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.coner.api.response.GetEventRegistrationsResponse;
import org.coner.boundary.EventBoundary;
import org.coner.boundary.RegistrationBoundary;
import org.coner.core.ConerCoreService;
import org.coner.util.ApiEntityTestUtils;
import org.coner.util.DomainEntityTestUtils;
import org.coner.util.JacksonUtil;
import org.coner.util.TestConstants;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 */
public class EventRegistrationsResourceTest {

    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final RegistrationBoundary registrationBoundary = mock(RegistrationBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventRegistrationsResource(eventBoundary, registrationBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();


    @Before
    public void setup() {
        reset(eventBoundary, registrationBoundary, conerCoreService);
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void itShouldGetRegistrationsForEvent() throws Exception {

        // Event
        final String eventId = TestConstants.EVENT_ID;

        org.coner.core.domain.Event domainEvent = DomainEntityTestUtils.fullDomainEvent();

        // Registrations
        final String registrationID = TestConstants.REGISTRATION_ID;

        List<org.coner.core.domain.Registration> domainRegistrations = new ArrayList<>();
        org.coner.core.domain.Registration domainRegistration1 = DomainEntityTestUtils.fullDomainRegistration();

        List<org.coner.api.entity.Registration> apiRegistrations = new ArrayList<>();
        org.coner.api.entity.Registration apiRegistration1 = ApiEntityTestUtils.fullApiRegistration();

        domainRegistrations.add(domainRegistration1);
        apiRegistrations.add(apiRegistration1);

        when(conerCoreService.getEvent(eventId)).thenReturn(domainEvent);
        when(conerCoreService.getRegistrations(domainEvent)).thenReturn(domainRegistrations);
        when(registrationBoundary.toApiEntities(domainRegistrations)).thenReturn(apiRegistrations);

        GetEventRegistrationsResponse response = resources.client()
                .target("/events/" + eventId + "/registrations")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetEventRegistrationsResponse.class);

        verify(conerCoreService).getRegistrations(domainEvent);
        verify(registrationBoundary).toApiEntities(domainRegistrations);

        assertThat(response).isNotNull();
        assertThat(response.getRegistrations())
                .isNotNull()
                .isNotEmpty();
        assertThat(response.getRegistrations().get(0).getId()).isEqualTo(registrationID);
    }

    @Test
    public void itShouldCreateRegistrationForEvent() throws Exception {

        final String eventId = TestConstants.EVENT_ID;

        org.coner.api.entity.Registration requestRegistration = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/registration_add-request.json"),
                org.coner.api.entity.Registration.class
        );
        Entity<org.coner.api.entity.Registration> requestEntity = Entity.json(requestRegistration);

        org.coner.core.domain.Registration requestRegistrationAsDomain = new org.coner.core.domain.Registration();
        requestRegistrationAsDomain.setId("arbitrary-id-from-service");
        requestRegistrationAsDomain.setFirstName(TestConstants.REGISTRATION_FIRSTNAME);
        requestRegistrationAsDomain.setLastName(TestConstants.REGISTRATION_LASTNAME);

        org.coner.core.domain.Event domainEvent = DomainEntityTestUtils.fullDomainEvent();

        when(registrationBoundary.toDomainEntity(requestRegistration)).thenReturn(requestRegistrationAsDomain);
        when(conerCoreService.getEvent(eventId)).thenReturn(domainEvent);

        Response response = resources.client()
                .target("/events/" + eventId + "/registrations")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).addRegistration(domainEvent, requestRegistrationAsDomain);

        assertThat(response)
                .isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
    }

    @Test
    public void whenEventDoesNotExistItShouldReturnNotFound() {
        when(conerCoreService.getEvent(TestConstants.EVENT_ID)).thenReturn(null);

        Response eventRegistrationResponseContainer = resources.client()
                .target("/events/" + TestConstants.EVENT_ID + "/registrations")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getEvent(TestConstants.EVENT_ID);
        verifyNoMoreInteractions(conerCoreService);

        assertThat(eventRegistrationResponseContainer).isNotNull();
        assertThat(eventRegistrationResponseContainer.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}
