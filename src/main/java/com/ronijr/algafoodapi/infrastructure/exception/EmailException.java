package com.ronijr.algafoodapi.infrastructure.exception;

public class EmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailException(String message){
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}