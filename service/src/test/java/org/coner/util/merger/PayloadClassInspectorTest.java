package org.coner.util.merger;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class PayloadClassInspectorTest {

    private PayloadClassInspector payloadClassInspector;

    @Before
    public void setup() {
        payloadClassInspector = new PayloadClassInspector(TestPayload.class);
    }

    @Test
    public void whenHasFieldNameItShouldReturnTrue() {
        boolean actual = payloadClassInspector.hasFieldName("name");

        assertThat(actual).isTrue();
    }

    @Test
    public void whenDoesntHaveFieldNameItShouldReturnFalse() {
        boolean actual = payloadClassInspector.hasFieldName("oogabooga");

        assertThat(actual).isFalse();
    }

    @Test
    public void itShouldGetFieldByName() throws NoSuchFieldException {
        Field actual = payloadClassInspector.getFieldByName("name");

        assertThat(actual)
                .isNotNull()
                .isEqualTo(TestPayload.class.getField("name"));
    }

    public static class TestPayload {
        public int id;
        public String name;
        public boolean cool;
    }
}
