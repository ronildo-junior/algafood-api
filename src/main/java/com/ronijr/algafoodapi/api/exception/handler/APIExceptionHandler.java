package com.ronijr.algafoodapi.api.exception.handler;

import com.ronijr.algafoodapi.domain.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
class APIExceptionHandler extends ResponseEntityExceptionHandler {
    private final HttpHeaders headers = new HttpHeaders();

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        if(body == null) {
            body = ProblemDetails.builder().
                    timestamp(LocalDateTime.now()).
                    message(status.getReasonPhrase()).
                    url(request.getRequestURL().toString()).build();
        } else if (body instanceof String){
            body = ProblemDetails.builder().
                    timestamp(LocalDateTime.now()).
                    message((String) body).
                    url(request.getRequestURL().toString()).build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, webRequest);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, WebRequest webRequest) {
        return handleExceptionInternal(ex, ex.getMessage(), headers,
                HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler(EntityRequiredPropertyEmptyException.class)
    public ResponseEntity<Object> handlePropertyEmpty(EntityRequiredPropertyEmptyException ex, WebRequest webRequest) {
        return handleExceptionInternal(ex, ex.getMessage(), headers,
                HttpStatus.UNPROCESSABLE_ENTITY, webRequest);
    }

    @ExceptionHandler(EntityRelationshipNotFoundException.class)
    public ResponseEntity<Object> handleRelationshipNotFound(EntityRelationshipNotFoundException ex, WebRequest webRequest) {
        return handleExceptionInternal(ex, ex.getMessage(), headers,
                HttpStatus.UNPROCESSABLE_ENTITY, webRequest);
    }

    @ExceptionHandler(EntityRelationshipException.class)
    public ResponseEntity<Object> handleConflict(EntityRelationshipException ex, WebRequest webRequest) {
        return handleExceptionInternal(ex, ex.getMessage(), headers,
                HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(EntityUniqueViolationException.class)
    public ResponseEntity<Object> handleUniqueViolation(EntityUniqueViolationException ex, WebRequest webRequest) {
        return handleExceptionInternal(ex, ex.getMessage(), headers,
                HttpStatus.CONFLICT, webRequest);
    }
}