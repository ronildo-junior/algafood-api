package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.UserAssembler;
import com.ronijr.algafoodapi.api.assembler.UserDisassembler;
import com.ronijr.algafoodapi.api.model.UserModel;
import com.ronijr.algafoodapi.domain.model.User;
import com.ronijr.algafoodapi.domain.service.UserCommand;
import com.ronijr.algafoodapi.domain.service.UserQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.ronijr.algafoodapi.api.utils.MapperUtils.mergeFieldsMapInObject;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserQuery queryService;
    private final UserCommand commandService;
    private final UserAssembler assembler;
    private final UserDisassembler disassembler;

    @GetMapping
    public List<UserModel.Output> list() {
        return assembler.toCollectionModel(queryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toOutput(queryService.findByIdOrElseThrow(id)));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserModel.Create input) {
        User created = commandService.create(disassembler.inputCreateToDomain(input));
        UserModel.Output output = assembler.toOutput(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(output.getId()).
                toUri();
        return ResponseEntity.created(location).body(output);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel.Output> update(@PathVariable Long id, @RequestBody @Valid UserModel.Update input) {
        User current = new User();
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(id, current)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserModel.Output> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        User current = queryService.findByIdOrElseThrow(id);
        UserModel.Update input = assembler.toInputUpdate(current);
        mergeFieldsMapInObject(patchMap, input);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(id, current)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable Long id, @RequestBody @Valid UserModel.UpdatePassword input) {
        commandService.updatePassword(id, input.getPassword(), input.getNewPassword());
    }
}