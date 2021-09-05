package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.UserModel;
import com.ronijr.algafoodapi.config.mapper.UserMapper;
import com.ronijr.algafoodapi.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserAssembler {
    private final UserMapper mapper;

    public UserModel.Output toOutput(User user) {
        return mapper.entityToOutput(user);
    }

    public UserModel.Update toInputUpdate(User user) {
        return mapper.entityToInputUpdate(user);
    }

    public List<UserModel.Output> toCollectionModel(List<User> users) {
        return users.stream().
                map(this::toOutput).
                collect(Collectors.toList());
    }
}