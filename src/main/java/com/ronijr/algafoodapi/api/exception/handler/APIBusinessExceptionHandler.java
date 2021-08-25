package com.ronijr.algafoodapi.api.exception.handler;

import com.ronijr.algafoodapi.domain.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Handle Exceptions from Business/Domain */
@RestControllerAdvice
class APIBusinessExceptionHandler extends APIExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(EntityRequiredPropertyEmptyException.class)
    public ResponseEntity<Object> handlePropertyEmpty(EntityRequiredPropertyEmptyException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EntityRelationshipNotFoundException.class)
    public ResponseEntity<Object> handleRelationshipNotFound(EntityRelationshipNotFoundException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EntityRelationshipException.class)
    public ResponseEntity<Object> handleConflict(EntityRelationshipException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.CONFLICT);
    }

    @ExceptionHandler(EntityUniqueViolationException.class)
    public ResponseEntity<Object> handleUniqueViolation(EntityUniqueViolationException ex, WebRequest webRequest) {
        return handleException(ex, webRequest, ProblemType.CONFLICT);
    }
}