package com.ronijr.algafoodapi.domain.repository;

import com.ronijr.algafoodapi.domain.model.State;

import java.util.List;

public interface StateRepository {
    List<State> list();
    State get(Long id);
    State add(State state);
    void remove(Long id);
}
