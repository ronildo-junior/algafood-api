package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.assembler.StateAssembler;
import com.ronijr.algafoodapi.api.v1.assembler.StateDisassembler;
import com.ronijr.algafoodapi.api.v1.model.StateModel;
import com.ronijr.algafoodapi.config.security.CheckSecurity;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.service.command.StateCommand;
import com.ronijr.algafoodapi.domain.service.query.StateQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.util.Map;

import static com.ronijr.algafoodapi.api.utils.MapperUtils.mergeFieldsMapInObject;
import static com.ronijr.algafoodapi.api.utils.MapperUtils.verifyMapContainsOnlyFieldsOfClass;

@RestController
@RequestMapping(value = "/v1/states", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class StateController {
    private final StateQuery queryService;
    private final StateCommand commandService;
    private final StateAssembler assembler;
    private final StateDisassembler disassembler;

    @CheckSecurity.States.AllowRead
    @GetMapping
    public CollectionModel<StateModel.Output> list() {
        return assembler.toCollectionModel(queryService.findAll());
    }

    @CheckSecurity.States.AllowRead
    @GetMapping("/{id}")
    public ResponseEntity<StateModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(queryService.findByIdOrElseThrow(id)));
    }

    @CheckSecurity.States.AllowCreate
    @PostMapping
    public ResponseEntity<StateModel.Output> create(@RequestBody @Valid StateModel.Input input) {
        State created = commandService.create(disassembler.toDomain(input));
        StateModel.Output output = assembler.toModel(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(output.getId()).
                toUri();
        return ResponseEntity.created(location).body(output);
    }

    @CheckSecurity.States.AllowEdit
    @PutMapping("/{id}")
    public ResponseEntity<StateModel.Output> update(@PathVariable Long id, @RequestBody @Valid StateModel.Input input) {
        State current = queryService.findByIdOrElseThrow(id);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @CheckSecurity.States.AllowEdit
    @PatchMapping("/{id}")
    public ResponseEntity<StateModel.Output> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        verifyMapContainsOnlyFieldsOfClass(patchMap, StateModel.Input.class);
        State current = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @CheckSecurity.States.AllowDelete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}