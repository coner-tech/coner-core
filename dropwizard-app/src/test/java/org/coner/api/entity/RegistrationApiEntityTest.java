package org.coner.api.entity;

import org.coner.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.*;
import org.skyscreamer.jsonassert.JSONAssert;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;

public class RegistrationApiEntityTest {
    private final String fixturePath = "fixtures/api/entity/registration_full.json";

    private ObjectMapper objectMapper;
    private RegistrationApiEntity registration;

    @Before
    public void setup() {
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);

        registration = ApiEntityTestUtils.fullApiRegistration();
    }

    @Test
    public void deserializesFromJson() throws Exception {
        RegistrationApiEntity actual = objectMapper.readValue(fixture(fixturePath), RegistrationApiEntity.class);
        assertThat(actual).isEqualTo(registration);
    }

    @Test
    public void serializesToJson() throws Exception {
        String actual = objectMapper.writeValueAsString(registration);
        String expected = fixture(fixturePath);
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void hashCodeTest() throws Exception {
        RegistrationApiEntity otherRegistration = ApiEntityTestUtils.fullApiRegistration();

        assertThat(registration.hashCode()).isEqualTo(otherRegistration.hashCode());
    }
}
