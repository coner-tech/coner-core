package org.coner.util.merger;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.*;
import java.util.HashMap;

/**
 * ReflectionObjectMerger uses reflection to automatically merge objects that follow the JavaBean convention.
 * <p>
 * Only the destination object's setters which correspond to the source object's getters will be called. Only the
 * getters/setters which correspond to like-named properties will be used. Strings corresponding to Enum names will be
 * transformed automatically.
 *
 * @param <S> source JavaBean class
 * @param <D> destination JavaBean class
 */
public class ReflectionJavaBeanMerger<S, D> implements ObjectMerger<S, D> {

    private final ObjectMerger<S, D> additionalMerger;
    private ImmutableList<MergeOperation> mergeOperations;

    /**
     * Construct a ReflectionJavaBeanMerger with default functionality.
     */
    public ReflectionJavaBeanMerger() {
        this.additionalMerger = null;
    }

    /**
     * Construct a ReflectionJavaBeanMerger with an additional ObjectMerger. This is useful in case there are some
     * differences in class design that ReflectionJavaBeanMerger can't reasonably account for, such as different
     * property names or data types. You might use this in case your classes are mostly similar with only a couple
     * exceptions. However, if your objects diverge much, you may be better off implementing a custom ObjectMerger
     * which handles the entire merge itself.
     *
     * @param additionalMerger an additional ObjectMerger which will be called after reflection-based merge
     *                         operations have been performed
     */
    public ReflectionJavaBeanMerger(ObjectMerger<S, D> additionalMerger) {
        this.additionalMerger = additionalMerger;
    }

    /**
     * Build the sourceDestinationMethodPairs list.
     *
     * @param sourceClass      the class of the source
     * @param destinationClass the class of the destination
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

    private boolean shouldUseStringToEnumValueTransformer(
            Class<?> sourceGetterReturnType,
            Class<?> destinationSetterParameter0Type
    ) {
        return sourceGetterReturnType == String.class && Enum.class.isAssignableFrom(destinationSetterParameter0Type);
    }

    private boolean shouldUseEnumToStringValueTransformer(
            Class<?> sourceGetterReturnType,
            Class<?> destinationSetterParameter0Type
    ) {
        return Enum.class.isAssignableFrom(sourceGetterReturnType) && destinationSetterParameter0Type == String.class;
    }

    @Override
    public final void merge(S source, D destination) {
        if (mergeOperations == null) {
            buildSourceDestinationMethodPairs(source.getClass(), destination.getClass());
        }

        for (MergeOperation mergeOperation : mergeOperations) {
            try {
                Object value = mergeOperation.sourceMethod.invoke(source);
                if (mergeOperation.valueTransformer != null) {
                    value = mergeOperation.valueTransformer.transform(value);
                }
                mergeOperation.destinationMethod.invoke(destination, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        if (additionalMerger != null) {
            additionalMerger.merge(source, destination);
        }
    }

    private static final class MergeOperation {
        private final Method sourceMethod;
        private final Method destinationMethod;
        private final ValueTransformer valueTransformer;

        private MergeOperation(Method sourceMethod, Method destinationMethod, ValueTransformer valueTransformer) {
            this.sourceMethod = sourceMethod;
            this.destinationMethod = destinationMethod;
            this.valueTransformer = valueTransformer;
        }
    }

}
