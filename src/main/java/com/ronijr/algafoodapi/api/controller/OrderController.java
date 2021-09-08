package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.OrderAssembler;
import com.ronijr.algafoodapi.api.assembler.OrderDisassembler;
import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.domain.service.OrderCommand;
import com.ronijr.algafoodapi.domain.service.OrderQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderQuery query;
    private final OrderCommand command;
    private final OrderAssembler assembler;
    private final OrderDisassembler disassembler;

    @GetMapping
    public List<OrderModel.Summary> list() {
        return assembler.toCollectionModel(query.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toOutput(query.findByIdOrElseThrow(id)));
    }

    @PostMapping
    public ResponseEntity<OrderModel.Output> create(@RequestBody @Valid OrderModel.Input input) {
        return ResponseEntity.ok(assembler.toOutput(command.create(disassembler.toDomain(input))));
    }
}