package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.model.StateModel;
import com.ronijr.algafoodapi.domain.model.State;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StateMapper {
    State inputToEntity(StateModel.Input input);
    StateModel.Output entityToOutput(State entity);
    StateModel.Input entityToInput(State entity);
    void mergeIntoTarget(StateModel.Input input, @MappingTarget State target);
}