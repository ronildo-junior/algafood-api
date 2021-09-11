package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.OrderAssembler;
import com.ronijr.algafoodapi.api.assembler.OrderDisassembler;
import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.domain.filter.OrderFilter;
import com.ronijr.algafoodapi.domain.model.Order;
import com.ronijr.algafoodapi.domain.service.OrderCommand;
import com.ronijr.algafoodapi.domain.service.OrderQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.ronijr.algafoodapi.infrastructure.repository.specification.OrderSpecification.doFilter;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private static final int ORDER_PAGE_SIZE = 20;

    private final OrderQuery query;
    private final OrderCommand command;
    private final OrderAssembler assembler;
    private final OrderDisassembler disassembler;

    @GetMapping
    public Page<OrderModel.Summary> customSearch(OrderFilter filter, @PageableDefault(size = ORDER_PAGE_SIZE) Pageable pageable) {
        Page<Order> orderPage = query.findAll(doFilter(filter), pageable);
        List<OrderModel.Summary> orderSummary = assembler.toCollectionModel(orderPage.getContent());
        return new PageImpl<>(orderSummary, pageable, orderPage.getTotalElements());
    }

    @GetMapping("/{code}")
    public ResponseEntity<OrderModel.Output> get(@PathVariable String code) {
        return ResponseEntity.ok(assembler.toOutput(query.findByCodeOrElseThrow(code)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderModel.Output create(@RequestBody @Valid OrderModel.Input input) {
        return assembler.toOutput(command.create(disassembler.toDomain(input)));
    }
}