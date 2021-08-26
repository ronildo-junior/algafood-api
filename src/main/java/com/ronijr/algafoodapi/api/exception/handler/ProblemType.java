package com.ronijr.algafoodapi.api.exception.handler;

import org.springframework.http.HttpStatus;

public enum ProblemType {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "exception.title.bad.request", "bad-request"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,"exception.title.not.found", "not-found"),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "exception.title.unprocessable.entity", "unprocessable"),
    CONFLICT(HttpStatus.CONFLICT, "exception.title.conflict", "conflict"),
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "exception.title.system.error", "internal-server-error");

    HttpStatus status;
    String title;
    String uri;

    ProblemType(HttpStatus status, String title, String path){
        this.status = status;
        this.title = title;
        this.uri = "http://api.algafood.local/" + path;
    }
}