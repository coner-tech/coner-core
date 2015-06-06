package org.coner.api.entity;

import org.coner.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.*;
import org.skyscreamer.jsonassert.JSONAssert;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;

public class HandicapGroupApiEntityTest {
    private final String fixturePath = "fixtures/api/entity/handicap_group_full.json";

    private ObjectMapper objectMapper;
    private HandicapGroupApiEntity handicapGroup;

    @Before
    public void setup() {
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);

        handicapGroup = ApiEntityTestUtils.fullHandicapGroup();
    }

    @Test
    public void deserializesFromJson() throws Exception {
        HandicapGroupApiEntity actual = objectMapper.readValue(fixture(fixturePath), HandicapGroupApiEntity.class);
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
        HandicapGroupApiEntity otherHandicapGroup = ApiEntityTestUtils.fullHandicapGroup();

        assertThat(handicapGroup.hashCode()).isEqualTo(otherHandicapGroup.hashCode());
    }
}
