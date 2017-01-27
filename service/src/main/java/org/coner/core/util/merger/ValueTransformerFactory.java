package org.coner.core.util.merger;

public final class ValueTransformerFactory {

    private ValueTransformerFactory() {
    }

    public static ValueTransformer getValueTransformer(Class<?> sourceType, Class<?> destinationType)
            throws UnsupportedOperationException {
        ValueTransformer valueTransformer = null;
        if (destinationType.isAssignableFrom(sourceType)) {
            valueTransformer = null;
        } else if (StringToEnumValueTransformer.areEligible(sourceType, destinationType)) {
            valueTransformer = new StringToEnumValueTransformer((Class<? extends Enum>) destinationType);
        } else if (EnumToStringValueTransformer.areEligible(sourceType, destinationType)) {
            valueTransformer = new EnumToStringValueTransformer();
        } else {
            throw new UnsupportedOperationException();
        }
        return valueTransformer;
    }

}
