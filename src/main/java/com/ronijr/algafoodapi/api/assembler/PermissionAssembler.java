package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.controller.UserGroupPermissionController;
import com.ronijr.algafoodapi.api.model.PermissionModel;
import com.ronijr.algafoodapi.config.mapper.PermissionMapper;
import com.ronijr.algafoodapi.domain.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PermissionAssembler extends RepresentationModelAssemblerSupport<Permission, PermissionModel.Output> {
    @Autowired
    private PermissionMapper mapper;

    public PermissionAssembler() {
        super(Permission.class, PermissionModel.Output.class);
    }

    @Override
    public PermissionModel.Output toModel(Permission permission) {
        PermissionModel.Output model = mapper.entityToOutput(permission);
        model.add(linkTo(methodOn(UserGroupPermissionController.class).get(1L, model.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserGroupPermissionController.class).list(1L)).withRel("permission-list"));
        return model;
    }

    @Override
    public CollectionModel<PermissionModel.Output> toCollectionModel(Iterable<? extends Permission> permissions) {
        return super.toCollectionModel(permissions)
                .add(linkTo(UserGroupPermissionController.class).withSelfRel());
    }
}