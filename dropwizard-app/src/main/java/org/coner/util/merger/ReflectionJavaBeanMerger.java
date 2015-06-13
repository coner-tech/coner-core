package org.coner.util.merger;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.*;

/**
 * ReflectionJavaBeanMerger uses reflection to automatically merge objects that follow the JavaBean convention.
 * <p>
 * Only the destination object's setters which correspond to the source object's getters will be called. Only the
 * getters/setters which correspond to like-named properties will be used. Strings corresponding to Enum names will be
 * transformed automatically.
 *
 * @param <S> source JavaBean class
 * @param <D> destination JavaBean class
 */
public class ReflectionJavaBeanMerger<S, D> implements ObjectMerger<S, D> {

    private ImmutableList<MergeOperation> mergeOperations;

    public ReflectionJavaBeanMerger() {
    }

    /**
     * Build the sourceDestinationMethodPairs list.
     *
     * @param sourceClass      the class of the source
     * @param destinationClass the class of the destination
     */
    private void buildMergeOperations(Class<?> sourceClass, Class<?> destinationClass) {
        JavaBeanClassInspector sourceClassInspector = new JavaBeanClassInspector(sourceClass);

        JavaBeanClassInspector destinationClassInspector = new JavaBeanClassInspector(destinationClass);

        // build map of source getter and destination setter pairs by source field name
        ImmutableList.Builder<MergeOperation> mergeOperationsBuilder = ImmutableList.builder();
        for (String sourceFieldName : sourceClassInspector.getFieldNamesWithDirectAccessors()) {
            if (!destinationClassInspector.hasFieldWithDirectMutator(sourceFieldName)) {
                // no destination setter to pair with the source getter
                continue;
            }

            Method sourceAccessor = sourceClassInspector.getDirectAccessorByFieldName(sourceFieldName);
            Method destinationMutator = destinationClassInspector.getDirectMutatorByFieldName(sourceFieldName);

            Class<?> sourceAccessorReturnType = sourceAccessor.getReturnType();
            Class<?> destinationMutatorParameterType = destinationMutator.getParameterTypes()[0];
            ValueTransformer valueTransformer = null;
            try {
                valueTransformer = ValueTransformerFactory.getValueTransformer(
                        sourceAccessorReturnType,
                        destinationMutatorParameterType
                );
            } catch (UnsupportedOperationException e) {
                continue;
            }

            mergeOperationsBuilder.add(new MergeOperation(
                    sourceAccessor,
                    destinationMutator,
                    valueTransformer
            ));
        }
        mergeOperations = mergeOperationsBuilder.build();
    }

    @Override
    public final void merge(S source, D destination) {
        if (mergeOperations == null) {
            buildMergeOperations(source.getClass(), destination.getClass());
        }

        for (MergeOperation mergeOperation : mergeOperations) {
            mergeOperation.execute(source, destination);
        }
    }

    private static final class MergeOperation<S, D> {
        private final Method sourceAccessor;
        private final Method destinationMutator;
        private final ValueTransformer valueTransformer;

        private MergeOperation(Method sourceAccessor, Method destinationMutator, ValueTransformer valueTransformer) {
            this.sourceAccessor = sourceAccessor;
            this.destinationMutator = destinationMutator;
            this.valueTransformer = valueTransformer;
        }

        private void execute(S source, D destination) {
            try {
                Object value = sourceAccessor.invoke(source);
                if (valueTransformer != null) {
                    value = valueTransformer.transform(value);
                }
                destinationMutator.invoke(destination, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

}
