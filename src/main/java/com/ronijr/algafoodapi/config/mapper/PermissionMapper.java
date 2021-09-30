package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.v1.model.PermissionModel;
import com.ronijr.algafoodapi.domain.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission inputToEntity(PermissionModel.Input input);
    PermissionModel.Output entityToOutput(Permission entity);
    PermissionModel.Input entityToInput(Permission entity);
    void mergeIntoTarget(PermissionModel.Input input, @MappingTarget Permission target);
}