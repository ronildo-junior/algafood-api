package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.assembler.UserGroupAssembler;
import com.ronijr.algafoodapi.api.v1.model.UserGroupModel;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.config.security.CheckSecurity;
import com.ronijr.algafoodapi.domain.service.command.UserCommand;
import com.ronijr.algafoodapi.domain.service.query.UserQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

@RestController
@RequestMapping(value = "/v1/users/{userId}/user-groups",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class UserGroupUserController {
    private final UserCommand userCommand;
    private final UserQuery userQuery;
    private final UserGroupAssembler userGroupAssembler;
    private final AlgaSecurity algaSecurity;

    @CheckSecurity.UserGroups.AllowRead
    @GetMapping
    public CollectionModel<UserGroupModel.Output> list(@PathVariable Long userId) {
        CollectionModel<UserGroupModel.Output> model =
                userGroupAssembler.toCollectionModel(userQuery.getUserGroupList(userId))
                        .removeLinks()
                        .add(linkToUserGroupUser(userId));
        if (algaSecurity.allowEditUserGroup()) {
            model.add(linkToUserGroupAssociation(userId, "associate"));
            model.forEach(userGroup ->
                    userGroup.add(linkToUserGroupDisassociation(userId, userGroup.getId(), "disassociate"))
            );
        }
        return model;
    }

    @CheckSecurity.UserGroups.AllowRead
    @GetMapping("/{userGroupId}")
    public UserGroupModel.Output get(@PathVariable Long userId, @PathVariable Long userGroupId) {
        return userGroupAssembler.toModel(userQuery.getUserGroup(userId, userGroupId));
    }

    @CheckSecurity.UserGroups.AllowEdit
    @PutMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associate(@PathVariable Long userId, @PathVariable Long userGroupId) {
        userCommand.associateUserGroup(userId, userGroupId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UserGroups.AllowEdit
    @DeleteMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> disassociate(@PathVariable Long userId, @PathVariable Long userGroupId) {
        userCommand.disassociateUserGroup(userId, userGroupId);
        return ResponseEntity.noContent().build();
    }
}