package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.PermissionModel;
import com.ronijr.algafoodapi.config.mapper.PermissionMapper;
import com.ronijr.algafoodapi.domain.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToPermissions;

@Component
public class PermissionAssembler extends RepresentationModelAssemblerSupport<Permission, PermissionModel.Output> {
    @Autowired
    private PermissionMapper mapper;

    public PermissionAssembler() {
        super(Permission.class, PermissionModel.Output.class);
    }

    @Override
    public PermissionModel.Output toModel(Permission permission) {
        return mapper.entityToOutput(permission);
    }

    @Override
    public CollectionModel<PermissionModel.Output> toCollectionModel(Iterable<? extends Permission> permissions) {
        return super.toCollectionModel(permissions).add(linkToPermissions());
    }
}