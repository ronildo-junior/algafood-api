package com.ronijr.algafoodapi.api.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.ronijr.algafoodapi.config.message.AppMessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

final class ExceptionUtils {
    private ExceptionUtils() {}

    static ProblemDetails.ProblemDetailsBuilder problemBuilder(){
        return ProblemDetails.builder().timestamp(LocalDateTime.now());
    }

    static ProblemDetails.ProblemDetailsBuilder problemBuilder(ProblemType problemType){
        return problemBuilder().status(problemType.status.value()).type(problemType.uri);
    }

    static ProblemDetails.ProblemDetailsBuilder problemBuilder(ProblemType problemType, String detail){
        return problemBuilder(problemType).detail(detail).userMessage(detail);
    }

    static ProblemDetails.ProblemDetailsBuilder problemBuilder(ProblemType problemType, String detail, String userMessage){
        return problemBuilder(problemType).detail(detail).userMessage(userMessage);
    }

    static String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    static List<ProblemDetails.Object> getFields(BindingResult result, AppMessageSource messenger) {
        return result.getAllErrors().stream().
                map(r -> ProblemDetails.Object.builder().
                        name((r instanceof FieldError) ?
                                ((FieldError) r).getField() :
                                r.getObjectName()).
                        description(messenger.getMessage(r)).
                        build()).
                collect(Collectors.toList());
    }
}