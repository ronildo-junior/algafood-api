package com.ronijr.algafoodapi.infrastructure.exception;

public class TemplateRendererException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TemplateRendererException(String message){
        super(message);
    }

    public TemplateRendererException(String message, Throwable cause) {
        super(message, cause);
    }
}