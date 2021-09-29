package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.OrderStatusAssembler;
import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.domain.service.command.OrderStatusCommand;
import com.ronijr.algafoodapi.domain.service.query.OrderQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/orders/{orderCode}",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class OrderStatusController {
    private final OrderStatusCommand statusCommand;
    private final OrderQuery orderQuery;
    private final OrderStatusAssembler statusAssembler;

    @GetMapping("/status")
    public ResponseEntity<OrderModel.StatusInfo> getStatusInfo(@PathVariable String orderCode) {
        return ResponseEntity.ok(statusAssembler.toModel(orderQuery.findByCodeOrElseThrow(orderCode)));
    }

    @PutMapping("/confirmation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> confirmation(@PathVariable String orderCode) {
        statusCommand.confirm(orderCode);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancellation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> cancellation(@PathVariable String orderCode) {
        statusCommand.cancel(orderCode);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/delivery")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delivery(@PathVariable String orderCode) {
        statusCommand.delivery(orderCode);
        return ResponseEntity.noContent().build();
    }
}