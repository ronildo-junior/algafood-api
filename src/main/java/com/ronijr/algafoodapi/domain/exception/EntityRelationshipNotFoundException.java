package com.ronijr.algafoodapi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EntityRelationshipNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityRelationshipNotFoundException(String message){
        super(message);
    }

}