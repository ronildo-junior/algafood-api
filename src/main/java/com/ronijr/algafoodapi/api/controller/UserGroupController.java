package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.UserGroupAssembler;
import com.ronijr.algafoodapi.api.assembler.UserGroupDisassembler;
import com.ronijr.algafoodapi.api.model.UserGroupModel;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import com.ronijr.algafoodapi.domain.service.command.UserGroupCommand;
import com.ronijr.algafoodapi.domain.service.query.UserGroupQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
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
@RequestMapping(value = "/user-groups", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserGroupController {
    private final UserGroupQuery queryService;
    private final UserGroupCommand commandService;
    private final UserGroupAssembler assembler;
    private final UserGroupDisassembler disassembler;

    @GetMapping
    public CollectionModel<UserGroupModel.Output> list() {
        return assembler.toCollectionModel(queryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroupModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(queryService.findByIdOrElseThrow(id)));
    }

    @PostMapping
    public ResponseEntity<UserGroupModel.Output> create(@RequestBody @Valid UserGroupModel.Input input) {
        UserGroup created = commandService.create(disassembler.toDomain(input));
        UserGroupModel.Output output = assembler.toModel(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(output.getId()).
                toUri();
        return ResponseEntity.created(location).body(output);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGroupModel.Output> update(@PathVariable Long id, @RequestBody @Valid UserGroupModel.Input input) {
        UserGroup current = queryService.findByIdOrElseThrow(id);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserGroupModel.Output> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        verifyMapContainsOnlyFieldsOfClass(patchMap, UserGroupModel.Input.class);
        UserGroup current = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}