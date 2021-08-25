package com.ronijr.algafoodapi.api.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.ronijr.algafoodapi.api.exception.InvalidModelParseException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

public final class MapperUitls {
    private MapperUitls() {}

    public static void mergeFieldsMapInObject(Map<String, Object> fieldsMap, Object target)
            throws InvalidModelParseException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            Object mapObject = objectMapper.convertValue(fieldsMap, target.getClass());
            fieldsMap.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(target.getClass(), key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, target, ReflectionUtils.getField(field, mapObject));
                }
            });
        } catch (IllegalArgumentException e) {
            throw new InvalidModelParseException(
                    String.format("The property '%s' is not acceptable.",
                            ((PropertyBindingException) e.getCause()).getPath().
                                    stream().
                                    map(JsonMappingException.Reference::getFieldName).
                                    collect(Collectors.joining("."))));
        }
    }
}