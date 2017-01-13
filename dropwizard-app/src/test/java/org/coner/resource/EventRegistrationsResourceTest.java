package org.coner.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.util.TestConstants.EVENT_ID;
import static org.coner.util.TestConstants.REGISTRATION_ID;
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

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.api.request.AddRegistrationRequest;
import org.coner.api.response.GetEventRegistrationsResponse;
import org.coner.boundary.RegistrationApiAddPayloadBoundary;
import org.coner.boundary.RegistrationApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.exception.EntityNotFoundException;
import org.coner.util.ApiEntityTestUtils;
import org.coner.util.DomainEntityTestUtils;
import org.coner.util.JacksonUtil;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;

public class EventRegistrationsResourceTest {

    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);
    private final RegistrationApiDomainBoundary apiDomainBoundary = mock(RegistrationApiDomainBoundary.class);
    private final RegistrationApiAddPayloadBoundary addPayloadBoundary = mock(RegistrationApiAddPayloadBoundary.class);

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventRegistrationsResource(
                    conerCoreService,
                    apiDomainBoundary,
                    addPayloadBoundary
            ))
            .build();

    @Before
    public void setup() {
        reset(apiDomainBoundary, conerCoreService);
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void itShouldGetRegistrationsForEvent() throws Exception {
        List<Registration> domainRegistrations = Arrays.asList(DomainEntityTestUtils.fullDomainRegistration());
        List<RegistrationApiEntity> apiRegistrations = Arrays.asList(ApiEntityTestUtils.fullApiRegistration());
        when(conerCoreService.getRegistrations(EVENT_ID)).thenReturn(domainRegistrations);
        when(apiDomainBoundary.toLocalEntities(domainRegistrations)).thenReturn(apiRegistrations);

        GetEventRegistrationsResponse response = resources.client()
                .target("/events/" + EVENT_ID + "/registrations")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetEventRegistrationsResponse.class);

        verify(conerCoreService).getRegistrations(EVENT_ID);
        assertThat(response).isNotNull();
        assertThat(response.getRegistrations())
                .isNotNull()
                .isNotEmpty();
        assertThat(response.getRegistrations().get(0).getId()).isEqualTo(REGISTRATION_ID);
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
        when(conerCoreService.addRegistration(addPayload)).thenReturn(domainEntity);
        RegistrationApiEntity apiEntity = ApiEntityTestUtils.fullApiRegistration();
        when(apiDomainBoundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target("/events/" + eventId + "/registrations")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).addRegistration(addPayload);
        verifyNoMoreInteractions(conerCoreService);
        assertThat(response)
                .isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
    }

    @Test
    public void whenEventDoesNotExistItShouldRespondNotFound() throws Exception {
        when(conerCoreService.getRegistrations(EVENT_ID)).thenThrow(EntityNotFoundException.class);

        Response eventRegistrationResponseContainer = resources.client()
                .target("/events/" + EVENT_ID + "/registrations")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getRegistrations(EVENT_ID);
        verifyNoMoreInteractions(conerCoreService);

        assertThat(eventRegistrationResponseContainer.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}
