package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.TestConstants.EVENT_ID;
import static org.coner.core.util.TestConstants.REGISTRATION_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.api.response.GetEventRegistrationsResponse;
import org.coner.core.boundary.RegistrationApiAddPayloadBoundary;
import org.coner.core.boundary.RegistrationApiDomainBoundary;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.domain.service.EventRegistrationService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.JacksonUtil;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;

public class EventRegistrationsResourceTest {

    private final EventRegistrationService eventRegistrationService = mock(EventRegistrationService.class);
    private final RegistrationApiDomainBoundary apiDomainBoundary = mock(RegistrationApiDomainBoundary.class);
    private final RegistrationApiAddPayloadBoundary addPayloadBoundary = mock(RegistrationApiAddPayloadBoundary.class);

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventRegistrationsResource(
                    eventRegistrationService,
                    apiDomainBoundary,
                    addPayloadBoundary
            ))
            .addResource(new DomainServiceExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(apiDomainBoundary, eventRegistrationService);
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void itShouldGetRegistrationsForEvent() throws Exception {
        List<Registration> domainRegistrations = Arrays.asList(DomainEntityTestUtils.fullDomainRegistration());
        List<RegistrationApiEntity> apiRegistrations = Arrays.asList(ApiEntityTestUtils.fullApiRegistration());
        when(eventRegistrationService.getAllWithEventId(EVENT_ID)).thenReturn(domainRegistrations);
        when(apiDomainBoundary.toLocalEntities(domainRegistrations)).thenReturn(apiRegistrations);

        GetEventRegistrationsResponse response = resources.client()
                .target("/events/" + EVENT_ID + "/registrations")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetEventRegistrationsResponse.class);

        verify(eventRegistrationService).getAllWithEventId(EVENT_ID);
        assertThat(response).isNotNull();
        assertThat(response.getEntities())
                .isNotNull()
                .isNotEmpty();
        assertThat(response.getEntities().get(0).getId()).isEqualTo(REGISTRATION_ID);
    }

    @Test
    public void itShouldCreateRegistrationForEvent() throws Exception {
        final String eventId = EVENT_ID;
        AddRegistrationRequest apiRequest = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/registration_add-request.json"),
                AddRegistrationRequest.class
        );
        Entity<AddRegistrationRequest> requestEntity = Entity.json(apiRequest);
        RegistrationAddPayload addPayload = mock(RegistrationAddPayload.class);
        when(addPayloadBoundary.toRemoteEntity(apiRequest)).thenReturn(addPayload);
        Registration domainEntity = DomainEntityTestUtils.fullDomainRegistration();
        when(eventRegistrationService.add(addPayload)).thenReturn(domainEntity);
        RegistrationApiEntity apiEntity = ApiEntityTestUtils.fullApiRegistration();
        when(apiDomainBoundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target("/events/" + eventId + "/registrations")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(eventRegistrationService).add(addPayload);
        verifyNoMoreInteractions(eventRegistrationService);
        assertThat(response)
                .isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
    }

    @Test
    public void whenEventDoesNotExistItShouldRespondNotFound() throws Exception {
        when(eventRegistrationService.getAllWithEventId(EVENT_ID)).thenThrow(EntityNotFoundException.class);

        Response eventRegistrationResponseContainer = resources.client()
                .target("/events/" + EVENT_ID + "/registrations")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(eventRegistrationService).getAllWithEventId(EVENT_ID);
        verifyNoMoreInteractions(eventRegistrationService);

        assertThat(eventRegistrationResponseContainer.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}
