package com.ronijr.algafoodapi.domain.exception;

public class EntityUniqueViolationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityUniqueViolationException(String message){
        super(message);
    }

}
