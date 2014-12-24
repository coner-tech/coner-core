package org.axrunner.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.axrunner.api.entity.Event;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;

/**
 *
 */
public class ApiEventDeserializationTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = Jackson.newObjectMapper();
    }

    @Test
    public void deserializesFromJson() throws Exception {
        Event expected = new Event();
        expected.setId("test-id");
        expected.setName("test name");
        ZonedDateTime zdt = ZonedDateTime.parse("2014-12-24T07:27:00-05:00");
        expected.setDate(Date.from(Instant.from(zdt)));

        Event actual = objectMapper.readValue(FixtureHelpers.fixture("fixtures/event.json"), Event.class);

        assertThat(actual).isEqualTo(expected);
    }

}
