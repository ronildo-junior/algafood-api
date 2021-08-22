package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.exception.EntityUniqueViolationException;
import com.ronijr.algafoodapi.domain.model.State;
import com.ronijr.algafoodapi.domain.service.StateCommandService;
import com.ronijr.algafoodapi.domain.service.StateQueryService;
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
        State state = queryService.findById(id);
        if (state != null) {
            return ResponseEntity.ok(state);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody State state) {
        if (state.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST");
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
        if (queryService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            state.setId(id);
            State result = commandService.update(state);
            return ResponseEntity.ok(result);
        } catch (EntityUniqueViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> pathMap) {
        State state = queryService.findById(id);
        if (state == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            mergeFieldsMapInObject(pathMap, state);
            state = commandService.update(state);
            return ResponseEntity.ok(state);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityUniqueViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            commandService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityRelationshipException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
