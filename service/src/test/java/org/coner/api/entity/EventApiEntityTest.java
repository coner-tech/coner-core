package org.coner.api.entity;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;

import org.coner.util.ApiEntityTestUtils;
import org.coner.util.JacksonUtil;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

public class EventApiEntityTest {

    private final String fixturePath = "fixtures/api/entity/event_full.json";

    private ObjectMapper objectMapper;
    private EventApiEntity event;

    @Before
    public void setup() {
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);

        event = ApiEntityTestUtils.fullApiEvent();
    }

    @Test
    public void deserializesFromJson() throws Exception {
        EventApiEntity actual = objectMapper.readValue(fixture(fixturePath), EventApiEntity.class);
        assertThat(actual).isEqualTo(event);
    }

    @Test
    public void serializesToJson() throws Exception {
        String actual = objectMapper.writeValueAsString(event);
        String expected = fixture(fixturePath);
        JSONAssert.assertEquals(expected, actual, false);
    }

}
