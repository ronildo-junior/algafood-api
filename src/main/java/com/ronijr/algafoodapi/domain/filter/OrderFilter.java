package com.ronijr.algafoodapi.domain.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.OffsetDateTime;

@Getter @Setter
public class OrderFilter {
    private Long customerId;
    private Long restaurantId;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime creationDateFirst;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime creationDateLast;
}