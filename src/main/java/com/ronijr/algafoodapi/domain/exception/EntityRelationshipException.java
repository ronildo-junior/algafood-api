package com.ronijr.algafoodapi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntityRelationshipException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityRelationshipException(String message){
        super(message);
    }

}
