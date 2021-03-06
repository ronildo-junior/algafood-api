package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.assembler.OrderAssembler;
import com.ronijr.algafoodapi.api.v1.assembler.OrderDisassembler;
import com.ronijr.algafoodapi.api.v1.assembler.OrderSummaryAssembler;
import com.ronijr.algafoodapi.api.v1.model.OrderModel;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.config.security.CheckSecurity;
import com.ronijr.algafoodapi.domain.filter.OrderFilter;
import com.ronijr.algafoodapi.domain.model.Order;
import com.ronijr.algafoodapi.domain.service.command.OrderCommand;
import com.ronijr.algafoodapi.domain.service.query.OrderQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.ronijr.algafoodapi.infrastructure.repository.specification.OrderSpecification.doFilter;

@RestController
@RequestMapping(value = "/v1/orders", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class OrderController {
    private static final int ORDER_PAGE_SIZE = 20;

    private final OrderQuery query;
    private final OrderCommand command;
    private final OrderAssembler assembler;
    private final OrderSummaryAssembler assemblerSummary;
    private final OrderDisassembler disassembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;
    private final AlgaSecurity security;

    @CheckSecurity.Orders.AllowList
    @GetMapping
    public PagedModel<OrderModel.Summary> customSearch(OrderFilter filter, @PageableDefault(size = ORDER_PAGE_SIZE) Pageable pageable) {
        Page<Order> ordersPage = query.findAll(doFilter(filter), pageable);
        return pagedResourcesAssembler.toModel(ordersPage, assemblerSummary);
    }

    @CheckSecurity.Orders.AllowQuery
    @GetMapping("/{code}")
    public ResponseEntity<OrderModel.Output> get(@PathVariable String code) {
        return ResponseEntity.ok(assembler.toModel(query.findByCodeOrElseThrow(code)));
    }

    @CheckSecurity.Orders.AllowCreate
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderModel.Output create(@RequestBody @Valid OrderModel.Input input) {
        return assembler.toModel(command.create(disassembler.toDomain(input), security.getUserId()));
    }
}