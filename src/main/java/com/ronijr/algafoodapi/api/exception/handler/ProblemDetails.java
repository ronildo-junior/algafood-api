package com.ronijr.algafoodapi.api.exception.handler;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder
class ProblemDetails {
    private LocalDateTime timestamp;
    private String message;
    private String url;
}
