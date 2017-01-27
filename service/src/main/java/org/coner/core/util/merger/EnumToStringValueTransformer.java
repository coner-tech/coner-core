package org.coner.core.util.merger;

public class EnumToStringValueTransformer implements ValueTransformer {

    public static boolean areEligible(Class<?> sourceType, Class<?> destinationType) {
        return Enum.class.isAssignableFrom(sourceType) && destinationType == String.class;
    }

    @Override
    public Object transform(Object value) {
        Enum<?> enumValue = (Enum<?>) value;
        return enumValue.name();
    }
}
