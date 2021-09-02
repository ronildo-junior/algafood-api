package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.StateAssembler;
import com.ronijr.algafoodapi.api.assembler.StateDisassembler;
import com.ronijr.algafoodapi.api.model.StateModel;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.service.StateCommand;
import com.ronijr.algafoodapi.domain.service.StateQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.ronijr.algafoodapi.api.utils.MapperUtils.mergeFieldsMapInObject;

@RestController
@RequestMapping("/states")
@AllArgsConstructor
public class StateController {
    private final StateQuery queryService;
    private final StateCommand commandService;
    private final StateAssembler assembler;
    private final StateDisassembler disassembler;

    @GetMapping
    public List<StateModel.Output> list() {
        return assembler.toCollectionModel(queryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StateModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toOutput(queryService.findByIdOrElseThrow(id)));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody StateModel.Input input) {
        State created = commandService.create(disassembler.toDomain(input));
        StateModel.Output output = assembler.toOutput(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(output.getId()).
                toUri();
        return ResponseEntity.created(location).body(output);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateModel.Output> update(@PathVariable Long id, @RequestBody StateModel.Input input) {
        State current = queryService.findByIdOrElseThrow(id);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StateModel.Output> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        State current = queryService.findByIdOrElseThrow(id);
        StateModel.Input input = assembler.toInput(current);
        mergeFieldsMapInObject(patchMap, input);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}