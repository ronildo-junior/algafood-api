package com.ronijr.algafoodapi.api.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.ronijr.algafoodapi.api.exception.InvalidModelParseException;
import com.ronijr.algafoodapi.config.message.AppMessageSource;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
class APIExceptionHandler extends ResponseEntityExceptionHandler {
    private final HttpHeaders headers = new HttpHeaders();
    @Autowired
    private AppMessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        if(body == null) {
            body = ProblemDetails.builder().
                    status(status.value()).
                    title(status.getReasonPhrase()).
                    timestamp(LocalDateTime.now()).
                    build();
        } else if (body instanceof String){
            body = ProblemDetails.builder().
                    status(status.value()).
                    title((String) body).
                    timestamp(LocalDateTime.now()).
                    build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, webRequest);
    }

    protected ProblemDetails.ProblemDetailsBuilder createProblemDetailsBuilder(ProblemType problemType, String detail,
                                                                               String userMessage){
        return ProblemDetails.builder().
                status(problemType.status.value()).
                type(problemType.uri).
                title(messageSource.getMessage(problemType.title)).
                detail(detail).
                userMessage(userMessage).
                timestamp(LocalDateTime.now());
    }

    protected ResponseEntity<Object> handleException(Exception ex, WebRequest webRequest, ProblemType problemType) {
        ProblemDetails problemDetails = createProblemDetailsBuilder(problemType, ex.getMessage(), ex.getMessage()).build();
        return handleExceptionInternal(ex, problemDetails, headers, problemType.status, webRequest);
    }

    protected ResponseEntity<Object> handleException(Exception ex, WebRequest webRequest, ProblemType problemType, String detail) {
        ProblemDetails problemDetails = createProblemDetailsBuilder(problemType, detail,
                messageSource.getMessage("exception.internal.error")).build();
        return handleExceptionInternal(ex, problemDetails, headers, problemType.status, webRequest);
    }

    private String joinPath(List<Reference> references) {
        return references.stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.BAD_REQUEST,
                messageSource.getMessage("resource.not.found", new Object[] { ex.getRequestURL() }));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, webRequest);
        }
        return super.handleTypeMismatch(ex, headers, status, webRequest);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.BAD_REQUEST,
                messageSource.getMessage("exception.invalid.path.variable",
                        new Object[] { ex.getName(), ex.getValue(), Optional.ofNullable(ex.getRequiredType()).
                                map(Class::getSimpleName).
                                orElse("Object")}));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) cause, headers, status, webRequest);
        }
        if (cause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) cause, headers, status, webRequest);
        }
        return handleException(ex, webRequest, ProblemType.BAD_REQUEST, ex.getCause().getMessage());
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
                                                         HttpStatus status, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.BAD_REQUEST,
                messageSource.getMessage("exception.invalid.property",
                        new Object[] { joinPath(ex.getPath()) }));
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers,
                                                       HttpStatus status, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.BAD_REQUEST,
                messageSource.getMessage("exception.invalid.type",
                        new Object[] { joinPath(ex.getPath()), ex.getValue(), ex.getTargetType().getSimpleName() }));
    }

    @ExceptionHandler(InvalidModelParseException.class)
    private ResponseEntity<Object> handleInvalidModelParse(InvalidModelParseException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleGenericException(Exception ex, WebRequest webRequest){
        ex.printStackTrace();
        return handleException(ex, webRequest, ProblemType.SYSTEM_ERROR,
                messageSource.getMessage("exception.internal.error"));
    }

}