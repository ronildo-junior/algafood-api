package com.ronijr.algafoodapi.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusTransitionException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final String RESOURCE_MESSAGE = "status.transition-not-allowed";
    private final String currentStatus;
    private final String newStatus;
}