package com.ronijr.algafoodapi.config.documentation;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger.schema.ApiModelTypeNameProvider;

/** Custom naming for class names. */
@Configuration
public class QualifiedTypeNameResolver extends ApiModelTypeNameProvider {
    @Override
    public String nameFor(Class<?> type) {
        return type.getTypeName().
                replace(type.getPackageName() + ".", "").
                replace("$", "");
    }
}