package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.exception.StateNotFoundException;
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
        try {
            return ResponseEntity.ok(queryService.findById(id));
        } catch (StateNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody State state) {
        if (state.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST.");
        try {
            State result = commandService.create(state);
            URI location = ServletUriComponentsBuilder.
                    fromCurrentRequest().
                    path("/{id}").
                    buildAndExpand(result.getId()).
                    toUri();
            return ResponseEntity.created(location).body(result);
        } catch (EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody State state) {
        try {
            State current = queryService.findById(id);
            BeanUtils.copyProperties(state, current, "id");
            return ResponseEntity.ok(commandService.update(current));
        } catch (EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (StateNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        try {
            State state = queryService.findById(id);
            mergeFieldsMapInObject(patchMap, state);
            return ResponseEntity.ok(commandService.update(state));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (StateNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            commandService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityRelationshipException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (StateNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
