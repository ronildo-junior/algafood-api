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

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

@RestController
@RequestMapping(value = "/user-groups/{userGroupId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserGroupPermissionController {
    private final UserGroupCommand userGroupCommand;
    private final UserGroupQuery userGroupQuery;
    private final PermissionAssembler permissionAssembler;

    @GetMapping
    public CollectionModel<PermissionModel.Output> list(@PathVariable Long userGroupId) {
        CollectionModel<PermissionModel.Output> collectionModel =
                permissionAssembler.toCollectionModel(userGroupQuery.listPermissions(userGroupId))
                        .removeLinks()
                        .add(linkToUserGroupPermissions(userGroupId))
                        .add(linkToUserGroupPermissionAssociation(userGroupId, "associate"));
        collectionModel.forEach(permission ->
            permission.add(linkToUserGroupPermissionDisassociation(userGroupId, permission.getId(), "disassociate"))
        );
        return collectionModel;
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionModel.Output> get(@PathVariable Long userGroupId, @PathVariable Long permissionId) {
        return ResponseEntity.ok(permissionAssembler.toModel(userGroupQuery.getPermission(userGroupId, permissionId)));
    }

    @PutMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associatePermission(@PathVariable Long userGroupId, @PathVariable Long permissionId) {
        userGroupCommand.grantPermission(userGroupId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> disassociatePermission(@PathVariable Long userGroupId, @PathVariable Long permissionId) {
        userGroupCommand.revokePermission(userGroupId, permissionId);
        return ResponseEntity.noContent().build();
    }
}