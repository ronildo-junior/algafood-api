package com.ronijr.algafoodapi.config.documentation;

import com.ronijr.algafoodapi.api.exception.handler.ProblemDetails;
import com.ronijr.algafoodapi.api.v1.controller.CuisineController;
import com.ronijr.algafoodapi.config.documentation.model.ControllerTag;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ronijr.algafoodapi.config.documentation.HttpStatus.*;

@Configuration
public class SpringDocConfig implements OperationCustomizer {
    public static final String PATH_SCHEMAS = "#/components/schemas/";

    private final List<Integer> problemStatuses = Arrays.asList(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.UNPROCESSABLE_ENTITY.value()
    );

    /** Initial Configuration */
    @Bean
    public OpenAPI springShopOpenAPI() {
        OpenAPI openAPI = new OpenAPI().info(this.getInfo());
        addTags(openAPI);
        addSecurity(openAPI);
        return openAPI;
    }

    @Bean
    @Lazy(false)
    PageConverter pageableOpenAPIConverter() {
        return new PageConverter();
    }

    @Bean
    public OpenApiCustomiser customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> {
            var schemas = openApi.getComponents().getSchemas();
            addProblemResponse(openApi);
            openApi.getPaths().values().forEach(
                    pathItem -> pathItem.readOperations().forEach(
                            operation -> {
                                ApiResponses apiResponses = operation.getResponses();
                                changeOkToCreatedStatusInPost(operation, pathItem, apiResponses);
                                addDefaultResponses(pathItem, operation, apiResponses);
                                addContentResponse(apiResponses);
                                replacePatchMapWithPutBody(pathItem, operation, schemas);
                            }
                    )
            );
        };
    }

    /**
     * Assign Tags in Methods
     */
    @Override
    public Operation customize(final Operation operation, final HandlerMethod handlerMethod) {
        var tag = ControllerTag.findByType(handlerMethod.getBeanType().getSimpleName());
        if (tag.isPresent()) {
            operation.getTags().clear();
            operation.addTagsItem(tag.get().getTitle());
            addDefaultResponsesInMethod(handlerMethod, operation);
        }
        return operation;
    }

    /**
     * Replace Response Status 200 by 201 in POST Methods
     */
    private void changeOkToCreatedStatusInPost(final Operation operation, final PathItem pathItem, final ApiResponses apiResponses) {
        if (operation.equals(pathItem.getPost()) && apiResponses.containsKey(OK)) {
            ApiResponse apiResponse = apiResponses.remove(OK);
            apiResponse.setDescription("Successful Created");
            apiResponses.put(CREATED, apiResponse);
        }
    }

    /**
     * Add Responses to Custom Method
     */
    private void addDefaultResponsesInMethod(final HandlerMethod handlerMethod, final Operation operation) {
        if (handlerMethod.getBeanType().isAssignableFrom(CuisineController.class)) {
            List<Class<?>> list = Arrays.stream(handlerMethod.getMethod().getAnnotations())
                    .map(Annotation::annotationType)
                    .collect(Collectors.toList());
            if (list.contains(org.springframework.web.bind.annotation.PutMapping.class) ||
                    list.contains(org.springframework.web.bind.annotation.PostMapping.class)) {
                addConflictResponse(operation.getResponses());
            }
        }
    }

    /**
     * Add Default Responses to all operations
     */
    private void addDefaultResponses(final PathItem pathItem, final Operation operation, final ApiResponses apiResponses) {
        if (operation.equals(pathItem.getDelete())) {
            addNoContentOnDelete(apiResponses);
        }
        if (operation.getParameters() != null && !operation.getParameters().isEmpty()) {
            addBadRequestOnParameterInvalid(operation, apiResponses);
            addNotFoundOnParameterInformed(operation, apiResponses);
        }
        if (operation.getRequestBody() != null &&
                (operation.equals(pathItem.getPost()) || operation.equals(pathItem.getPut()))) {
            addUnprocessableEntityOnRequestBody(apiResponses);
        }
        addInternalErrorResponse(apiResponses);
    }

    private void addBadRequestOnParameterInvalid(final Operation operation, final ApiResponses apiResponses) {
        String description = "Parameter (" + operation.getParameters().stream()
                .map(r -> r.getDescription() == null ? r.getName() : r.getDescription())
                .collect(Collectors.joining(" / ")) + ") Invalid";
        if (apiResponses.containsKey(BAD_REQUEST)) {
            ApiResponse apiResponse = apiResponses.get(BAD_REQUEST);
            apiResponse.setDescription(apiResponse.getDescription() + " / " + description);
        } else {
            ApiResponse apiResponse = new ApiResponse()
                    .description(description);
            apiResponses.addApiResponse(BAD_REQUEST, apiResponse);
        }
    }

    private void addNotFoundOnParameterInformed(final Operation operation, final ApiResponses apiResponses) {
        String description = "Resource with informed Parameter " + operation.getParameters().stream()
                .map(r -> r.getDescription() == null ? r.getName() : r.getDescription())
                .collect(Collectors.joining(" / ")) + " Not Found";
        if (apiResponses.containsKey(NOT_FOUND)) {
            ApiResponse apiResponse = apiResponses.get(NOT_FOUND);
            apiResponse.setDescription(apiResponse.getDescription() + " / " + description);
        } else {
            ApiResponse apiResponse = new ApiResponse()
                    .description(description);
            apiResponses.addApiResponse(NOT_FOUND, apiResponse);
        }
    }

    private void addUnprocessableEntityOnRequestBody(final ApiResponses apiResponses) {
        String description = "\n" +
                "Request body was rejected by the server. " +
                "The structure is accepted but its content is invalid.";
        if (apiResponses.containsKey(UNPROCESSABLE_ENTITY)) {
            ApiResponse apiResponse = apiResponses.get(UNPROCESSABLE_ENTITY);
            apiResponse.setDescription(apiResponse.getDescription() + " / " + description);
        } else {
            ApiResponse apiResponse = new ApiResponse()
                    .description(description);
            apiResponses.addApiResponse(UNPROCESSABLE_ENTITY, apiResponse);
        }
    }

    private void addNoContentOnDelete(final ApiResponses apiResponses) {
        if (!apiResponses.containsKey(NO_CONTENT)) {
            ApiResponse apiResponse = new ApiResponse()
                    .description("Successful deleting");
            apiResponses.addApiResponse(NO_CONTENT, apiResponse);
        }
    }

    private void addConflictResponse(final ApiResponses apiResponses) {
        if (!apiResponses.containsKey(CONFLICT)) {
            ApiResponse apiResponse = new ApiResponse()
                    .description("Conflict on save");
            apiResponses.addApiResponse(CONFLICT, apiResponse);
        }
    }

    private void addInternalErrorResponse(final ApiResponses apiResponses) {
        if (!apiResponses.containsKey(INTERNAL_SERVER_ERROR)) {
            ApiResponse apiResponse = new ApiResponse()
                    .description("Internal Server Error");
            apiResponses.addApiResponse(INTERNAL_SERVER_ERROR, apiResponse);
        }
    }

    private void replacePatchMapWithPutBody(
            final PathItem pathItem, final Operation operation, final Map<String, Schema> schemas) {
        if (operation.getRequestBody() != null && operation.equals(pathItem.getPatch())) {
            RequestBody requestBody = pathItem.getPut().getRequestBody();
            var schema = requestBody.getContent().entrySet().iterator().next().getValue().getSchema();
            var fullSchema = schemas.values().stream()
                    .filter(v -> v.getName().equals(schema.get$ref().replace(PATH_SCHEMAS, "")))
                    .findAny();
            if (fullSchema.isPresent()) {
                operation.setRequestBody(duplicateBodyWithoutRequiredFields(fullSchema.get()));
            } else {
                operation.setRequestBody(requestBody);
            }
        }
    }

    private RequestBody duplicateBodyWithoutRequiredFields(final Schema<?> schema) {
        return new RequestBody().content(
                new Content().addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().schema(duplicateSchemaWithoutRequiredFields(schema))));
    }

    private Schema<Map<String, Object>> duplicateSchemaWithoutRequiredFields(final Schema<?> oldSchema) {
        Schema<Map<String, Object>> newSchema = new Schema<>();
        oldSchema.getProperties().forEach(newSchema::addProperties);
        newSchema.setRequired(null);
        return newSchema;
    }

    private void addContentResponse(final ApiResponses apiResponses) {
        apiResponses.forEach((statusCode, apiResponse) -> {
            if (problemStatuses.contains(Integer.valueOf(statusCode))) {
                addProblemResponse(apiResponse);
            }
        });
    }

    private void addProblemResponse(final ApiResponse apiResponse) {
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

    /**
     * Add Problem Details Model
     */
    private void addProblemResponse(final OpenAPI openAPI) {
        var schemas = openAPI.getComponents().getSchemas();
        schemas.putAll(ModelConverters.getInstance().read(ProblemDetails.class));
        schemas.putAll(ModelConverters.getInstance().read(ProblemDetails.Object.class));
    }

    /**
     * Create tags
     */
    private void addTags(final OpenAPI openAPI) {
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

    private void addSecurity(final OpenAPI openAPI) {
        openAPI.components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization")))
                .addSecurityItem(
                        new SecurityRequirement().addList("bearer-jwt", Arrays.asList("READ", "WRITE")));
    }

    private Info getInfo() {
        return new Info()
                .title("AlgaFood API")
                .description("Open API for delivery food.")
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