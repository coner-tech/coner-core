package org.coner.util.merger;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.*;

public class ReflectionPayloadJavaBeanMerger<S, D> implements ObjectMerger<S, D> {
    private final Direction direction;
    private ImmutableList<MergeOperation<S, D>> mergeOperations;

    public ReflectionPayloadJavaBeanMerger(Direction direction) {
        this.direction = direction;
    }

    public static <S, D> ReflectionPayloadJavaBeanMerger<S, D> javaBeanToPayload() {
        return new ReflectionPayloadJavaBeanMerger<>(Direction.JAVABEAN_TO_PAYLOAD);
    }

    public static <S, D> ReflectionPayloadJavaBeanMerger<S, D> payloadToJavaBean(
    ) {
        return new ReflectionPayloadJavaBeanMerger<>(Direction.PAYLOAD_TO_JAVABEAN);
    }

    @Override
    public void merge(S source, D destination) {
        if (mergeOperations == null) {
            buildMergeOperations(source.getClass(), destination.getClass());
        }

        for (MergeOperation<S, D> mergeOperation : mergeOperations) {
            mergeOperation.execute(source, destination);
        }
    }

    private void buildMergeOperations(Class<?> sourceClass, Class<?> destinationClass) {
        switch (direction) {
            case PAYLOAD_TO_JAVABEAN:
                mergeOperations = buildPayloadToJavaBeanMergeOperations(sourceClass, destinationClass);
                break;
            case JAVABEAN_TO_PAYLOAD:
                mergeOperations = buildJavaBeanToPayloadMergeOperations(sourceClass, destinationClass);
                break;
            default:
                throw new IllegalStateException("unknown direction: " + direction.name());
        }
    }

    private ImmutableList<MergeOperation<S, D>> buildPayloadToJavaBeanMergeOperations(
            Class<?> sourceClass,
            Class<?> destinationClass
    ) {
        ImmutableList.Builder<MergeOperation<S, D>> builder = ImmutableList.builder();
        Field[] sourceFields = sourceClass.getDeclaredFields();
        JavaBeanClassInspector destinationInspector = new JavaBeanClassInspector(destinationClass);
        for (Field sourceField : sourceFields) {
            String sourceFieldName = sourceField.getName();
            if (!destinationInspector.hasFieldWithDirectMutator(sourceFieldName)) {
                continue;
            }
            Method destinationDirectMutator = destinationInspector.getDirectMutatorByFieldName(sourceFieldName);
            Class<?> destinationDirectMutatorType = destinationDirectMutator.getParameters()[0].getType();
            ValueTransformer valueTransformer = null;
            try {
                valueTransformer = ValueTransformerFactory.getValueTransformer(
                        sourceField.getType(),
                        destinationDirectMutatorType
                );
            } catch (UnsupportedOperationException e) {
                continue;
            }
            builder.add(new PayloadToJavaBeanMergeOperation<>(
                    sourceField,
                    destinationDirectMutator,
                    valueTransformer
            ));
        }
        return builder.build();
    }

    private ImmutableList<MergeOperation<S, D>> buildJavaBeanToPayloadMergeOperations(
            Class<?> sourceClass,
            Class<?> destinationClass
    ) {
        ImmutableList.Builder<MergeOperation<S, D>> builder = ImmutableList.builder();
        JavaBeanClassInspector sourceInspector = new JavaBeanClassInspector(sourceClass);
        PayloadClassInspector destinationInspector = new PayloadClassInspector(destinationClass);
        for (String sourceFieldName : sourceInspector.getFieldNamesWithDirectAccessors()) {
            if (!destinationInspector.hasFieldName(sourceFieldName)) {
                continue;
            }
            Method sourceAccessor = sourceInspector.getDirectAccessorByFieldName(sourceFieldName);
            Field destinationField = destinationInspector.getFieldByName(sourceFieldName);
            ValueTransformer valueTransformer = null;
            try {
                valueTransformer = ValueTransformerFactory.getValueTransformer(
                        sourceAccessor.getReturnType(),
                        destinationField.getType()
                );
            } catch (UnsupportedOperationException e) {
                continue;
            }
            builder.add(new JavaBeanToPayloadMergeOperation<>(
                    sourceAccessor,
                    destinationField,
                    valueTransformer
            ));
        }
        return builder.build();
    }

    public enum Direction {
        PAYLOAD_TO_JAVABEAN,
        JAVABEAN_TO_PAYLOAD;
    }

    private abstract static class MergeOperation<S, D> {
        protected final Field payloadField;
        protected final Method javaBeanMethod;

        protected final ValueTransformer valueTransformer;

        private MergeOperation(Field payloadField, Method javaBeanMethod, ValueTransformer valueTransformer) {
            this.payloadField = payloadField;
            this.javaBeanMethod = javaBeanMethod;
            this.valueTransformer = valueTransformer;
        }

        public final void execute(S source, D destination) {
            try {
                Object value = getValue(source);
                if (valueTransformer != null) {
                    value = valueTransformer.transform(value);
                }
                setValue(destination, value);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        protected abstract Object getValue(S source)
                throws IllegalAccessException, InvocationTargetException;

        protected abstract void setValue(D destination, Object value)
                throws InvocationTargetException, IllegalAccessException;

    }

    private static final class PayloadToJavaBeanMergeOperation<S, D> extends MergeOperation<S, D> {

        private PayloadToJavaBeanMergeOperation(
                Field sourcePayloadField,
                Method destinationJavaBeanMutator,
                ValueTransformer valueTransformer
        ) {
            super(sourcePayloadField, destinationJavaBeanMutator, valueTransformer);
        }

        @Override
        protected void setValue(D destination, Object value) throws InvocationTargetException, IllegalAccessException {
            javaBeanMethod.invoke(destination, value);
        }

        @Override
        protected Object getValue(S source) throws IllegalAccessException {
            return payloadField.get(source);
        }

    }

    private static final class JavaBeanToPayloadMergeOperation<S, D> extends MergeOperation<S, D> {

        private JavaBeanToPayloadMergeOperation(
                Method sourceJavaBeanAccessor,
                Field destinationPayloadField,
                ValueTransformer valueTransformer
        ) {
            super(destinationPayloadField, sourceJavaBeanAccessor, valueTransformer);
        }

        @Override
        protected void setValue(D destination, Object value) throws InvocationTargetException, IllegalAccessException {
            payloadField.set(destination, value);
        }

        @Override
        protected Object getValue(S source) throws IllegalAccessException, InvocationTargetException {
            return javaBeanMethod.invoke(source);
        }

    }

}
