package com.ronijr.algafoodapi.api.controller;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ronijr.algafoodapi.api.assembler.OrderAssembler;
import com.ronijr.algafoodapi.api.assembler.OrderDisassembler;
import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.domain.service.OrderCommand;
import com.ronijr.algafoodapi.domain.service.OrderQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.StringUtils;
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
    public MappingJacksonValue list(@RequestParam(required = false) String fields) {
        List<OrderModel.Summary> orders = assembler.toCollectionModel(query.findAll());
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        if (StringUtils.hasLength(fields)) {
            filterProvider.addFilter("orderFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fields.split(",")));
        } else {
            filterProvider.addFilter("orderFilter", SimpleBeanPropertyFilter.serializeAll());
        }
        MappingJacksonValue wrapper = new MappingJacksonValue(orders);
        wrapper.setFilters(filterProvider);
        return wrapper;
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