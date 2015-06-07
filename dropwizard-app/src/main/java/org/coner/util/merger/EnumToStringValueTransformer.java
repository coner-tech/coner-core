package org.coner.util.merger;

/**
 *
 */
public class EnumToStringValueTransformer implements ValueTransformer {

    @Override
    public Object transform(Object value) {
        Enum<?> enumValue = (Enum<?>) value;
        return enumValue.name();
    }
}
