package com.ronijr.algafoodapi.domain.exception;

public class EntityRelationshipNotFoundException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public EntityRelationshipNotFoundException(String message){
        super(message);
    }

    public EntityRelationshipNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}