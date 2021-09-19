package com.ronijr.algafoodapi.config.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.jackson.TypeNameResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/** Custom naming for class names. */
@Configuration
public class CustomConverter extends ModelResolver {
    public static void add(ObjectMapper objectMapper)
    {
        ModelConverters.getInstance().addConverter(new CustomConverter(objectMapper));
    }
    public CustomConverter(ObjectMapper mapper) {
        super(mapper, new QualifiedTypeNameResolver());
    }

    static class QualifiedTypeNameResolver extends TypeNameResolver {
        @Override
        protected String nameForClass(Class<?> type, Set<Options> options) {
            String className = type.getTypeName().
                    replace(type.getPackageName() + ".", "").
                    replace("$", "");
            if (options.contains(Options.SKIP_API_MODEL)) {
                return className;
            }
            final io.swagger.v3.oas.annotations.media.Schema model = type.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
            final String modelName = model == null ? null : StringUtils.trimToNull(model.name());
            return modelName == null ? className : modelName;
        }
    }
}