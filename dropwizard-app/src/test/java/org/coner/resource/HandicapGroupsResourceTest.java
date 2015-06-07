package org.coner.resource;

import org.coner.api.entity.HandicapGroupApiEntity;
import org.coner.api.response.*;
import org.coner.boundary.HandicapGroupApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.HandicapGroup;
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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class HandicapGroupsResourceTest {
    private final HandicapGroupApiDomainBoundary handicapGroupBoundary = mock(HandicapGroupApiDomainBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);
    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HandicapGroupsResource(handicapGroupBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        reset(handicapGroupBoundary, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void whenAddValidHandicapGroupItShouldAddHandicapGroup() throws Exception {
        Response response = postHandicapGroup();

        assertThat(response)
                .isNotNull();
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaders().get("Location").get(0)).isNotNull();
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo(TestConstants.HANDICAP_GROUP_ID);
    }

    @Test
    public void whenAddHandicapGroupWithUserSuppliedIdItShouldFailValidation() throws Exception {
        HandicapGroupApiEntity requestHandicapGroup = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/handicap_group_add-request-with-id.json"),
                HandicapGroupApiEntity.class
        );
        Entity<HandicapGroupApiEntity> requestEntity = Entity.json(requestHandicapGroup);

        Response response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
        assertThat(errorsResponse.getErrors())
                .contains("id handicapGroup.id may only be assigned by the system (was bad-id-in-request)");

        verify(conerCoreService, never()).addHandicapGroup(any(HandicapGroup.class));
    }

    @Test
    public void whenAddHandicapGroupWithLargeFactorItShouldFailValidation() throws Exception {
        HandicapGroupApiEntity requestHandicapGroup = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/handicap_group_add-request-wrong-factor.json"),
                HandicapGroupApiEntity.class
        );
        Entity<HandicapGroupApiEntity> requestEntity = Entity.json(requestHandicapGroup);


        Response response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
        assertThat(errorsResponse.getErrors())
                .contains("handicapFactor must be less than or equal to 1.0000 (was 5.00)");

        verify(conerCoreService, never()).addHandicapGroup(any(HandicapGroup.class));
    }

    @Test
    public void itShouldGetHandicapGroups() throws Exception {
        List<HandicapGroup> domainHandicapGroups = new ArrayList<>();
        when(conerCoreService.getHandicapGroups()).thenReturn(domainHandicapGroups);

        GetHandicapGroupsResponse response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetHandicapGroupsResponse.class);

        assertThat(response)
                .isNotNull();
        assertThat(response.getHandicapGroups())
                .isNotNull()
                .isEmpty();

        verify(conerCoreService).getHandicapGroups();
        verify(handicapGroupBoundary).toLocalEntities(domainHandicapGroups);
    }

    @Test
    public void itShouldGetPostedHandicapGroup() throws Exception {
        postHandicapGroup();

        List<HandicapGroup> domainHandicapGroups = new ArrayList<>();
        domainHandicapGroups.add(DomainEntityTestUtils.fullHandicapGroup());
        List<HandicapGroupApiEntity> handicapGroupApiEntities = new ArrayList<>();
        handicapGroupApiEntities.add(ApiEntityTestUtils.fullHandicapGroup());

        when(conerCoreService.getHandicapGroups())
                .thenReturn(domainHandicapGroups);
        when(handicapGroupBoundary.toLocalEntities(domainHandicapGroups))
                .thenReturn(handicapGroupApiEntities);

        GetHandicapGroupsResponse response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetHandicapGroupsResponse.class);

        verify(handicapGroupBoundary).toLocalEntities(domainHandicapGroups);
        verify(conerCoreService).getHandicapGroups();
        verifyNoMoreInteractions(conerCoreService, handicapGroupBoundary);

        assertThat(response)
                .isNotNull();
        assertThat(response.getHandicapGroups())
                .isNotNull()
                .hasSize(1);

    }

    private Response postHandicapGroup() throws Exception {
        HandicapGroupApiEntity requestHandicapGroup = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/handicap_group_add-request.json"),
                HandicapGroupApiEntity.class
        );

        Entity<HandicapGroupApiEntity> requestEntity = Entity.json(requestHandicapGroup);

        HandicapGroup requestHandicapGroupAsDomain = DomainEntityTestUtils.fullHandicapGroup();

        when(handicapGroupBoundary.toRemoteEntity(requestHandicapGroup)).thenReturn(requestHandicapGroupAsDomain);

        Response response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(handicapGroupBoundary).toRemoteEntity(requestHandicapGroup);
        verify(conerCoreService).addHandicapGroup(requestHandicapGroupAsDomain);
        verifyNoMoreInteractions(conerCoreService, handicapGroupBoundary);

        return response;
    }

}
