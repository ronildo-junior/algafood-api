package com.ronijr.algafoodapi.api.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implements Specification RFC 7807 from IETF.
 * Problem Details for HTTP APIs.*/
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Builder
class ProblemDetails {
    private Integer status;
    private String type;
    private String title;
    private String detail;
    /**
     * Custom Properties. */
    private String userMessage;
    private LocalDateTime timestamp;
    private List<Object> fields;

    @Getter @Builder
    public static class Object {
        private String name;
        private String description;
    }
}