package com.ronijr.algafoodapi.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class ResourceUtils {
    public static String getContentFromResource(String resourceName) {
        try (InputStream stream = ResourceUtils.class.getResourceAsStream(resourceName)) {
            return StreamUtils.copyToString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Object getObjectFromJson(String resourceName, Class<?> target) {
        try {
            return new ObjectMapper().readValue(getContentFromResource(resourceName), target);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
