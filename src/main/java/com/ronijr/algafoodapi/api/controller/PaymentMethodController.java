package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.PaymentMethodAssembler;
import com.ronijr.algafoodapi.api.assembler.PaymentMethodDisassembler;
import com.ronijr.algafoodapi.api.model.PaymentMethodModel;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
import com.ronijr.algafoodapi.domain.service.command.PaymentMethodCommand;
import com.ronijr.algafoodapi.domain.service.query.PaymentMethodQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ronijr.algafoodapi.api.utils.MapperUtils.mergeFieldsMapInObject;
import static com.ronijr.algafoodapi.api.utils.MapperUtils.verifyMapContainsOnlyFieldsOfClass;

@RestController
@RequestMapping("/payment-methods")
@AllArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodQuery queryService;
    private final PaymentMethodCommand commandService;
    private final PaymentMethodAssembler assembler;
    private final PaymentMethodDisassembler disassembler;

    @GetMapping
    public ResponseEntity<List<PaymentMethodModel.Output>> list() {
        return ResponseEntity.ok().
                cacheControl(
                        CacheControl.maxAge(10, TimeUnit.SECONDS).
                        cachePublic()).
                body(assembler.toCollectionModel(queryService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok().
                cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS)).
                body(assembler.toOutput(queryService.findByIdOrElseThrow(id)));
    }

    @PostMapping
    public ResponseEntity<PaymentMethodModel.Output> create(@RequestBody @Valid PaymentMethodModel.Input input) {
        PaymentMethod created = commandService.create(disassembler.toDomain(input));
        PaymentMethodModel.Output output = assembler.toOutput(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(output.getId()).
                toUri();
        return ResponseEntity.created(location).body(output);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodModel.Output> update(@PathVariable Long id, @RequestBody @Valid PaymentMethodModel.Input input) {
        PaymentMethod current = queryService.findByIdOrElseThrow(id);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentMethodModel.Output> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        verifyMapContainsOnlyFieldsOfClass(patchMap, PaymentMethodModel.Input.class);
        PaymentMethod current = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}