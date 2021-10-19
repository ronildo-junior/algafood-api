package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.UserModel;
import com.ronijr.algafoodapi.config.mapper.UserMapper;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.*;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserModel.Output> {
    @Autowired
    private UserMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public UserAssembler() {
        super(User.class, UserModel.Output.class);
    }

    @Override
    public UserModel.Output toModel(User user) {
        UserModel.Output model = mapper.entityToOutput(user);
        model.add(linkToUser(model.getId()));
        if (algaSecurity.allowQueryUserGroupPermissions()) {
            model.add(linkToUsers("user-list"));
            model.add(linkToUserGroupUser(user.getId(), "user-group"));
        }
        return model;
    }

    @Override
    public CollectionModel<UserModel.Output> toCollectionModel(Iterable<? extends User> users) {
        return super.toCollectionModel(users).add(linkToUsers());
    }
}