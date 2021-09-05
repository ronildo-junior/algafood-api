package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.model.UserGroupModel;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserGroupMapper {
    UserGroup inputToEntity(UserGroupModel.Input input);
    UserGroupModel.Output entityToOutput(UserGroup entity);
    UserGroupModel.Input entityToInput(UserGroup entity);
    void mergeIntoTarget(UserGroupModel.Input input, @MappingTarget UserGroup target);
}