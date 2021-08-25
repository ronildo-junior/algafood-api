package com.ronijr.algafoodapi.domain.exception;

public class EntityUniqueViolationException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public EntityUniqueViolationException(String message){
        super(message);
    }

    public EntityUniqueViolationException(String message, Throwable cause) {
        super(message, cause);
    }

}
