package org.coner.boundary;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 */
public class ReflectionEntityMergerTest {

    private final String VALUE_PROPERTY_COMMON = "property-common";
    private final boolean VALUE_PROPERTY_BOOLEAN = true;
    private final String VALUE_PROPERTY_WITH_DIFFERENT_TYPE = "property-with-different-type";
    private final String VALUE_PROPERTY_WITH_NO_COUNTERPART = "property-with-no-counterpart";
    private final String VALUE_PROPERTY_WITH_GETTER_OF_DIFFERENT_NAME = "property-with-getter-of-different-name";
    private final String VALUE_PROPERTY_WITH_NON_STANDARD_GETTER_SETTER_METHOD_NAME =
            "property-with-non-standard-getter-setter-method-name";
    private final String VALUE_PROPERTY_WITH_GETTER_WITH_PARAMETER = "property-with-getter-with-parameter";
    private final String VALUE_PROPERTY_WITH_SETTER_WITH_MORE_THAN_ONE_PARAMETER =
            "property-with-setter-with-more-than-one-parameter";

    private TestSourceEntity testSourceEntity;
    private TestDestinationEntity testDestinationEntity;

    @Before
    public void setup() {
        testSourceEntity = new TestSourceEntity(
                VALUE_PROPERTY_COMMON,
                VALUE_PROPERTY_BOOLEAN,
                VALUE_PROPERTY_WITH_DIFFERENT_TYPE,
                VALUE_PROPERTY_WITH_NO_COUNTERPART,
                VALUE_PROPERTY_WITH_GETTER_OF_DIFFERENT_NAME,
                VALUE_PROPERTY_WITH_NON_STANDARD_GETTER_SETTER_METHOD_NAME,
                VALUE_PROPERTY_WITH_GETTER_WITH_PARAMETER,
                VALUE_PROPERTY_WITH_SETTER_WITH_MORE_THAN_ONE_PARAMETER
        );
        testDestinationEntity = new TestDestinationEntity();
    }

    @Test
    public void itShouldMergeEntities() {
        ReflectionEntityMerger<TestSourceEntity, TestDestinationEntity> merger = new ReflectionEntityMerger<>();

        merger.merge(testSourceEntity, testDestinationEntity);

        assertThat(testDestinationEntity.propertyCommon).isEqualTo(VALUE_PROPERTY_COMMON);
        assertThat(testDestinationEntity.propertyBoolean).isEqualTo(VALUE_PROPERTY_BOOLEAN);
        assertThat(testDestinationEntity.propertyWithDifferentType).isZero();
        assertThat(testDestinationEntity.propertyWithSetterOfDifferentName).isNull();
        assertThat(testDestinationEntity.propertyWithNonStandardGetterSetterMethodName).isNull();
        assertThat(testDestinationEntity.propertyWithGetterWithParameter).isNull();
        assertThat(testDestinationEntity.propertyWithSetterWithMoreThanOneParameter).isNull();
    }

    @Test
    public void itShouldCallAdditionalEntityMerger() {
        EntityMerger mockEntityMerger = mock(EntityMerger.class);
        ReflectionEntityMerger<TestSourceEntity, TestDestinationEntity> merger = new ReflectionEntityMerger<>(
                mockEntityMerger
        );

        merger.merge(testSourceEntity, testDestinationEntity);

        verify(mockEntityMerger).merge(testSourceEntity, testDestinationEntity);
    }

    @Test
    public void itShouldCallAdditionalEntityMergerAfterReflectionMergeComplete() {
        ReflectionEntityMerger<TestSourceEntity, TestDestinationEntity> merger = new ReflectionEntityMerger<>(
                (sourceEntity, destinationEntity) -> {
                    assertThat(destinationEntity.propertyCommon).isEqualTo(VALUE_PROPERTY_COMMON);
                    assertThat(destinationEntity.propertyBoolean).isTrue();
                }
        );

        merger.merge(testSourceEntity, testDestinationEntity);
    }

    private static class TestSourceEntity {
        private String propertyCommon;
        private boolean propertyBoolean;
        private String propertyWithDifferentType;
        private String propertyWithNoCounterpart;
        private String propertyWithGetterOfDifferentName1;
        private String propertyWithNonStandardGetterSetterMethodName;
        private String propertyWithGetterWithParameter;
        private String propertyWithSetterWithMoreThanOneParameter;

        public TestSourceEntity(
                String propertyCommon,
                boolean propertyBoolean,
                String propertyWithDifferentType,
                String propertyWithNoCounterpart,
                String propertyWithGetterOfDifferentName1,
                String propertyWithNonStandardGetterSetterMethodName,
                String propertyWithGetterWithParameter,
                String propertyWithSetterWithMoreThanOneParameter
        ) {
            this.propertyCommon = propertyCommon;
            this.propertyBoolean = propertyBoolean;
            this.propertyWithDifferentType = propertyWithDifferentType;
            this.propertyWithNoCounterpart = propertyWithNoCounterpart;
            this.propertyWithGetterOfDifferentName1 = propertyWithGetterOfDifferentName1;
            this.propertyWithNonStandardGetterSetterMethodName = propertyWithNonStandardGetterSetterMethodName;
            this.propertyWithGetterWithParameter = propertyWithGetterWithParameter;
            this.propertyWithSetterWithMoreThanOneParameter = propertyWithSetterWithMoreThanOneParameter;
        }

        public String getPropertyCommon() {
            return propertyCommon;
        }

        public void setPropertyCommon(String propertyCommon) {
            this.propertyCommon = propertyCommon;
        }

        public boolean isPropertyBoolean() {
            return propertyBoolean;
        }

        public void setPropertyBoolean(boolean propertyBoolean) {
            this.propertyBoolean = propertyBoolean;
        }

        public String getPropertyWithDifferentType() {
            return propertyWithDifferentType;
        }

        public void setPropertyWithDifferentType(String propertyWithDifferentType) {
            this.propertyWithDifferentType = propertyWithDifferentType;
        }

        public String getPropertyWithNoCounterpart() {
            return propertyWithNoCounterpart;
        }

        public void setPropertyWithNoCounterpart(String propertyWithNoCounterpart) {
            this.propertyWithNoCounterpart = propertyWithNoCounterpart;
        }

        public String getPropertyWithGetterOfDifferentName() {
            return propertyWithGetterOfDifferentName1;
        }

        public void setPropertyWithGetterOfDifferentName(String propertyWithGetterOfDifferentName1) {
            this.propertyWithGetterOfDifferentName1 = propertyWithGetterOfDifferentName1;
        }

        public String gitPropertyWithNonStandardGetterSetterMethodName() {
            return propertyWithNonStandardGetterSetterMethodName;
        }

        public String getPropertyWithGetterWithParameter(Object parameter) {
            return propertyWithGetterWithParameter;
        }

        public void setPropertyWithGetterWithParameter(String propertyWithGetterWithParameter) {
            this.propertyWithGetterWithParameter = propertyWithGetterWithParameter;
        }

        public String getPropertyWithSetterWithMoreThanOneParameter() {
            return propertyWithSetterWithMoreThanOneParameter;
        }

        public void setPropertyWithSetterWithMoreThanOneParameter(
                String propertyWithSetterWithMoreThanOneParameter,
                Object parameter2
        ) {
            this.propertyWithSetterWithMoreThanOneParameter = propertyWithSetterWithMoreThanOneParameter;
        }
    }

    private static class TestDestinationEntity {
        private String propertyCommon;
        private boolean propertyBoolean;
        private int propertyWithDifferentType;
        private String propertyWithSetterOfDifferentName;
        private String propertyWithNonStandardGetterSetterMethodName;
        private String propertyWithGetterWithParameter;
        private String propertyWithSetterWithMoreThanOneParameter;

        public void setPropertyCommon(String propertyCommon) {
            this.propertyCommon = propertyCommon;
        }

        public boolean isPropertyBoolean() {
            return propertyBoolean;
        }

        public void setPropertyBoolean(boolean propertyBoolean) {
            this.propertyBoolean = propertyBoolean;
        }

        public void setPropertyWithDifferentType(int propertyWithDifferentType) {
            this.propertyWithDifferentType = propertyWithDifferentType;
        }

        public void setPropertyWithSetterOfDifferentName(String propertyWithSetterOfDifferentName) {
            this.propertyWithSetterOfDifferentName = propertyWithSetterOfDifferentName;
        }

        public void putPropertyWithNonStandardGetterSetterMethodName(String stuff) {
            this.propertyWithNonStandardGetterSetterMethodName = stuff;
        }

        public String getPropertyCommon() {
            return propertyCommon;
        }

        public int getPropertyWithDifferentType() {
            return propertyWithDifferentType;
        }

        public String getPropertyWithSetterOfDifferentName() {
            return propertyWithSetterOfDifferentName;
        }

        public String getPropertyWithNonStandardGetterSetterMethodName() {
            return propertyWithNonStandardGetterSetterMethodName;
        }

        public String getPropertyWithGetterWithParameter(Object parameter) {
            return propertyWithGetterWithParameter;
        }

        public void setPropertyWithGetterWithParameter(String propertyWithGetterWithParameter) {
            this.propertyWithGetterWithParameter = propertyWithGetterWithParameter;
        }

        public String getPropertyWithSetterWithMoreThanOneParameter() {
            return propertyWithSetterWithMoreThanOneParameter;
        }

        public void setPropertyWithSetterWithMoreThanOneParameter(
                String propertyWithSetterWithMoreThanOneParameter,
                Object parameter2
        ) {
            this.propertyWithSetterWithMoreThanOneParameter = propertyWithSetterWithMoreThanOneParameter;
        }
    }
}
