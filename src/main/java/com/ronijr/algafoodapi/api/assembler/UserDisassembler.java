package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.UserModel;
import com.ronijr.algafoodapi.config.mapper.UserMapper;
import com.ronijr.algafoodapi.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDisassembler {
    private final UserMapper mapper;

    public User inputCreateToDomain(UserModel.Create input) {
        return mapper.inputCreateToEntity(input);
    }

    public User inputUpdateToDomain(UserModel.Update input) {
        return mapper.inputUpdateToEntity(input);
    }

    public void copyToDomainObject(UserModel.Update input, User user) {
        mapper.mergeIntoTarget(input, user);
    }
}