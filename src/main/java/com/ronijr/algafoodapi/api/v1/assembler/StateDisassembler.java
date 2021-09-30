package com.ronijr.algafoodapi.api.v1.assembler;

import com.ronijr.algafoodapi.api.v1.model.StateModel;
import com.ronijr.algafoodapi.config.mapper.StateMapper;
import com.ronijr.algafoodapi.domain.model.State;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StateDisassembler {
    private final StateMapper mapper;

    public State toDomain(StateModel.Input input) {
        return mapper.inputToEntity(input);
    }

    public void copyToDomainObject(StateModel.Input input, State state) {
        mapper.mergeIntoTarget(input, state);
    }
}