package com.ronijr.algafoodapi.config.mapper;

import com.ronijr.algafoodapi.api.model.StateModel;
import com.ronijr.algafoodapi.domain.model.State;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StateMapper {
    State inputToEntity(StateModel.Input input);
    StateModel.Output entityToOutput(State entity);
    StateModel.Summary entityToSummary(State entity);
}