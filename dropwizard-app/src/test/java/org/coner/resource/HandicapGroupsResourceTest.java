package org.coner.resource;

import org.coner.api.response.*;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.HandicapGroup;
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
    private final HandicapGroupBoundary handicapGroupBoundary = mock(HandicapGroupBoundary.class);
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
        org.coner.api.entity.HandicapGroup requestHandicapGroup = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/handicap_group_add-request-with-id.json"),
                org.coner.api.entity.HandicapGroup.class
        );
        Entity<org.coner.api.entity.HandicapGroup> requestEntity = Entity.json(requestHandicapGroup);

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
        org.coner.api.entity.HandicapGroup requestHandicapGroup = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/handicap_group_add-request-wrong-factor.json"),
                org.coner.api.entity.HandicapGroup.class
        );
        Entity<org.coner.api.entity.HandicapGroup> requestEntity = Entity.json(requestHandicapGroup);


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

        verify(handicapGroupBoundary).toApiEntities(domainHandicapGroups);
        verify(conerCoreService).getHandicapGroups();
    }

    @Test
    public void itShouldGetPostedHandicapGroup() throws Exception {
        postHandicapGroup();

        List<HandicapGroup> domainHandicapGroups = new ArrayList<>();
        domainHandicapGroups.add(DomainEntityTestUtils.fullHandicapGroup());
        List<org.coner.api.entity.HandicapGroup> apiHandicapGroups = new ArrayList<>();
        apiHandicapGroups.add(ApiEntityTestUtils.fullHandicapGroup());

        when(conerCoreService.getHandicapGroups())
                .thenReturn(domainHandicapGroups);
        when(handicapGroupBoundary.toApiEntities(domainHandicapGroups))
                .thenReturn(apiHandicapGroups);

        GetHandicapGroupsResponse response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetHandicapGroupsResponse.class);

        verify(handicapGroupBoundary).toApiEntities(domainHandicapGroups);
        verify(conerCoreService).getHandicapGroups();
        verifyNoMoreInteractions(conerCoreService, handicapGroupBoundary);

        assertThat(response)
                .isNotNull();
        assertThat(response.getHandicapGroups())
                .isNotNull()
                .hasSize(1);

    }


    private Response postHandicapGroup() throws Exception {
        org.coner.api.entity.HandicapGroup requestHandicapGroup = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/handicap_group_add-request.json"),
                org.coner.api.entity.HandicapGroup.class
        );

        Entity<org.coner.api.entity.HandicapGroup> requestEntity = Entity.json(requestHandicapGroup);

        org.coner.core.domain.HandicapGroup requestHandicapGroupAsDomain = DomainEntityTestUtils.fullHandicapGroup();

        when(handicapGroupBoundary.toDomainEntity(requestHandicapGroup)).thenReturn(requestHandicapGroupAsDomain);

        Response response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(handicapGroupBoundary).toDomainEntity(requestHandicapGroup);
        verify(conerCoreService).addHandicapGroup(requestHandicapGroupAsDomain);
        verifyNoMoreInteractions(conerCoreService, handicapGroupBoundary);

        return response;
    }

}
