package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.PermissionModel;
import com.ronijr.algafoodapi.config.mapper.PermissionMapper;
import com.ronijr.algafoodapi.domain.model.Permission;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PermissionAssembler {
    private final PermissionMapper mapper;

    public PermissionModel.Output toOutput(Permission permission) {
        return mapper.entityToOutput(permission);
    }

    public PermissionModel.Input toInput(Permission permission) {
        return mapper.entityToInput(permission);
    }

    public List<PermissionModel.Output> toCollectionModel(Collection<Permission> permissions) {
        return permissions.stream().
                map(this::toOutput).
                collect(Collectors.toList());
    }
}