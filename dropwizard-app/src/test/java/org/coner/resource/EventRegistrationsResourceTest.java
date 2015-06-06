package org.coner.resource;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.api.response.GetEventRegistrationsResponse;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.*;
import org.coner.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.util.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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

        Event domainEvent = DomainEntityTestUtils.fullDomainEvent();

        // Registrations
        final String registrationID = TestConstants.REGISTRATION_ID;

        List<Registration> domainRegistrations = new ArrayList<>();
        Registration domainRegistration1 = DomainEntityTestUtils.fullDomainRegistration();

        List<RegistrationApiEntity> apiRegistrations = new ArrayList<>();
        RegistrationApiEntity apiRegistration1 = ApiEntityTestUtils.fullApiRegistration();

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

        RegistrationApiEntity requestRegistration = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/registration_add-request.json"),
                RegistrationApiEntity.class
        );
        Entity<RegistrationApiEntity> requestEntity = Entity.json(requestRegistration);

        Registration requestRegistrationAsDomain = new Registration();
        requestRegistrationAsDomain.setId("arbitrary-id-from-service");
        requestRegistrationAsDomain.setFirstName(TestConstants.REGISTRATION_FIRSTNAME);
        requestRegistrationAsDomain.setLastName(TestConstants.REGISTRATION_LASTNAME);

        Event domainEvent = DomainEntityTestUtils.fullDomainEvent();

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
