package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.controller.UserController;
import com.ronijr.algafoodapi.api.model.UserModel;
import com.ronijr.algafoodapi.config.mapper.UserMapper;
import com.ronijr.algafoodapi.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserModel.Output> {
    @Autowired
    private UserMapper mapper;

    public UserAssembler() {
        super(User.class, UserModel.Output.class);
    }

    @Override
    public UserModel.Output toModel(User user) {
        UserModel.Output model = mapper.entityToOutput(user);
        model.add(linkTo(methodOn(UserController.class).get(model.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).list()).withRel("user-list"));
        return model;
    }

    @Override
    public CollectionModel<UserModel.Output> toCollectionModel(Iterable<? extends User> users) {
        return super.toCollectionModel(users)
                .add(linkTo(UserController.class).withSelfRel());
    }
}