package org.coner.boundary;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * ReflectionEntityMerger uses reflection to automatically merge entities that follow the JavaBean convention.
 * <p/>
 * Only the destination entity's setters which correspond to the source entity's getters will be called. Only the
 * getters/setters which correspond to like-named properties will be used. Strings corresponding to Enum names will be
 * transformed automatically.
 *
 * @param <F> source entity class
 * @param <T> destination entity class
 */
public class ReflectionEntityMerger<F, T> implements EntityMerger<F, T> {

    private final EntityMerger<F, T> additionalEntityMerger;
    private ImmutableList<MergeOperation> mergeOperations;

    /**
     * Construct a ReflectionEntityMerger with default functionality
     */
    public ReflectionEntityMerger() {
        this.additionalEntityMerger = null;
    }

    /**
     * Construct a ReflectionEntityMerger with an additional EntityMerger
     *
     * @param additionalEntityMerger an additional EntityMerger which will be called after reflection-based merge
     *                               operations have been performed
     */
    public ReflectionEntityMerger(EntityMerger<F, T> additionalEntityMerger) {
        this.additionalEntityMerger = additionalEntityMerger;
    }

    /**
     * build the sourceDestinationMethodPairs list
     *
     * @param sourceClass      the class of the source entity
     * @param destinationClass the class of the destination entity
     */
    private void buildSourceDestinationMethodPairs(Class<?> sourceClass, Class<?> destinationClass) {
        Method[] allSourceMethods = sourceClass.getDeclaredMethods();
        Method[] allDestinationMethods = destinationClass.getDeclaredMethods();

        // build map of source getters by field name
        HashMap<String, Method> sourceGettersByFieldName = new HashMap<>();
        for (Method sourceMethod : allSourceMethods) {
            boolean sourceMethodStartsWithGet = sourceMethod.getName().startsWith("get");
            boolean sourceMethodStartsWithIs = sourceMethod.getName().startsWith("is");
            if (!sourceMethodStartsWithGet && !sourceMethodStartsWithIs) {
                // if it doesn't start with "get" or "is" it's not a getter
                continue;
            }
            if (sourceMethod.getParameterCount() > 0) {
                // if it has parameters it's not a getter
                continue;
            }
            String sourceFieldName;
            if (sourceMethodStartsWithGet) {
                sourceFieldName = new StringBuilder(sourceMethod.getName().substring(3, 4).toLowerCase())
                        .append(sourceMethod.getName().substring(4))
                        .toString();
            } else if (sourceMethodStartsWithIs) {
                sourceFieldName = new StringBuilder(sourceMethod.getName().substring(2, 3).toLowerCase())
                        .append(sourceMethod.getName().substring(3))
                        .toString();
            } else {
                throw new IllegalStateException(
                        "should never reach here unless acceptable getter name prefixes expand beyond \"get\", \"is\""
                );
            }
            Field sourceField;
            try {
                sourceField = sourceClass.getDeclaredField(sourceFieldName);
            } catch (NoSuchFieldException e) {
                // if it doesn't have a field matching the same name, we're not using it
                continue;
            }
            sourceGettersByFieldName.put(sourceFieldName, sourceMethod);
        }

        // build map of destination setters by field name
        HashMap<String, Method> destinationSettersByFieldName = new HashMap<>();
        for (Method destinationMethod : allDestinationMethods) {
            if (!destinationMethod.getName().startsWith("set")) {
                // if it doesn't start with "set" it's not a setter
                continue;
            }
            if (destinationMethod.getParameterCount() != 1) {
                // if it doesn't have 1 parameter it's not a setter
                continue;
            }
            String destinationFieldName = new StringBuilder(destinationMethod.getName().substring(3, 4).toLowerCase())
                    .append(destinationMethod.getName().substring(4))
                    .toString();
            Field destinationField;
            try {
                destinationField = destinationClass.getDeclaredField(destinationFieldName);
            } catch (NoSuchFieldException e) {
                // if it doesn't have a field matching the same name, we're not using it
                continue;
            }
            destinationSettersByFieldName.put(destinationFieldName, destinationMethod);
        }

        // build map of source getter and destination setter pairs by source field name
        ImmutableList.Builder<MergeOperation> mergeOperationsBuilder = ImmutableList
                .builder();
        for (String sourceFieldName : sourceGettersByFieldName.keySet()) {
            ValueTransformer valueTransformer = null;
            if (!destinationSettersByFieldName.containsKey(sourceFieldName)) {
                // no destination setter to pair with the source getter
                continue;
            }

            Method sourceGetter = sourceGettersByFieldName.get(sourceFieldName);
            Method destinationSetter = destinationSettersByFieldName.get(sourceFieldName);

            if (!destinationSetter.getParameters()[0].getType().isAssignableFrom(sourceGetter.getReturnType())) {
                // source getter return type doesn't match destination setter type.

                Class<?> sourceGetterReturnType = sourceGetter.getReturnType();
                Class<?> destinationSetterParameter0Type = destinationSetter.getParameterTypes()[0];
                if (shouldUseStringToEnumValueTransformer(sourceGetterReturnType, destinationSetterParameter0Type)) {
                    valueTransformer = new StringToEnumValueTransformer(
                            (Class<? extends Enum>) destinationSetter.getParameterTypes()[0]
                    );
                } else if (shouldUseEnumToStringValueTransformer(
                        sourceGetterReturnType,
                        destinationSetterParameter0Type)) {
                    valueTransformer = new EnumToStringValueTransformer();
                } else {
                    // not supported, skip
                    continue;
                }

            }

            mergeOperationsBuilder.add(new MergeOperation(
                    sourceGetter,
                    destinationSetter,
                    valueTransformer
            ));
        }
        mergeOperations = mergeOperationsBuilder.build();
    }

    /**
     * Check whether a MergeOperation should use a StringToEnumValueTransformer
     *
     * @param sourceGetterReturnType          the type returned by the source getter
     * @param destinationSetterParameter0Type the type of the first parameter of the destination setter
     * @return true if should or false if shouldn't
     */
    private boolean shouldUseStringToEnumValueTransformer(
            Class<?> sourceGetterReturnType,
            Class<?> destinationSetterParameter0Type
    ) {
        return sourceGetterReturnType == String.class && Enum.class.isAssignableFrom(destinationSetterParameter0Type);
    }

    /**
     * Check whether a MergeOperation should use an EnumToStringValueTransformer
     *
     * @param sourceGetterReturnType          the type returned by the source getter
     * @param destinationSetterParameter0Type the type of the first parameter of the destination setter
     * @return true if should or false if shouldn't
     */
    private boolean shouldUseEnumToStringValueTransformer(
            Class<?> sourceGetterReturnType,
            Class<?> destinationSetterParameter0Type
    ) {
        return Enum.class.isAssignableFrom(sourceGetterReturnType) && destinationSetterParameter0Type == String.class;
    }

    @Override
    public final void merge(F sourceEntity, T destinationEntity) {
        if (mergeOperations == null) {
            buildSourceDestinationMethodPairs(sourceEntity.getClass(), destinationEntity.getClass());
        }

        for (MergeOperation mergeOperation : mergeOperations) {
            try {
                Object value = mergeOperation.sourceMethod.invoke(sourceEntity);
                if (mergeOperation.valueTransformer != null) {
                    value = mergeOperation.valueTransformer.transform(value);
                }
                mergeOperation.destinationMethod.invoke(destinationEntity, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        if (additionalEntityMerger != null) {
            additionalEntityMerger.merge(sourceEntity, destinationEntity);
        }
    }

    /**
     * A SourceDestinationMethodPair is a tuple which pairs a getter method from the source entity class and a setter
     * method from the destination entity class. The merge implementation
     */
    private static class MergeOperation {
        final Method sourceMethod;
        final Method destinationMethod;
        final ValueTransformer valueTransformer;

        /**
         * Private constructor accepting and assigning both the sourceMethod and destinationMethod
         *
         * @param sourceMethod      the sourceMethod
         * @param destinationMethod the destinationMethod
         * @param valueTransformer  the valueTransformer
         */
        private MergeOperation(Method sourceMethod, Method destinationMethod, ValueTransformer valueTransformer) {
            this.sourceMethod = sourceMethod;
            this.destinationMethod = destinationMethod;
            this.valueTransformer = valueTransformer;
        }
    }

    /**
     * A ValueTransformer accepts a value and creates a transformed representation of the value
     */
    private interface ValueTransformer {
        /**
         * Transform an input value and return the transformed value
         *
         * @param value the input value
         * @return the transformed value
         */
        Object transform(Object value);
    }

    /**
     * StringToEnumValueTransformer transforms a String value containing an Enum name-value to the instance of the
     * Enum name-value.
     */
    private static class StringToEnumValueTransformer implements ValueTransformer {
        private final Class<? extends Enum> destinationEnumType;

        /**
         * @param destinationEnumType the destinationEnumType
         */
        public StringToEnumValueTransformer(Class<? extends Enum> destinationEnumType) {
            this.destinationEnumType = destinationEnumType;
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

    /**
     * EnumToStringValueTransformer transforms an Enum value to a String containing the name
     */
    private static class EnumToStringValueTransformer implements ValueTransformer {

        @Override
        public Object transform(Object value) {
            Enum<?> enumValue = (Enum<?>) value;
            return enumValue.name();
        }
    }
}
