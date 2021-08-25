package com.ronijr.algafoodapi.domain.exception;

public class EntityRelationshipException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public EntityRelationshipException(String message){
        super(message);
    }

    public EntityRelationshipException(String message, Throwable cause) {
        super(message, cause);
    }

}
