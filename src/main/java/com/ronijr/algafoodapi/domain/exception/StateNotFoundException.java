package com.ronijr.algafoodapi.domain.exception;

public class StateNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public StateNotFoundException(String message){
        super(message);
    }

    public StateNotFoundException(Long id) {
        super(String.format("State with id = %d not found.", id));
    }
}
