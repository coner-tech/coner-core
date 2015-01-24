package org.coner.boundary;

import com.google.common.collect.ImmutableList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 */
public class ReflectionEntityMerger<F, T> implements EntityMerger<F, T> {

    private ImmutableList<SourceDestinationMethodPair> sourceDestinationMethodPairs;

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
        ImmutableList.Builder<SourceDestinationMethodPair> sourceDestinationMethodPairsBuilder = ImmutableList.builder();
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
    public void merge(F sourceEntity, T destinationEntity) {
        if (sourceDestinationMethodPairs == null) {
            buildSourceDestinationMethodPairs(sourceEntity.getClass(), destinationEntity.getClass());
        }

        for (SourceDestinationMethodPair sourceDestinationMethodPair : sourceDestinationMethodPairs) {
            Object sourceValue;
            try {
                // call source getter
                sourceValue = sourceDestinationMethodPair.sourceMethod.invoke(sourceEntity);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            try {
                // call destination setter
                sourceDestinationMethodPair.destinationMethod.invoke(destinationEntity, sourceValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
            }
        }
    }

    private static class SourceDestinationMethodPair {
        final Method sourceMethod;
        final Method destinationMethod;

        private SourceDestinationMethodPair(Method sourceMethod, Method destinationMethod) {
            this.sourceMethod = sourceMethod;
            this.destinationMethod = destinationMethod;
        }
    }
}
