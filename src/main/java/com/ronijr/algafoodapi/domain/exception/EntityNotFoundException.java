package com.ronijr.algafoodapi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message){
        super(message);
    }

    public EntityNotFoundException(Long id) {
        super(String.format("Entity with id = %d not found.", id));
    }

    public EntityNotFoundException(String entityName, String id) {
        super(String.format("Entity %s with id = %s not found.", entityName,  id));
    }
}
