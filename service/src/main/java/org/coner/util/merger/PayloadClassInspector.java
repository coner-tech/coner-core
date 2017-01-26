package org.coner.util.merger;

import java.lang.reflect.Field;

import com.google.common.collect.ImmutableMap;

public final class PayloadClassInspector {

    private final Class<?> payloadClass;
    private ImmutableMap<String, Field> fieldNamesToFields;

    public PayloadClassInspector(Class<?> payloadClass) {
        this.payloadClass = payloadClass;
    }

    public boolean hasFieldName(String fieldName) {
        if (fieldNamesToFields == null) {
            buildFieldNamesToFields();
        }
        return fieldNamesToFields.containsKey(fieldName);
    }

    public Field getFieldByName(String fieldName) {
        if (fieldNamesToFields == null) {
            buildFieldNamesToFields();
        }
        return fieldNamesToFields.get(fieldName);
    }

    private void buildFieldNamesToFields() {
        ImmutableMap.Builder<String, Field> builder = ImmutableMap.builder();
        for (Field field : payloadClass.getDeclaredFields()) {
            builder.put(field.getName(), field);
        }
        fieldNamesToFields = builder.build();
    }

}
