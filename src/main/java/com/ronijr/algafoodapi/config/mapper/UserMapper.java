package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.v1.model.UserModel;
import com.ronijr.algafoodapi.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User inputCreateToEntity(UserModel.Create input);
    User inputUpdateToEntity(UserModel.Update input);
    UserModel.Output entityToOutput(User entity);
    UserModel.Update entityToInputUpdate(User entity);
    void mergeIntoTarget(UserModel.Update input, @MappingTarget User target);
}