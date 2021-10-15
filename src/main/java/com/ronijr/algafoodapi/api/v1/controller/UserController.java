package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.assembler.UserAssembler;
import com.ronijr.algafoodapi.api.v1.assembler.UserDisassembler;
import com.ronijr.algafoodapi.api.v1.model.UserModel;
import com.ronijr.algafoodapi.config.security.CheckSecurity;
import com.ronijr.algafoodapi.domain.model.User;
import com.ronijr.algafoodapi.domain.service.command.UserCommand;
import com.ronijr.algafoodapi.domain.service.query.UserQuery;
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

import static com.ronijr.algafoodapi.api.utils.MapperUtils.verifyMapContainsOnlyFieldsOfClass;

@RestController
@RequestMapping(value = "/v1/users", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class UserController {
    private final UserQuery queryService;
    private final UserCommand commandService;
    private final UserAssembler assembler;
    private final UserDisassembler disassembler;

    @CheckSecurity.Users.AllowRead
    @GetMapping
    public CollectionModel<UserModel.Output> list() {
        return assembler.toCollectionModel(queryService.findAll());
    }

    @CheckSecurity.Users.AllowRead
    @GetMapping("/{userId}")
    public ResponseEntity<UserModel.Output> get(@PathVariable Long userId) {
        return ResponseEntity.ok(assembler.toModel(queryService.findByIdOrElseThrow(userId)));
    }

    @PostMapping
    public ResponseEntity<UserModel.Output> create(@RequestBody @Valid UserModel.Create input) {
        User created = commandService.create(disassembler.inputCreateToDomain(input));
        UserModel.Output output = assembler.toModel(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{userId}").
                buildAndExpand(output.getId()).
                toUri();
        return ResponseEntity.created(location).body(output);
    }

    @CheckSecurity.Users.AllowEdit
    @PutMapping("/{userId}")
    public ResponseEntity<UserModel.Output> update(@PathVariable Long userId, @RequestBody @Valid UserModel.Update input) {
        User current = disassembler.inputUpdateToDomain(input);
        return ResponseEntity.ok(assembler.toModel(commandService.update(userId, current)));
    }

    @CheckSecurity.Users.AllowEdit
    @PatchMapping("/{userId}")
    public ResponseEntity<UserModel.Output> updatePartial(@PathVariable Long userId, @RequestBody Map<String, Object> patchMap) {
        verifyMapContainsOnlyFieldsOfClass(patchMap, UserModel.Update.class);
        return ResponseEntity.ok(assembler.toModel(commandService.updatePartial(userId, patchMap)));
    }

    @CheckSecurity.Users.AllowDelete
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        commandService.delete(userId);
    }

    @CheckSecurity.Users.AllowEditPassword
    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable Long userId, @RequestBody @Valid UserModel.UpdatePassword input) {
        commandService.updatePassword(userId, input.getPassword(), input.getNewPassword());
    }
}