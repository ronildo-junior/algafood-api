package com.ronijr.algafoodapi.api.exception.handler;

import org.springframework.http.HttpStatus;

public enum ProblemType {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request", "bad-request"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,"Resource Not Found", "not-found"),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", "unprocessable"),
    CONFLICT(HttpStatus.CONFLICT, "Conflict", "conflict"),
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "internal-server-error");

    HttpStatus status;
    String title;
    String uri;

    ProblemType(HttpStatus status, String title, String path){
        this.status = status;
        this.title = title;
        this.uri = "http://api.algafood.local/" + path;
    }
}