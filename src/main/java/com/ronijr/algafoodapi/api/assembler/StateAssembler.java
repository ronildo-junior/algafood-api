package com.ronijr.algafoodapi.api.assembler;

import com.ronijr.algafoodapi.api.model.StateModel;
import com.ronijr.algafoodapi.config.mapper.StateMapper;
import com.ronijr.algafoodapi.domain.model.State;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class StateAssembler {
    private final StateMapper mapper;

    public StateModel.Output toOutput(State state) {
        return mapper.entityToOutput(state);
    }

    public StateModel.Input toInput(State state) {
        return mapper.entityToInput(state);
    }

    public List<StateModel.Output> toCollectionModel(List<State> states) {
        return states.stream().
                map(this::toOutput).
                collect(Collectors.toList());
    }
}