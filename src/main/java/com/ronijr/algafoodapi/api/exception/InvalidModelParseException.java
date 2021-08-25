package com.ronijr.algafoodapi.api.exception;

public class InvalidModelParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidModelParseException(String message){
        super(message);
    }

}
