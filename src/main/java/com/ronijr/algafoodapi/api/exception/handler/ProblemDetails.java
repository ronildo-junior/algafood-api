package com.ronijr.algafoodapi.api.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private LocalDateTime timestamp;
}