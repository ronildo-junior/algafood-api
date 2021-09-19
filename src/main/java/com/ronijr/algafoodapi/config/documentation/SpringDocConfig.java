package com.ronijr.algafoodapi.config.documentation;

import com.ronijr.algafoodapi.api.exception.handler.ProblemDetails;
import com.ronijr.algafoodapi.config.documentation.model.ControllerTag;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SpringDocConfig implements OperationCustomizer {
    private final List<Integer> problemStatuses = Arrays.asList(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.UNPROCESSABLE_ENTITY.value()
    );

    @Bean
    public OpenAPI springShopOpenAPI() {
        OpenAPI openAPI = new OpenAPI().info(this.getInfo());
        addTags(openAPI);
        return openAPI;
    }

    @Bean
    public OpenApiCustomiser customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> {
            addProblemResponse(openApi);

            openApi.getPaths().values().forEach(
                    pathItem -> pathItem.readOperations().forEach(
                            operation -> {
                                ApiResponses apiResponses = operation.getResponses();
                                changeOkToCreatedStatusInPost(operation, pathItem, apiResponses);
                                addContentResponse(apiResponses);
                            }
                    )
            );
        };
    }

    /** Assign Tags in Methods */
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ControllerTag tag = ControllerTag.findByType(handlerMethod.getBeanType().getSimpleName()).stream().
                findAny().
                orElseThrow(() ->
                        new EnumConstantNotPresentException(ControllerTag.class,
                                handlerMethod.getBeanType().getSimpleName()));
        operation.getTags().clear();
        operation.addTagsItem(tag.getTitle());
        return operation;
    }

    /** Replace Response Status 200 by 201 in POST Methods */
    private void changeOkToCreatedStatusInPost(Operation operation, PathItem pathItem, ApiResponses apiResponses) {
        if (operation.equals(pathItem.getPost()) && apiResponses.containsKey("200")) {
            apiResponses.put("201", apiResponses.remove("200"));
        }
    }

    private void addContentResponse(ApiResponses apiResponses) {
        apiResponses.forEach((statusCode, apiResponse) -> {
            if (problemStatuses.contains(Integer.valueOf(statusCode))) {
                addProblemResponse(apiResponse);
            }
        });
    }

    private void addProblemResponse(ApiResponse apiResponse) {
        Schema<ProblemDetails> schema = new Schema<>();
        schema.setName("ProblemDetails");
        schema.set$ref("#/components/schemas/ProblemDetails");
        MediaType mediaType = new MediaType();
        mediaType.schema(schema);
        apiResponse.content(
                new Content().addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        mediaType));
    }

    /** Add Problem Details Model */
    private void addProblemResponse(OpenAPI openAPI) {
        var schemas = openAPI.getComponents().getSchemas();
        schemas.putAll(ModelConverters.getInstance().read(ProblemDetails.class));
        schemas.putAll(ModelConverters.getInstance().read(ProblemDetails.Object.class));
    }

    /** Create tags */
    private void addTags(OpenAPI openAPI) {
        openAPI.addTagsItem(ControllerTag.CITY.getTag())
                .addTagsItem(ControllerTag.CUISINE.getTag())
                .addTagsItem(ControllerTag.ORDER.getTag())
                .addTagsItem(ControllerTag.PAYMENT_METHOD.getTag())
                .addTagsItem(ControllerTag.PRODUCT.getTag())
                .addTagsItem(ControllerTag.RESTAURANT.getTag())
                .addTagsItem(ControllerTag.STATE.getTag())
                .addTagsItem(ControllerTag.STATISTIC.getTag())
                .addTagsItem(ControllerTag.USER.getTag())
                .addTagsItem(ControllerTag.USER_GROUP.getTag());
    }

    private Info getInfo() {
        return new Info()
                .title("AlgaFood API")
                .description("Open API for delivery food.")
                .version("v1")
                .contact(this.getContact())
                .license(new License().name("Apache 2.0"));
    }

    private Contact getContact() {
        Contact contact = new Contact();
        contact.name("Ronildo Junior");
        contact.email("ronildo.junior@outlook.com");
        contact.url("https://github.com/ronildo-junior");
        return contact;
    }
}