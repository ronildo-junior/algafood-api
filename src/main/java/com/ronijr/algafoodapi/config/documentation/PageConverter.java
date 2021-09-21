package com.ronijr.algafoodapi.config.documentation;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Iterator;

public class PageConverter implements ModelConverter {
    private static final Class<?> CLASS_TO_REPLACE = org.springframework.data.domain.Page.class;
    private static final Class<?> CLASS_FROM_REPLACE = com.ronijr.algafoodapi.config.documentation.model.PageSummary.class;

    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        JavaType javaType = Json.mapper().constructType(type.getType());
        if (javaType != null) {
            Class<?> cls = javaType.getRawClass();
            if (CLASS_TO_REPLACE.getTypeName().equals(cls.getCanonicalName())) {
                ParameterizedType oldType = (ParameterizedType) type.getType();
                ParameterizedType newType = TypeUtils.parameterize(CLASS_FROM_REPLACE, oldType.getActualTypeArguments());
                type = new AnnotatedType()
                        .type(newType)
                        .ctxAnnotations(type.getCtxAnnotations())
                        .parent(type.getParent())
                        .schemaProperty(type.isSchemaProperty())
                        .name(type.getName())
                        .resolveAsRef(type.isResolveAsRef())
                        .jsonViewAnnotation(type.getJsonViewAnnotation())
                        .propertyName(type.getPropertyName())
                        .skipOverride(true);

                return this.resolve(type, context, chain);
            }
        }
        return (chain.hasNext()) ? chain.next().resolve(type, context, chain) : null;
    }
}