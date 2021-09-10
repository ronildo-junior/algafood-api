package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.OrderAssembler;
import com.ronijr.algafoodapi.api.assembler.OrderDisassembler;
import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.domain.repository.filter.OrderFilter;
import com.ronijr.algafoodapi.domain.service.OrderCommand;
import com.ronijr.algafoodapi.domain.service.OrderQuery;
import lombok.AllArgsConstructor;
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
    private final OrderQuery query;
    private final OrderCommand command;
    private final OrderAssembler assembler;
    private final OrderDisassembler disassembler;

    @GetMapping
    public List<OrderModel.Summary> customSearch(OrderFilter filter) {
        return assembler.toCollectionModel(query.findAll(doFilter(filter)));
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