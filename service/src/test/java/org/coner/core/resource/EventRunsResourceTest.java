package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.TestConstants.EVENT_ID;
import static org.coner.core.util.TestConstants.RUN_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.coner.core.api.entity.RunApiEntity;
import org.coner.core.api.request.AddRunRequest;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.domain.service.RunEntityService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.RunMapper;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.JacksonUtil;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;

public class EventRunsResourceTest {

    private RunEntityService runEntityService = mock(RunEntityService.class);
    private RunMapper runMapper = mock(RunMapper.class);

    private ObjectMapper objectMapper;

    private DomainServiceExceptionMapper domainServiceExceptionMapper = new DomainServiceExceptionMapper();
    private RuntimeExceptionUnwrappingMapper runtimeExceptionUnwrappingMapper = new RuntimeExceptionUnwrappingMapper(
            domainServiceExceptionMapper
    );

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventRunsResource(
                    runEntityService,
                    runMapper
            ))
            .addResource(domainServiceExceptionMapper)
            .addResource(runtimeExceptionUnwrappingMapper)
            .build();

    @Before
    public void setup() {
        reset(runMapper, runEntityService);
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void itShouldAddRunWithFullDetails() throws Exception {
        AddRunRequest apiRequest = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/run_add-request_full.json"),
                AddRunRequest.class
        );
        Entity<AddRunRequest> requestEntity = Entity.json(apiRequest);
        RunAddPayload addPayload = mock(RunAddPayload.class);
        when(runMapper.toDomainAddPayload(apiRequest, EVENT_ID)).thenReturn(addPayload);
        Run domainEntity = DomainEntityTestUtils.fullRun();
        when(runEntityService.add(addPayload)).thenReturn(domainEntity);
        RunApiEntity apiEntity = ApiEntityTestUtils.fullRun();
        when(runMapper.toApiEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target(UriBuilder.fromPath("/events/{eventId}/runs").build(EVENT_ID))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(runEntityService).add(addPayload);
        verifyNoMoreInteractions(runEntityService);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaderString(HttpHeader.LOCATION.asString()))
                .containsSequence("/events/", EVENT_ID, "/runs/", RUN_ID);
    }

    @Test
    public void itShouldAddRunWithoutRegistration() throws Exception {
        AddRunRequest apiRequest = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/run_add-request_without-registration.json"),
                AddRunRequest.class
        );
        Entity<AddRunRequest> requestEntity = Entity.json(apiRequest);
        RunAddPayload addPayload = mock(RunAddPayload.class);
        when(runMapper.toDomainAddPayload(apiRequest, EVENT_ID)).thenReturn(addPayload);
        Run domainEntity = DomainEntityTestUtils.fullRun();
        domainEntity.setRegistration(null);
        when(runEntityService.add(addPayload)).thenReturn(domainEntity);
        RunApiEntity apiEntity = ApiEntityTestUtils.fullRun();
        apiEntity.setRegistrationId(null);
        when(runMapper.toApiEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target(UriBuilder.fromPath("/events/{eventId}/runs").build(EVENT_ID))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(runEntityService).add(addPayload);
        verifyNoMoreInteractions(runEntityService);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaderString(HttpHeader.LOCATION.asString()))
                .containsSequence("/events/", EVENT_ID, "/runs/", RUN_ID);
    }

    @Test
    public void itShouldAddRunWithoutRawTime() throws Exception {
        AddRunRequest apiRequest = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/run_add-request_without-raw-time.json"),
                AddRunRequest.class
        );
        Entity<AddRunRequest> requestEntity = Entity.json(apiRequest);
        RunAddPayload addPayload = mock(RunAddPayload.class);
        when(runMapper.toDomainAddPayload(apiRequest, EVENT_ID)).thenReturn(addPayload);
        Run domainEntity = DomainEntityTestUtils.fullRun();
        domainEntity.setRawTime(null);
        when(runEntityService.add(addPayload)).thenReturn(domainEntity);
        RunApiEntity apiEntity = ApiEntityTestUtils.fullRun();
        apiEntity.setRawTime(null);
        when(runMapper.toApiEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target(UriBuilder.fromPath("/events/{eventId}/runs").build(EVENT_ID))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(runEntityService).add(addPayload);
        verifyNoMoreInteractions(runEntityService);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaderString(HttpHeader.LOCATION.asString()))
                .containsSequence("/events/", EVENT_ID, "/runs/", RUN_ID);
    }

    @Test
    public void itShouldAddRunWithoutPenalty() throws Exception {
        AddRunRequest apiRequest = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/run_add-request_without-penalty.json"),
                AddRunRequest.class
        );
        Entity<AddRunRequest> requestEntity = Entity.json(apiRequest);
        RunAddPayload addPayload = mock(RunAddPayload.class);
        when(runMapper.toDomainAddPayload(apiRequest, EVENT_ID)).thenReturn(addPayload);
        Run domainEntity = DomainEntityTestUtils.fullRun();
        domainEntity.setDidNotFinish(false);
        when(runEntityService.add(addPayload)).thenReturn(domainEntity);
        RunApiEntity apiEntity = ApiEntityTestUtils.fullRun();
        apiEntity.setDidNotFinish(false);
        when(runMapper.toApiEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target(UriBuilder.fromPath("/events/{eventId}/runs").build(EVENT_ID))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(runEntityService).add(addPayload);
        verifyNoMoreInteractions(runEntityService);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaderString(HttpHeader.LOCATION.asString()))
                .containsSequence("/events/", EVENT_ID, "/runs/", RUN_ID);
    }

    @Test
    public void itShouldFailToAddRunWhenMapperThrowsRuntimeExceptionWrappingEntityNotFoundException() {
        AddRunRequest apiRequest = ApiRequestTestUtils.fullAddRun();
        Entity<AddRunRequest> requestEntity = Entity.json(apiRequest);
        when(runMapper.toDomainAddPayload(apiRequest, EVENT_ID))
                .thenThrow(new RuntimeException(new EntityNotFoundException(Event.class, EVENT_ID)));

        Response response = resources.client()
                .target(UriBuilder.fromPath("/events/{eventId}/runs").build(EVENT_ID))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verifyZeroInteractions(runEntityService);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
        assertThat(response.readEntity(ErrorMessage.class))
                .extracting(ErrorMessage::getCode, ErrorMessage::getMessage)
                .containsExactly(HttpStatus.NOT_FOUND_404, "Event entity not found with id " + EVENT_ID);
    }

}
