package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.PermissionAssembler;
import com.ronijr.algafoodapi.api.model.PermissionModel;
import com.ronijr.algafoodapi.domain.service.command.UserGroupCommand;
import com.ronijr.algafoodapi.domain.service.query.UserGroupQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user-groups/{groupId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserGroupPermissionController {
    private final UserGroupCommand userGroupCommand;
    private final UserGroupQuery userGroupQuery;
    private final PermissionAssembler permissionAssembler;

    @GetMapping
    public CollectionModel<PermissionModel.Output> list(@PathVariable Long groupId) {
        return permissionAssembler.toCollectionModel(userGroupQuery.listPermissions(groupId));
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionModel.Output> get(@PathVariable Long groupId, @PathVariable Long permissionId) {
        return ResponseEntity.ok(permissionAssembler.toModel(userGroupQuery.getPermission(groupId, permissionId)));
    }

    @PutMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associatePermission(@PathVariable Long groupId, @PathVariable Long permissionId) {
        userGroupCommand.grantPermission(groupId, permissionId);
    }

    @DeleteMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disassociatePermission(@PathVariable Long groupId, @PathVariable Long permissionId) {
        userGroupCommand.revokePermission(groupId, permissionId);
    }
}