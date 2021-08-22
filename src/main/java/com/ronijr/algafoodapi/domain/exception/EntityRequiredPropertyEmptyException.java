package com.ronijr.algafoodapi.domain.exception;

public class EntityRequiredPropertyEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityRequiredPropertyEmptyException(String message){
        super(message);
    }

}
