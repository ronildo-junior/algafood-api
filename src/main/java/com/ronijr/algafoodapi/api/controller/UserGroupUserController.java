package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.UserGroupAssembler;
import com.ronijr.algafoodapi.api.model.UserGroupModel;
import com.ronijr.algafoodapi.domain.service.UserCommand;
import com.ronijr.algafoodapi.domain.service.UserQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/user-groups")
@AllArgsConstructor
public class UserGroupUserController {
    private final UserCommand userCommand;
    private final UserQuery userQuery;
    private final UserGroupAssembler userGroupAssembler;

    @GetMapping
    public List<UserGroupModel.Output> list(@PathVariable Long userId) {
        return userGroupAssembler.toCollectionModel(userQuery.getUserGroupList(userId));
    }

    @GetMapping("/{userGroupId}")
    public UserGroupModel.Output get(@PathVariable Long userId, @PathVariable Long userGroupId) {
        return userGroupAssembler.toOutput(userQuery.getUserGroup(userId, userGroupId));
    }

    @PutMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associate(@PathVariable Long userId, @PathVariable Long userGroupId) {
        userCommand.associateUserGroup(userId, userGroupId);
    }

    @DeleteMapping("/{userGroupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disassociate(@PathVariable Long userId, @PathVariable Long userGroupId) {
        userCommand.disassociateUserGroup(userId, userGroupId);
    }
}