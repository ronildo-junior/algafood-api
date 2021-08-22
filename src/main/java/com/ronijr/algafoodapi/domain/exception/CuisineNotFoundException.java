package com.ronijr.algafoodapi.domain.exception;

public class CuisineNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1L;

    public CuisineNotFoundException(String message){
        super(message);
    }

    public CuisineNotFoundException(Long id) {
        super(String.format("Cuisine with id = %d not found.", id));
    }
}
