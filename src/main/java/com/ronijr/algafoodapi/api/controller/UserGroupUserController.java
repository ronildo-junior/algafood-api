package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.UserGroupAssembler;
import com.ronijr.algafoodapi.api.model.UserGroupModel;
import com.ronijr.algafoodapi.domain.service.command.UserCommand;
import com.ronijr.algafoodapi.domain.service.query.UserQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

@RestController
@RequestMapping(value = "/users/{userId}/user-groups", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserGroupUserController {
    private final UserCommand userCommand;
    private final UserQuery userQuery;
    private final UserGroupAssembler userGroupAssembler;

    @GetMapping
    public CollectionModel<UserGroupModel.Output> list(@PathVariable Long userId) {
        CollectionModel<UserGroupModel.Output> model =
                userGroupAssembler.toCollectionModel(userQuery.getUserGroupList(userId))
                        .removeLinks()
                        .add(linkToUserGroupUser(userId))
                        .add(linkToUserGroupAssociation(userId, "associate"));
        model.forEach(userGroup ->
            userGroup.add(linkToUserGroupDisassociation(userId, userGroup.getId(),"disassociate"))
        );
        return model;
    }

    @GetMapping("/{userGroupId}")
    public UserGroupModel.Output get(@PathVariable Long userId, @PathVariable Long userGroupId) {
        return userGroupAssembler.toModel(userQuery.getUserGroup(userId, userGroupId));
    }

    @PutMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associate(@PathVariable Long userId, @PathVariable Long userGroupId) {
        userCommand.associateUserGroup(userId, userGroupId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> disassociate(@PathVariable Long userId, @PathVariable Long userGroupId) {
        userCommand.disassociateUserGroup(userId, userGroupId);
        return ResponseEntity.noContent().build();
    }
}