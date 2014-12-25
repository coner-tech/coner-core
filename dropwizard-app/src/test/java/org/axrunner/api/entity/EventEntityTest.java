package org.axrunner.api.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.axrunner.util.JacksonUtil;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;

/**
 *
 */
public class EventEntityTest {

    private final String fixturePath = "fixtures/api/entity/event_full.json";

    private ObjectMapper objectMapper;
    private Event event;

    @Before
    public void setup() {
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);

        event = new Event();
        event.setId("test-id");
        event.setName("test name");
        ZonedDateTime zdt = ZonedDateTime.parse("2014-12-24T07:27:00-05:00");
        event.setDate(Date.from(Instant.from(zdt)));
    }

    @Test
    public void deserializesFromJson() throws Exception {
        Event actual = objectMapper.readValue(fixture(fixturePath), Event.class);
        assertThat(actual).isEqualTo(event);
    }

    @Test
    public void serializesToJson() throws Exception {
        String actual = objectMapper.writeValueAsString(event);
        String expected = fixture(fixturePath);
        assertThat(actual).isEqualTo(expected);
    }

}
