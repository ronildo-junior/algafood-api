package com.ronijr.algafoodapi.api.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.ronijr.algafoodapi.api.exception.InvalidModelParseException;
import lombok.experimental.UtilityClass;
import org.springframework.util.ReflectionUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public final class MapperUtils {
    @SuppressWarnings("unchecked")
    public static Object mergeFieldsMapInObject(Map<String, Object> fieldsMap, Object target)
            throws InvalidModelParseException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            fieldsMap.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(target.getClass(), key);
                if (field != null) {
                    if (value instanceof Map) {
                        invokeSetter(target, field.getName(),
                                mergeFieldsMapInObject((Map<String, Object>) value, invokeGetter(target, key)));
                    } else {
                        invokeSetter(target, field.getName(), parseValue(value, field.getType()));
                    }
                }
            });
            return target;
        } catch (IllegalArgumentException e) {
            throw new InvalidModelParseException(
                    String.format("%s is not acceptable.",
                            ((PropertyBindingException) e.getCause()).getPath().stream().
                                    map(JsonMappingException.Reference::getFieldName).
                                    collect(Collectors.joining("."))));
        }
    }

    public static List<String> getClassFieldNames(Class<?> representation) {
        List<String> fields = Arrays.stream(representation.getSuperclass().getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
        fields.addAll(Arrays.stream(representation.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList()));
        return fields;
    }

    public static void verifyMapContainsOnlyFieldsOfClass(Map<String, Object> map, Class<?> representation) {
        List<String> fields = getClassFieldNames(representation);
        List<String> fieldsInvalid = new ArrayList<>();
        map.forEach((key, value) -> {
            if (!fields.contains(key)) {
                fieldsInvalid.add(key);
            }
        });
        if (!fieldsInvalid.isEmpty()) {
            throw new InvalidModelParseException(
                    String.format("%s is not acceptable.", (String.join(",", fieldsInvalid))));
        }
    }

    private static Object parseValue(Object value, Class<?> type) {
        if (type == Long.class && value instanceof Integer)  {
            return ((Integer) value).longValue();
        }
        return value;
    }

    private static void invokeSetter(Object object, String fieldName, Object value) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(fieldName, object.getClass());
            descriptor.getWriteMethod().invoke(object, value);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static Object invokeGetter(Object object, String fieldName) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(fieldName, object.getClass());
            return descriptor.getReadMethod().invoke(object);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}