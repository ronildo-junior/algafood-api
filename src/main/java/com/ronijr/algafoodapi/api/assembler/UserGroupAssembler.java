package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.UserGroupModel;
import com.ronijr.algafoodapi.config.mapper.UserGroupMapper;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserGroupAssembler {
    private final UserGroupMapper mapper;

    public UserGroupModel.Output toOutput(UserGroup userGroup) {
        return mapper.entityToOutput(userGroup);
    }

    public UserGroupModel.Input toInput(UserGroup userGroup) {
        return mapper.entityToInput(userGroup);
    }

    public List<UserGroupModel.Output> toCollectionModel(Collection<UserGroup> userGroups) {
        return userGroups.stream().
                map(this::toOutput).
                collect(Collectors.toList());
    }
}