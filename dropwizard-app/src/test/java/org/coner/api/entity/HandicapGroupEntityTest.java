package org.coner.api.entity;

import org.coner.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.*;
import org.skyscreamer.jsonassert.JSONAssert;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;

public class HandicapGroupEntityTest {
    private final String fixturePath = "fixtures/api/entity/handicap_group_full.json";

    private ObjectMapper objectMapper;
    private HandicapGroup handicapGroup;

    @Before
    public void setup() {
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);

        handicapGroup = ApiEntityTestUtils.fullHandicapGroup();
    }

    @Test
    public void deserializesFromJson() throws Exception {
        HandicapGroup actual = objectMapper.readValue(fixture(fixturePath), HandicapGroup.class);
        assertThat(actual).isEqualTo(handicapGroup);
    }

    @Test
    public void serializesToJson() throws Exception {
        String actual = objectMapper.writeValueAsString(handicapGroup);
        String expected = fixture(fixturePath);
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void hashCodeTest() throws Exception {
        HandicapGroup otherHandicapGroup = ApiEntityTestUtils.fullHandicapGroup();

        assertThat(handicapGroup.hashCode()).isEqualTo(otherHandicapGroup.hashCode());
    }
}
