package com.ronijr.algafoodapi.domain.exception;

public class EntityRequiredPropertyEmptyException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public EntityRequiredPropertyEmptyException(String message){
        super(message);
    }

    public EntityRequiredPropertyEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

}
