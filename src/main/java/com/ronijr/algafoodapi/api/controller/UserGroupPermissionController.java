package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.PermissionAssembler;
import com.ronijr.algafoodapi.api.model.PermissionModel;
import com.ronijr.algafoodapi.domain.service.UserGroupCommand;
import com.ronijr.algafoodapi.domain.service.UserGroupQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-groups/{groupId}/permissions")
@AllArgsConstructor
public class UserGroupPermissionController {
    private final UserGroupCommand userGroupCommand;
    private final UserGroupQuery userGroupQuery;
    private final PermissionAssembler permissionAssembler;

    @GetMapping
    public List<PermissionModel.Output> list(@PathVariable Long groupId) {
        return permissionAssembler.toCollectionModel(userGroupQuery.listPermissions(groupId));
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionModel.Output> get(@PathVariable Long groupId, @PathVariable Long permissionId) {
        return ResponseEntity.ok(permissionAssembler.toOutput(userGroupQuery.getPermission(groupId, permissionId)));
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