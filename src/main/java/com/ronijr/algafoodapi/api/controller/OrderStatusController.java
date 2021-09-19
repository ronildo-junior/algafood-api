package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.OrderAssembler;
import com.ronijr.algafoodapi.api.model.OrderModel;
import com.ronijr.algafoodapi.domain.service.command.OrderStatusCommand;
import com.ronijr.algafoodapi.domain.service.query.OrderQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/orders/{orderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class OrderStatusController {
    private final OrderStatusCommand statusCommand;
    private final OrderQuery orderQuery;
    private final OrderAssembler orderAssembler;

    @GetMapping("/status")
    public ResponseEntity<OrderModel.StatusInfo> getStatusInfo(@PathVariable String orderCode) {
        return ResponseEntity.ok(orderAssembler.toStatusInfo(orderQuery.findByCodeOrElseThrow(orderCode)));
    }

    @PutMapping("/confirmation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmation(@PathVariable String orderCode) {
        statusCommand.confirm(orderCode);
    }

    @PutMapping("/cancellation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable String orderCode) {
        statusCommand.cancel(orderCode);
    }

    @PutMapping("/delivery")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void entregar(@PathVariable String orderCode) {
        statusCommand.delivery(orderCode);
    }
}