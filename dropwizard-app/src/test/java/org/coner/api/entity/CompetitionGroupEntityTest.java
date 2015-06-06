package org.coner.api.entity;

import org.coner.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.assertj.core.api.Assertions;
import org.junit.*;
import org.skyscreamer.jsonassert.JSONAssert;

public class CompetitionGroupEntityTest {
    private final String fixturePath = "fixtures/api/entity/competition_group_full.json";

    private ObjectMapper objectMapper;
    private CompetitionGroup competitionGroup;

    @Before
    public void setup() {
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);

        competitionGroup = ApiEntityTestUtils.fullCompetitionGroup();
    }

    @Test
    public void deserializesFromJson() throws Exception {
        CompetitionGroup actual = objectMapper.readValue(FixtureHelpers.fixture(fixturePath), CompetitionGroup.class);
        Assertions.assertThat(actual).isEqualTo(competitionGroup);
    }

    @Test
    public void serializesToJson() throws Exception {
        String actual = objectMapper.writeValueAsString(competitionGroup);
        String expected = FixtureHelpers.fixture(fixturePath);
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void hashCodeTest() throws Exception {
        CompetitionGroup otherCompetitionGroup = ApiEntityTestUtils.fullCompetitionGroup();

        Assertions.assertThat(competitionGroup.hashCode()).isEqualTo(otherCompetitionGroup.hashCode());
    }
}
