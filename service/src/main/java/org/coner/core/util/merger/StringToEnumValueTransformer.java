package org.coner.core.util.merger;

import com.google.common.base.Strings;

public class StringToEnumValueTransformer implements ValueTransformer {
    private final Class<? extends Enum> destinationEnumType;

    public StringToEnumValueTransformer(Class<? extends Enum> destinationEnumType) {
        this.destinationEnumType = destinationEnumType;
    }

    public static boolean areEligible(Class<?> sourceType, Class<?> destinationType) {
        return sourceType == String.class && Enum.class.isAssignableFrom(destinationType);
    }

    @Override
    public Object transform(Object value) {
        String stringValue = (String) value;
        if (Strings.isNullOrEmpty(stringValue)) {
            return null;
        }
        try {
            return Enum.valueOf(destinationEnumType, stringValue);
        } catch (IllegalArgumentException e) {
            // the destinationEnumType does not contain an Enum constant with the name contained in stringValue
            return null;
        }
    }
}
