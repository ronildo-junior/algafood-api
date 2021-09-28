package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.OrderAssembler;
import com.ronijr.algafoodapi.api.assembler.OrderDisassembler;
import com.ronijr.algafoodapi.api.assembler.OrderSummaryAssembler;
import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.domain.filter.OrderFilter;
import com.ronijr.algafoodapi.domain.model.Order;
import com.ronijr.algafoodapi.domain.service.command.OrderCommand;
import com.ronijr.algafoodapi.domain.service.query.OrderQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.ronijr.algafoodapi.infrastructure.repository.specification.OrderSpecification.doFilter;

@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderController {
    private static final int ORDER_PAGE_SIZE = 20;

    private final OrderQuery query;
    private final OrderCommand command;
    private final OrderAssembler assembler;
    private final OrderSummaryAssembler assemblerSummary;
    private final OrderDisassembler disassembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<OrderModel.Summary> customSearch(OrderFilter filter, @PageableDefault(size = ORDER_PAGE_SIZE) Pageable pageable) {
        Page<Order> ordersPage = query.findAll(doFilter(filter), pageable);
        return pagedResourcesAssembler.toModel(ordersPage, assemblerSummary);
    }

    @GetMapping("/{code}")
    public ResponseEntity<OrderModel.Output> get(@PathVariable String code) {
        return ResponseEntity.ok(assembler.toModel(query.findByCodeOrElseThrow(code)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderModel.Output create(@RequestBody @Valid OrderModel.Input input) {
        return assembler.toModel(command.create(disassembler.toDomain(input)));
    }
}