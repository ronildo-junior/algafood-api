package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.service.StateCommandService;
import com.ronijr.algafoodapi.domain.service.StateQueryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.ronijr.algafoodapi.config.utils.AppUtils.mergeFieldsMapInObject;

@RestController
@RequestMapping("/states")
public class StateController {
    @Autowired
    private StateQueryService queryService;
    @Autowired
    private StateCommandService commandService;

    @GetMapping
    public List<State> list() {
        return queryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<State> get(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.findByIdOrElseThrow(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody State state) {
        if (state.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST.");
        State result = commandService.create(state);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(result.getId()).
                toUri();
        return ResponseEntity.created(location).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<State> update(@PathVariable Long id, @RequestBody State state) {
        State current = queryService.findByIdOrElseThrow(id);
        BeanUtils.copyProperties(state, current, "id");
        return ResponseEntity.ok(commandService.update(current));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<State> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        State state = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, state);
        return ResponseEntity.ok(commandService.update(state));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}
