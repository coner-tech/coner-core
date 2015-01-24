package org.coner.boundary;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * ReflectionEntityMerger uses reflection to automatically merge entities that follow the JavaBean convention.
 * <p/>
 * Only the destination entity's setters which correspond to the source entity's getters will be called. Only the
 * getters/setters which correspond to like-named properties will be used. Only getters and setters which
 * accept/return the same type as each other will be used.
 *
 * @param <F> source entity class
 * @param <T> destination entity class
 */
public class ReflectionEntityMerger<F, T> implements EntityMerger<F, T> {

    private ImmutableList<SourceDestinationMethodPair> sourceDestinationMethodPairs;
    private EntityMerger<F, T> additionalEntityMerger;

    /**
     * Setter for an additional EntityMerger which could perform any additional merge operations that would otherwise
     * be beyond the ability of the ReflectionEntityMerger to perform. The ReflectionEntityMerger will call the passed
     * EntityMerger's merge method after all reflection-based merge operations have been performed.
     *
     * @param additionalEntityMerger the additional EntityMerger whose merge method will be called after all
     *                               reflection-based merge operations have been performed.
     */
    public void setAdditionalEntityMerger(EntityMerger<F, T> additionalEntityMerger) {
        this.additionalEntityMerger = additionalEntityMerger;
    }

    /**
     * build the sourceDestinationMethodPairs list
     *
     * @param sourceClass      the class of the source entity
     * @param destinationClass the class of the destination entity
     */
    private void buildSourceDestinationMethodPairs(Class<?> sourceClass, Class<?> destinationClass) {
        Method[] allSourceMethods = sourceClass.getMethods();
        Method[] allDestinationMethods = destinationClass.getMethods();

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
                e.printStackTrace();
                continue;
            }
            if (!sourceField.getType().isAssignableFrom(sourceMethod.getReturnType())) {
                // if some type conversion is happening under the covers, we're not using it
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
                e.printStackTrace();
                continue;
            }
            if (!destinationField.getType().isAssignableFrom(destinationMethod.getParameters()[0].getType())) {
                // if some type conversion is happening under the covers, we're not using it
                continue;
            }
            destinationSettersByFieldName.put(destinationFieldName, destinationMethod);
        }

        // build map of source getter and destination setter pairs by source field name
        ImmutableList.Builder<SourceDestinationMethodPair> sourceDestinationMethodPairsBuilder = ImmutableList
                .builder();
        for (String sourceFieldName : sourceGettersByFieldName.keySet()) {
            if (!destinationSettersByFieldName.containsKey(sourceFieldName)) {
                // no destination setter to pair with the source getter
                continue;
            }

            Method sourceGetter = sourceGettersByFieldName.get(sourceFieldName);
            Method destinationSetter = destinationSettersByFieldName.get(sourceFieldName);

            if (!destinationSetter.getParameters()[0].getType().isAssignableFrom(sourceGetter.getReturnType())) {
                // source getter return type doesn't match destination setter type, skip
                continue;
            }

            sourceDestinationMethodPairsBuilder.add(new SourceDestinationMethodPair(
                    sourceGetter,
                    destinationSetter
            ));
        }
        sourceDestinationMethodPairs = sourceDestinationMethodPairsBuilder.build();
    }

    @Override
    public final void merge(F sourceEntity, T destinationEntity) {
        if (sourceDestinationMethodPairs == null) {
            buildSourceDestinationMethodPairs(sourceEntity.getClass(), destinationEntity.getClass());
        }

        for (SourceDestinationMethodPair sourceDestinationMethodPair : sourceDestinationMethodPairs) {
            try {
                Object sourceValue = sourceDestinationMethodPair.sourceMethod.invoke(sourceEntity);
                sourceDestinationMethodPair.destinationMethod.invoke(destinationEntity, sourceValue);
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
    private static class SourceDestinationMethodPair {
        final Method sourceMethod;
        final Method destinationMethod;

        /**
         * Private constructor accepting and assigning both the sourceMethod and destinationMethod
         *
         * @param sourceMethod      the sourceMethod
         * @param destinationMethod the destinationMethod
         */
        private SourceDestinationMethodPair(Method sourceMethod, Method destinationMethod) {
            this.sourceMethod = sourceMethod;
            this.destinationMethod = destinationMethod;
        }
    }
}
