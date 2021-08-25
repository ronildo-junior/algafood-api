package com.ronijr.algafoodapi.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidModelParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidModelParseException(String message){
        super(message);
    }

}
