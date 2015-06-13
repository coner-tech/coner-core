package org.coner.util.merger;

import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionJavaBeanMergerTest {

    private static final String VALUE_PROPERTY_COMMON = "property-common";
    private static final boolean VALUE_PROPERTY_BOOLEAN = true;
    private static final String VALUE_PROPERTY_WITH_DIFFERENT_TYPE = "property-with-different-type";
    private static final String VALUE_PROPERTY_WITH_NO_COUNTERPART = "property-with-no-counterpart";
    private static final String VALUE_PROPERTY_WITH_GETTER_OF_DIFFERENT_NAME = "property-with-getter-of-different-name";
    private static final String VALUE_PROPERTY_WITH_NON_STANDARD_GETTER_SETTER_METHOD_NAME =
            "property-with-non-standard-getter-setter-method-name";
    private static final String VALUE_PROPERTY_WITH_GETTER_WITH_PARAMETER = "property-with-getter-with-parameter";
    private static final String VALUE_PROPERTY_WITH_SETTER_WITH_MORE_THAN_ONE_PARAMETER =
            "property-with-setter-with-more-than-one-parameter";
    private static final String VALUE_PROPERTY_STRING_WITH_DESTINATION_ENUM = "EAST";
    private static final TestSourceEntity.Status VALUE_PROPERTY_ENUM_WITH_STRING_DESTINATION =
            TestSourceEntity.Status.PLAYING;

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
                VALUE_PROPERTY_WITH_SETTER_WITH_MORE_THAN_ONE_PARAMETER,
                VALUE_PROPERTY_STRING_WITH_DESTINATION_ENUM,
                VALUE_PROPERTY_ENUM_WITH_STRING_DESTINATION
        );
        testDestinationEntity = new TestDestinationEntity();
    }

    @Test
    public void itShouldMergeEntities() {
        ReflectionJavaBeanMerger<TestSourceEntity, TestDestinationEntity> merger = new ReflectionJavaBeanMerger<>();

        merger.merge(testSourceEntity, testDestinationEntity);

        assertThat(testDestinationEntity.propertyCommon).isEqualTo(VALUE_PROPERTY_COMMON);
        assertThat(testDestinationEntity.propertyBoolean).isEqualTo(VALUE_PROPERTY_BOOLEAN);
        assertThat(testDestinationEntity.propertyWithDifferentType).isZero();
        assertThat(testDestinationEntity.propertyWithSetterOfDifferentName).isNull();
        assertThat(testDestinationEntity.propertyWithNonStandardGetterSetterMethodName).isNull();
        assertThat(testDestinationEntity.propertyWithGetterWithParameter).isNull();
        assertThat(testDestinationEntity.propertyWithSetterWithMoreThanOneParameter).isNull();
        assertThat(testDestinationEntity.propertyStringWithDestinationEnum)
                .isEqualTo(TestDestinationEntity.Direction.EAST);
        assertThat(testDestinationEntity.propertyEnumWithStringDestination).isEqualTo("PLAYING");
    }

    @Test
    public void itShouldTransformStringValueInvalidToEnumNull() {
        ReflectionJavaBeanMerger<TestSourceEntity, TestDestinationEntity> merger = new ReflectionJavaBeanMerger<>();

        // null string
        testSourceEntity.setPropertyStringWithDestinationEnum(null);
        merger.merge(testSourceEntity, testDestinationEntity);
        assertThat(testDestinationEntity.getPropertyStringWithDestinationEnum()).isNull();

        // empty string
        testSourceEntity.setPropertyStringWithDestinationEnum("");
        merger.merge(testSourceEntity, testDestinationEntity);
        assertThat(testDestinationEntity.getPropertyStringWithDestinationEnum()).isNull();

        // garbage name string
        testSourceEntity.setPropertyStringWithDestinationEnum("garbage");
        merger.merge(testSourceEntity, testDestinationEntity);
        assertThat(testDestinationEntity.getPropertyStringWithDestinationEnum()).isNull();

        // string with illegal enum name
        testSourceEntity.setPropertyStringWithDestinationEnum("`~!@#$%^&*()-+=?/>.,<';:");
        merger.merge(testSourceEntity, testDestinationEntity);
        assertThat(testDestinationEntity.getPropertyStringWithDestinationEnum()).isNull();
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
        private String propertyStringWithDestinationEnum;
        private Status propertyEnumWithStringDestination;

        public TestSourceEntity(
                String propertyCommon,
                boolean propertyBoolean,
                String propertyWithDifferentType,
                String propertyWithNoCounterpart,
                String propertyWithGetterOfDifferentName1,
                String propertyWithNonStandardGetterSetterMethodName,
                String propertyWithGetterWithParameter,
                String propertyWithSetterWithMoreThanOneParameter,
                String propertyStringWithDestinationEnum,
                Status propertyEnumWithStringDestination) {
            this.propertyCommon = propertyCommon;
            this.propertyBoolean = propertyBoolean;
            this.propertyWithDifferentType = propertyWithDifferentType;
            this.propertyWithNoCounterpart = propertyWithNoCounterpart;
            this.propertyWithGetterOfDifferentName1 = propertyWithGetterOfDifferentName1;
            this.propertyWithNonStandardGetterSetterMethodName = propertyWithNonStandardGetterSetterMethodName;
            this.propertyWithGetterWithParameter = propertyWithGetterWithParameter;
            this.propertyWithSetterWithMoreThanOneParameter = propertyWithSetterWithMoreThanOneParameter;
            this.propertyStringWithDestinationEnum = propertyStringWithDestinationEnum;
            this.propertyEnumWithStringDestination = propertyEnumWithStringDestination;
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

        public String getPropertyWithGetterOfDifferentName1() {
            return propertyWithGetterOfDifferentName1;
        }

        public void setPropertyWithGetterOfDifferentName1(String propertyWithGetterOfDifferentName1) {
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

        public Status getPropertyEnumWithStringDestination() {
            return propertyEnumWithStringDestination;
        }

        public void setPropertyEnumWithStringDestination(Status propertyEnumWithStringDestination) {
            this.propertyEnumWithStringDestination = propertyEnumWithStringDestination;
        }

        public String getPropertyStringWithDestinationEnum() {
            return propertyStringWithDestinationEnum;
        }

        public void setPropertyStringWithDestinationEnum(String propertyStringWithDestinationEnum) {
            this.propertyStringWithDestinationEnum = propertyStringWithDestinationEnum;
        }

        private static enum Status {
            BUFFERING,
            PLAYING,
            PAUSED,
            STOPPED
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
        private Direction propertyStringWithDestinationEnum;
        private String propertyEnumWithStringDestination;

        public boolean isPropertyBoolean() {
            return propertyBoolean;
        }

        public void setPropertyBoolean(boolean propertyBoolean) {
            this.propertyBoolean = propertyBoolean;
        }

        public void putPropertyWithNonStandardGetterSetterMethodName(String stuff) {
            this.propertyWithNonStandardGetterSetterMethodName = stuff;
        }

        public String getPropertyCommon() {
            return propertyCommon;
        }

        public void setPropertyCommon(String propertyCommon) {
            this.propertyCommon = propertyCommon;
        }

        public int getPropertyWithDifferentType() {
            return propertyWithDifferentType;
        }

        public void setPropertyWithDifferentType(int propertyWithDifferentType) {
            this.propertyWithDifferentType = propertyWithDifferentType;
        }

        public String getPropertyWithSetterOfDifferentName() {
            return propertyWithSetterOfDifferentName;
        }

        public void setPropertyWithSetterOfDifferentName(String propertyWithSetterOfDifferentName) {
            this.propertyWithSetterOfDifferentName = propertyWithSetterOfDifferentName;
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

        public Direction getPropertyStringWithDestinationEnum() {
            return propertyStringWithDestinationEnum;
        }

        public void setPropertyStringWithDestinationEnum(Direction propertyStringWithDestinationEnum) {
            this.propertyStringWithDestinationEnum = propertyStringWithDestinationEnum;
        }

        public String getPropertyEnumWithStringDestination() {
            return propertyEnumWithStringDestination;
        }

        public void setPropertyEnumWithStringDestination(String propertyEnumWithStringDestination) {
            this.propertyEnumWithStringDestination = propertyEnumWithStringDestination;
        }

        private static enum Direction {
            NORTH,
            SOUTH,
            EAST,
            WEST
        }
    }
}
