package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.UserGroupModel;
import com.ronijr.algafoodapi.config.mapper.UserGroupMapper;
import com.ronijr.algafoodapi.domain.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

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
        model.add(linkToUserGroup(resource.getId()));
        model.add(linkToUserGroups("user-group-list"));
        model.add(linkToUserGroupPermissions(resource.getId(), "permissions"));
        return model;
    }

    @Override
    public CollectionModel<UserGroupModel.Output> toCollectionModel(Iterable<? extends UserGroup> entities) {
        return super.toCollectionModel(entities).add(linkToUserGroups());
    }
}