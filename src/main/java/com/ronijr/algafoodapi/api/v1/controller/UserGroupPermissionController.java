package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.assembler.PermissionAssembler;
import com.ronijr.algafoodapi.api.v1.model.PermissionModel;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.config.security.CheckSecurity;
import com.ronijr.algafoodapi.domain.model.Permission;
import com.ronijr.algafoodapi.domain.service.command.UserGroupCommand;
import com.ronijr.algafoodapi.domain.service.query.UserGroupQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

@RestController
@RequestMapping(value = "/v1/user-groups/{userGroupId}/permissions",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class UserGroupPermissionController {
    private final UserGroupCommand userGroupCommand;
    private final UserGroupQuery userGroupQuery;
    private final PermissionAssembler permissionAssembler;
    private final AlgaSecurity algaSecurity;

    @CheckSecurity.UserGroups.AllowRead
    @GetMapping
    public CollectionModel<PermissionModel.Output> list(@PathVariable Long userGroupId) {
        Set<Permission> permissions = userGroupQuery.listPermissions(userGroupId);
        CollectionModel<PermissionModel.Output> collectionModel =
                permissionAssembler.toCollectionModel(permissions).removeLinks();
        if (algaSecurity.allowEditUserGroup()) {
            collectionModel.add(linkToUserGroupPermissions(userGroupId))
                    .add(linkToUserGroupPermissionAssociation(userGroupId, "associate"));
            collectionModel.forEach(permission ->
                    permission.add(linkToUserGroupPermissionDisassociation(userGroupId, permission.getId(), "disassociate"))
            );
        }
        return collectionModel;
    }

    @CheckSecurity.UserGroups.AllowRead
    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionModel.Output> get(@PathVariable Long userGroupId, @PathVariable Long permissionId) {
        return ResponseEntity.ok(permissionAssembler.toModel(userGroupQuery.getPermission(userGroupId, permissionId)));
    }

    @CheckSecurity.UserGroups.AllowEdit
    @PutMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associatePermission(@PathVariable Long userGroupId, @PathVariable Long permissionId) {
        userGroupCommand.grantPermission(userGroupId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UserGroups.AllowEdit
    @DeleteMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> disassociatePermission(@PathVariable Long userGroupId, @PathVariable Long permissionId) {
        userGroupCommand.revokePermission(userGroupId, permissionId);
        return ResponseEntity.noContent().build();
    }
}