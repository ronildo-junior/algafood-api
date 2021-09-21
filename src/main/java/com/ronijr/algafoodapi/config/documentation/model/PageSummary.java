package com.ronijr.algafoodapi.config.documentation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PageSummary<T> {
    private List<T> content;
    private Long size;
    private Long totalElements;
    private Long totalPages;
    private Long number;
}