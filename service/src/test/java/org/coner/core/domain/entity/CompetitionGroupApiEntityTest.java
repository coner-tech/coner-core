package org.coner.core.domain.entity;

import org.assertj.core.api.Assertions;
import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.JacksonUtil;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;

public class CompetitionGroupApiEntityTest {
    private final String fixturePath = "fixtures/api/entity/competition_group_full.json";

    private ObjectMapper objectMapper;
    private CompetitionGroupApiEntity competitionGroupApiEntity;

    @Before
    public void setup() {
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);

        competitionGroupApiEntity = ApiEntityTestUtils.fullCompetitionGroup();
    }

    @Test
    public void deserializesFromJson() throws Exception {
        CompetitionGroupApiEntity actual = objectMapper.readValue(
                FixtureHelpers.fixture(fixturePath),
                CompetitionGroupApiEntity.class
        );
        Assertions.assertThat(actual).isEqualTo(competitionGroupApiEntity);
    }

    @Test
    public void serializesToJson() throws Exception {
        String actual = objectMapper.writeValueAsString(competitionGroupApiEntity);
        String expected = FixtureHelpers.fixture(fixturePath);
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void hashCodeTest() throws Exception {
        CompetitionGroupApiEntity otherCompetitionGroupApiEntity = ApiEntityTestUtils.fullCompetitionGroup();

        Assertions.assertThat(competitionGroupApiEntity.hashCode())
                .isEqualTo(otherCompetitionGroupApiEntity.hashCode());
    }
}
