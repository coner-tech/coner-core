package org.coner.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.HandicapGroup;
import org.coner.util.JacksonUtil;
import org.coner.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class HandicapGroupsResourceTest {
    private final HandicapGroupBoundary handicapGroupBoundary = mock(HandicapGroupBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HandicapGroupsResource(handicapGroupBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(handicapGroupBoundary, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void whenAddValidHandicapGroupItShouldAddHandicapGroup() throws Exception {
        org.coner.api.entity.HandicapGroup requestHandicapGroup = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/handicap_group_add-request.json"),
                org.coner.api.entity.HandicapGroup.class
        );

        Entity<org.coner.api.entity.HandicapGroup> requestEntity = Entity.json(requestHandicapGroup);

        org.coner.core.domain.HandicapGroup requestHandicapGroupAsDomain = new org.coner.core.domain.HandicapGroup();
        requestHandicapGroupAsDomain.setId("arbitrary-id-from-service");
        requestHandicapGroupAsDomain.setName("C Street");
        requestHandicapGroupAsDomain.setHandicapFactor(BigDecimal.valueOf(0.75));

        when(handicapGroupBoundary.toDomainEntity(requestHandicapGroup)).thenReturn(requestHandicapGroupAsDomain);

        Response response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).addHandicapGroup(requestHandicapGroupAsDomain);

        assertThat(response)
                .isNotNull();
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaders().get("Location").get(0)).isNotNull();
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo("arbitrary-id-from-service");
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

}
