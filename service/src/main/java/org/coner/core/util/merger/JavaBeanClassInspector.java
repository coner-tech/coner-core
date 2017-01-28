package org.coner.core.util.merger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public final class JavaBeanClassInspector {

    private final Class<?> javaBeanClass;
    private ImmutableMap<String, Method> fieldNamesToDirectAccessors;
    private Map<Method, AccessorType> accessorMethodsToAccessorTypes;
    private ImmutableMap<String, Method> fieldNamesToDirectMutators;

    public JavaBeanClassInspector(Class<?> javaBeanClass) {
        this.javaBeanClass = javaBeanClass;
    }

    public ImmutableSet<String> getFieldNamesWithDirectAccessors() {
        if (fieldNamesToDirectAccessors == null) {
            buildFieldNamesToDirectAccessors();
        }
        return fieldNamesToDirectAccessors.keySet();
    }

    public Method getDirectAccessorByFieldName(String name) {
        if (fieldNamesToDirectAccessors == null) {
            buildFieldNamesToDirectAccessors();
        }
        return fieldNamesToDirectAccessors.get(name);
    }

    public Method getDirectMutatorByFieldName(String name) {
        if (fieldNamesToDirectMutators == null) {
            buildFieldNamesToDirectMutators();
        }
        return fieldNamesToDirectMutators.get(name);
    }

    public boolean hasFieldWithDirectMutator(String fieldName) {
        if (fieldNamesToDirectMutators == null) {
            buildFieldNamesToDirectMutators();
        }
        return fieldNamesToDirectMutators.containsKey(fieldName);
    }

    private void buildFieldNamesToDirectAccessors() {
        Method[] allMethods = javaBeanClass.getDeclaredMethods();
        ImmutableMap.Builder<String, Method> mapBuilder = ImmutableMap.builder();
        for (Method method : allMethods) {
            if (!isJavaBeanAccessor(method)) {
                continue;
            }
            String predictedFieldName = buildPredictedFieldNameForDirectJavaBeanAccessor(method);
            if (!isDirectJavaBeanAccessor(method)) {
                continue;
            }
            mapBuilder.put(predictedFieldName, method);
        }
        fieldNamesToDirectAccessors = mapBuilder.build();
    }

    private void buildFieldNamesToDirectMutators() {
        Method[] allMethods = javaBeanClass.getDeclaredMethods();
        ImmutableMap.Builder<String, Method> mapBuilder = ImmutableMap.builder();
        for (Method method : allMethods) {
            if (!isJavaBeanMutator(method)) {
                continue;
            }
            String predictedFieldName = buildPredictedFieldNameForDirectJavaBeanMutator(method);
            if (!isDirectJavaBeanMutator(method)) {
                continue;
            }
            mapBuilder.put(predictedFieldName, method);
        }
        fieldNamesToDirectMutators = mapBuilder.build();
    }

    private boolean isJavaBeanAccessor(Method method) {
        if (accessorMethodsToAccessorTypes == null) {
            accessorMethodsToAccessorTypes = new HashMap<>(javaBeanClass.getDeclaredMethods().length);
        }
        if (accessorMethodsToAccessorTypes.containsKey(method)) {
            return true;
        } else if (method.getParameterCount() != 0) {
            return false;
        } else if (method.getName().startsWith("get")) {
            accessorMethodsToAccessorTypes.put(method, AccessorType.GET);
            return true;
        } else if (method.getName().startsWith("is")) {
            accessorMethodsToAccessorTypes.put(method, AccessorType.IS);
            return true;
        }
        return false;
    }

    private boolean isJavaBeanMutator(Method method) {
        return method.getName().startsWith("set") && method.getParameterCount() == 1;
    }

    private boolean isDirectJavaBeanAccessor(Method method) {
        if (!isJavaBeanAccessor(method)) {
            return false;
        }

        String predictedFieldName = buildPredictedFieldNameForDirectJavaBeanAccessor(method);
        try {
            javaBeanClass.getDeclaredField(predictedFieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private boolean isDirectJavaBeanMutator(Method method) {
        if (!isJavaBeanMutator(method)) {
            return false;
        }

        String predictedFieldName = buildPredictedFieldNameForDirectJavaBeanMutator(method);
        try {
            javaBeanClass.getDeclaredField(predictedFieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private String buildPredictedFieldNameForDirectJavaBeanAccessor(Method method) {
        AccessorType accessorType = accessorMethodsToAccessorTypes.get(method);
        String fieldName;
        String methodName = method.getName();
        switch (accessorType) {
            case GET:
                fieldName = new StringBuilder(methodName.substring(3, 4).toLowerCase())
                        .append(methodName.substring(4))
                        .toString();
                break;
            case IS:
                fieldName = new StringBuilder(methodName.substring(2, 3).toLowerCase())
                        .append(methodName.substring(3))
                        .toString();
                break;
            default:
                throw new IllegalStateException();
        }
        return fieldName;
    }

    private String buildPredictedFieldNameForDirectJavaBeanMutator(Method method) {
        return new StringBuilder(method.getName().substring(3, 4).toLowerCase())
                .append(method.getName().substring(4))
                .toString();
    }

    private enum AccessorType {
        GET,
        IS
    }

}
