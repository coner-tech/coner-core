package org.coner.core.util.merger;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class JavaBeanClassInspectorTest {

    private JavaBeanClassInspector javaBeanClassInspector;

    @Before
    public void setup() {
        javaBeanClassInspector = new JavaBeanClassInspector(TestJavaBeanEntity.class);
    }

    @Test
    public void itShouldGetFieldNamesWithDirectAccessors() {
        Set<String> actual = javaBeanClassInspector.getFieldNamesWithDirectAccessors();

        assertThat(actual)
                .hasSize(3)
                .contains("id", "name", "cool");
    }

    @Test
    public void itShouldGetDirectAccessorByFieldName() throws NoSuchMethodException {
        Method actual = javaBeanClassInspector.getDirectAccessorByFieldName("name");

        assertThat(actual)
                .isNotNull()
                .isEqualTo(TestJavaBeanEntity.class.getMethod("getName"));
    }

    @Test
    public void itShouldGetDirectMutatorByFieldName() throws NoSuchMethodException {
        Method actual = javaBeanClassInspector.getDirectMutatorByFieldName("name");

        assertThat(actual)
                .isNotNull()
                .isEqualTo(TestJavaBeanEntity.class.getMethod("setName", String.class));
    }

    @Test
    public void whenItHasFieldWithDirectMutatorItShouldReturnTrue() {
        boolean actual = javaBeanClassInspector.hasFieldWithDirectMutator("name");

        assertThat(actual).isTrue();
    }

    @Test
    public void whenItDoesntHaveFieldNameWithDirectMutatorItShouldReturnFalse() {
        boolean actual = javaBeanClassInspector.hasFieldWithDirectMutator("entityInfo");

        assertThat(actual).isFalse();
    }

    static class TestJavaBeanEntity {
        private int id;
        private String name;
        private boolean cool;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isCool() {
            return cool;
        }

        public void setCool(boolean cool) {
            this.cool = cool;
        }

        // a method which isn't a javabean accessor/mutator
        @Override
        public String toString() {
            return "SomeSortOfEntity{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        // an indirect accessor
        public String getEntityInfo() {
            return name;
        }

        // an indirect mutator
        public void setEntityInfo(String entityInfo) {
            this.name = entityInfo;
        }
    }

}
