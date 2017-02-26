package org.coner.core.api.entity;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;

import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.JacksonUtil;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

public class RegistrationApiEntityTest {
    private final String fixturePath = "fixtures/api/entity/registration_full.json";

    private ObjectMapper objectMapper;
    private RegistrationApiEntity registration;

    @Before
    public void setup() {
        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);

        registration = ApiEntityTestUtils.fullRegistration();
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
        RegistrationApiEntity otherRegistration = ApiEntityTestUtils.fullRegistration();

        assertThat(registration.hashCode()).isEqualTo(otherRegistration.hashCode());
    }
}
