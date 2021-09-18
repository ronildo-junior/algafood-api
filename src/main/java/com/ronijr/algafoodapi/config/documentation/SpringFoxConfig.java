package com.ronijr.algafoodapi.config.documentation;

import com.fasterxml.classmate.TypeResolver;
import com.ronijr.algafoodapi.api.exception.handler.ProblemDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig implements WebMvcConfigurer {
    @Bean
    public Docket apiDocket() {
        var typeResolver = new TypeResolver();
        return new Docket(DocumentationType.OAS_30)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.ronijr.algafoodapi.api"))
                    .paths(PathSelectors.any())
                .build()
                    .apiInfo(apiInfo())
                    .useDefaultResponseMessages(false)
                    .globalResponses(HttpMethod.GET, globalGetResponseMessages())
                    .globalResponses(HttpMethod.POST, globalPostResponseMessages())
                    .globalResponses(HttpMethod.PUT, globalPutResponseMessages())
                    .globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
                    .additionalModels(typeResolver.resolve(ProblemDetails.class));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("index.html")
                .addResourceLocations("classpath:/META-INF/resources/");
    }

    private List<Response> globalGetResponseMessages() {
        return Arrays.asList(
                buildResponse(HttpStatus.INTERNAL_SERVER_ERROR),
                buildResponse(HttpStatus.NOT_ACCEPTABLE)
        );
    }

    private List<Response> globalPostResponseMessages() {
        return Arrays.asList(
                buildResponse(HttpStatus.INTERNAL_SERVER_ERROR),
                buildResponse(HttpStatus.BAD_REQUEST),
                buildResponse(HttpStatus.NOT_ACCEPTABLE),
                buildResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        );
    }

    private List<Response> globalPutResponseMessages() {
        List<Response> put = new ArrayList<>(globalPostResponseMessages());
        put.add(buildResponse(HttpStatus.NOT_FOUND));
        return put;
    }

    private List<Response> globalDeleteResponseMessages() {
        return Arrays.asList(
                buildResponse(HttpStatus.INTERNAL_SERVER_ERROR),
                buildResponse(HttpStatus.BAD_REQUEST)
        );
    }

    private Response buildResponse(HttpStatus status) {
        if (responseWithProblem(status)) {
            return new ResponseBuilder()
                    .code(String.valueOf(status.value()))
                    .description(status.getReasonPhrase())
                    .representation(MediaType.APPLICATION_JSON)
                        .apply(builderModelProblem())
                    .build();
        }
        return new ResponseBuilder()
                .code(String.valueOf(status.value()))
                .description(status.getReasonPhrase())
                .build();
    }

    private Consumer<RepresentationBuilder> builderModelProblem() {
        return r -> r.model(m -> m.name("ProblemDetails")
                .referenceModel(
                        ref -> ref.key(
                                k -> k.qualifiedModelName(
                                        q -> q.name("ProblemDetails")
                                                .namespace("com.ronijr.algafoodapi.api.exception.handler")
                                ))));
    }

    private boolean responseWithProblem(HttpStatus status) {
        return Arrays.asList(
            HttpStatus.INTERNAL_SERVER_ERROR,
            HttpStatus.BAD_REQUEST,
            HttpStatus.CONFLICT,
            HttpStatus.UNPROCESSABLE_ENTITY
        ).contains(status);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("AlgaFood API")
                .description("Open API for delivery food.")
                .version("1")
                .contact(new Contact("Ronildo Junior",
                        "https://github.com/ronildo-junior", "ronildo.junior@outlook.com"))
                .build();
    }
}