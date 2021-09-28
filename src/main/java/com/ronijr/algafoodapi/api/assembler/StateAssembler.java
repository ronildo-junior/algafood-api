package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.StateModel;
import com.ronijr.algafoodapi.config.mapper.StateMapper;
import com.ronijr.algafoodapi.domain.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.hateoas.AlgaLinks.*;

@Component
public class StateAssembler extends RepresentationModelAssemblerSupport<State, StateModel.Output> {
    @Autowired
    private StateMapper mapper;

    public StateAssembler() {
        super(State.class, StateModel.Output.class);
    }

    @Override
    public StateModel.Output toModel(State state) {
        StateModel.Output model = mapper.entityToOutput(state);
        model.add(linkToState(model.getId()));
        model.add(linkToStates("state-list"));
        return model;
    }

    @Override
    public CollectionModel<StateModel.Output> toCollectionModel(Iterable<? extends State> states) {
        return super.toCollectionModel(states).add(linkToStates());
    }
}