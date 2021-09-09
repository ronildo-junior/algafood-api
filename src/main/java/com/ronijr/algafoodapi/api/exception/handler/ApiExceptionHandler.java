package com.ronijr.algafoodapi.api.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.ronijr.algafoodapi.api.exception.InvalidModelParseException;
import com.ronijr.algafoodapi.config.message.AppMessageSource;
import com.ronijr.algafoodapi.domain.exception.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

import static com.ronijr.algafoodapi.api.exception.handler.ExceptionUtils.*;

@ControllerAdvice
@AllArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final HttpHeaders headers = new HttpHeaders();
    private final AppMessageSource messenger;

    private static final String INTERNAL_SERVER_ERROR = "exception.internal.error";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            return super.handleExceptionInternal(ex,
                    problemBuilder().status(status.value()).title(status.getReasonPhrase()).build(), headers, status, request);

        } else if (body instanceof String){
            return super.handleExceptionInternal(ex,
                    problemBuilder().status(status.value()).title((String) body).build(), headers, status, request);
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ResponseEntity<Object> handleException(
            Exception ex, WebRequest request, ProblemType problemType, ProblemDetails.ProblemDetailsBuilder builder) {
        var problem = builder.title(messenger.getMessage(problemType.title)).build();
        return handleExceptionInternal(ex, problem, headers, problemType.status, request);
    }

    protected ResponseEntity<Object> handleException(Exception ex, WebRequest request, ProblemType problemType, String detail) {
        return handleException(ex, request, problemType, problemBuilder(problemType, detail));
    }

    private ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request, String detail){
        return handleException(ex, request, ProblemType.BAD_REQUEST,
                problemBuilder(ProblemType.BAD_REQUEST, detail, messenger.getMessage(INTERNAL_SERVER_ERROR)));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = messenger.getMessage("resource.not.found", ex.getRequestURL());
        return handleException(ex, request, ProblemType.BAD_REQUEST, detail);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleBadRequest(ex, request, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = messenger.getMessage("exception.invalid.property.list");
        var problem = problemBuilder(ProblemType.BAD_REQUEST, detail).
                fields(getFields(ex.getBindingResult(), messenger));
        return handleException(ex, request, ProblemType.BAD_REQUEST, problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) cause, request);
        }
        if (cause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) cause, request);
        }
        return handleBadRequest(ex, request, ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, WebRequest request) {
        String detail = messenger.getMessage("exception.invalid.type",
                joinPath(ex.getPath()), ex.getValue(), ex.getTargetType().getSimpleName());
        return handleBadRequest(ex, request, detail);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, WebRequest request) {
        String detail = messenger.getMessage("exception.invalid.property", joinPath(ex.getPath()));
        return handleBadRequest(ex, request, detail);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String className = Optional.ofNullable(ex.getRequiredType()).map(Class::getSimpleName).orElse("Object");
        String detail = messenger.getMessage("exception.invalid.path.variable", ex.getName(), ex.getValue(), className);
        return handleBadRequest(ex, request, detail);
    }

    @ExceptionHandler(InvalidModelParseException.class)
    private ResponseEntity<Object> handleInvalidModelParse(InvalidModelParseException ex, WebRequest request) {
        return handleBadRequest(ex, request, ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidation(ValidationException ex, WebRequest request) {
        String detail = messenger.getMessage("exception.invalid.property.list");
        var problem = problemBuilder(ProblemType.BAD_REQUEST, detail).
                fields(getFields(ex.getBindingResult(), messenger));
        return handleException(ex, request, ProblemType.BAD_REQUEST, problem);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.RESOURCE_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EntityRelationshipNotFoundException.class)
    public ResponseEntity<Object> handleRelationshipNotFound(EntityRelationshipNotFoundException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.INVALID_DATA, ex.getMessage());
    }

    @ExceptionHandler(EntityRelationshipException.class)
    public ResponseEntity<Object> handleConflict(EntityRelationshipException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EntityUniqueViolationException.class)
    public ResponseEntity<Object> handleUniqueViolation(EntityUniqueViolationException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(StatusTransitionException.class)
    private ResponseEntity<Object> handleBusinessException(StatusTransitionException ex, WebRequest request){
        return handleException(ex, request, ProblemType.INVALID_DATA, messenger.getMessage(
                StatusTransitionException.RESOURCE_MESSAGE, ex.getCurrentStatus(), ex.getNewStatus()));
    }

    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request){
        return handleException(ex, request, ProblemType.INVALID_DATA, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request){
        ex.printStackTrace();
        return handleException(ex, request, ProblemType.SYSTEM_ERROR, messenger.getMessage(INTERNAL_SERVER_ERROR));
    }
}