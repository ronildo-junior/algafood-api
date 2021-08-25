package com.ronijr.algafoodapi.config.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronijr.algafoodapi.api.exception.InvalidModelParseException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

public final class AppUtils {
    private AppUtils() {}

    public static void mergeFieldsMapInObject(Map<String, Object> fieldsMap, Object target)
            throws InvalidModelParseException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object mapObject = objectMapper.convertValue(fieldsMap, target.getClass());
            fieldsMap.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(target.getClass(), key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, target, ReflectionUtils.getField(field, mapObject));
                }
            });
        } catch (IllegalArgumentException e) {
            throw new InvalidModelParseException(e.getMessage());
        }
    }
}