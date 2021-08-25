package com.ronijr.algafoodapi.domain.exception;

public class EntityNotFoundException extends BusinessException {

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

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
