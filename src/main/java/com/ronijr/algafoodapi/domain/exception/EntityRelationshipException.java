package com.ronijr.algafoodapi.domain.exception;

public class EntityRelationshipException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityRelationshipException(String message){
        super(message);
    }

}
