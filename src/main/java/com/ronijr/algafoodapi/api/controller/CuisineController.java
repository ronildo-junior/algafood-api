package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.exception.EntityUniqueViolationException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.service.CuisineCommandService;
import com.ronijr.algafoodapi.domain.service.CuisineQueryService;
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
@RequestMapping("/cuisines")
public class CuisineController {
    @Autowired
    private CuisineCommandService commandService;
    @Autowired
    private CuisineQueryService queryService;

    @GetMapping
    public List<Cuisine> list() {
        return queryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuisine> get(@PathVariable Long id) {
        Cuisine cuisine = queryService.findById(id);
        if (cuisine != null) {
            return ResponseEntity.ok(cuisine);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-name")
    public List<Cuisine> cuisinesByName(@RequestParam String name) {
        return queryService.findByName(name);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Cuisine cuisine) {
        if (cuisine.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST");
        try {
            Cuisine result = commandService.create(cuisine);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(result.getId())
                    .toUri();
            return ResponseEntity.created(location).body(result);
        } catch (EntityUniqueViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Cuisine cuisine) {
        if (queryService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            cuisine.setId(id);
            Cuisine result = commandService.update(cuisine);
            return ResponseEntity.ok(result);
        } catch (EntityUniqueViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateOnePartial(@PathVariable Long id, @RequestBody Map<String, Object> pathMap) {
        Cuisine cuisine = queryService.findById(id);
        if (cuisine == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            mergeFieldsMapInObject(pathMap, cuisine);
            cuisine = commandService.update(cuisine);
            return ResponseEntity.ok(cuisine);
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
