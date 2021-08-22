package com.ronijr.algafoodapi.domain.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String message){
        super(message);
    }

    public EntityNotFoundException(Long id) {
        super(String.format("Entity with id = %d not found ", id));
    }
}
