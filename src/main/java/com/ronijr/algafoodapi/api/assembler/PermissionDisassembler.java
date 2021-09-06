package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.PermissionModel;
import com.ronijr.algafoodapi.config.mapper.PermissionMapper;
import com.ronijr.algafoodapi.domain.model.Permission;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PermissionDisassembler {
    private final PermissionMapper mapper;

    public Permission toDomain(PermissionModel.Input input) {
        return mapper.inputToEntity(input);
    }

    public void copyToDomainObject(PermissionModel.Input input, Permission permission) {
        mapper.mergeIntoTarget(input, permission);
    }
}