package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.controller.UserGroupController;
import com.ronijr.algafoodapi.api.model.UserGroupModel;
import com.ronijr.algafoodapi.config.mapper.UserGroupMapper;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Relation(collectionRelation = "user-groups")
@Component
public class UserGroupAssembler extends RepresentationModelAssemblerSupport<UserGroup, UserGroupModel.Output> {
    @Autowired
    private UserGroupMapper mapper;

    public UserGroupAssembler() {
        super(UserGroup.class, UserGroupModel.Output.class);
    }

    @Override
    public UserGroupModel.Output toModel(UserGroup resource) {
        UserGroupModel.Output model = mapper.entityToOutput(resource);
        model.add(linkTo(methodOn(UserGroupController.class).get(model.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserGroupController.class).list()).withRel("user-group-list"));
        return model;
    }

    @Override
    public CollectionModel<UserGroupModel.Output> toCollectionModel(Iterable<? extends UserGroup> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(UserGroupController.class).withSelfRel());
    }
}