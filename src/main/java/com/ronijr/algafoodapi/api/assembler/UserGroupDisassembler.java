package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.UserGroupModel;
import com.ronijr.algafoodapi.config.mapper.UserGroupMapper;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserGroupDisassembler {
    private final UserGroupMapper mapper;

    public UserGroup toDomain(UserGroupModel.Input input) {
        return mapper.inputToEntity(input);
    }

    public void copyToDomainObject(UserGroupModel.Input input, UserGroup userGroup) {
        mapper.mergeIntoTarget(input, userGroup);
    }
}