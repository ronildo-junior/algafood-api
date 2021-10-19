package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.StateModel;
import com.ronijr.algafoodapi.config.mapper.StateMapper;
import com.ronijr.algafoodapi.config.security.AlgaSecurity;
import com.ronijr.algafoodapi.domain.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToState;
import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToStates;

@Component
public class StateAssembler extends RepresentationModelAssemblerSupport<State, StateModel.Output> {
    @Autowired
    private StateMapper mapper;
    @Autowired
    private AlgaSecurity algaSecurity;

    public StateAssembler() {
        super(State.class, StateModel.Output.class);
    }

    @Override
    public StateModel.Output toModel(State state) {
        StateModel.Output model = mapper.entityToOutput(state);
        model.add(linkToState(model.getId()));
        if (algaSecurity.allowQueryStates()) {
            model.add(linkToStates("state-list"));
        }
        return model;
    }

    @Override
    public CollectionModel<StateModel.Output> toCollectionModel(Iterable<? extends State> states) {
        return super.toCollectionModel(states).add(linkToStates());
    }
}