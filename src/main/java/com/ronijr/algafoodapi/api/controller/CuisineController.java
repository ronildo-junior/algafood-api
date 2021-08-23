package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.exception.CuisineNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRelationshipException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.exception.EntityUniqueViolationException;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.service.CuisineCommandService;
import com.ronijr.algafoodapi.domain.service.CuisineQueryService;
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
        try {
            return ResponseEntity.ok(queryService.findById(id));
        } catch (CuisineNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-name")
    public List<Cuisine> cuisinesByName(@RequestParam String name) {
        return queryService.findByName(name);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Cuisine cuisine) {
        if (cuisine.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST.");
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
        try {
            Cuisine current = queryService.findById(id);
            BeanUtils.copyProperties(cuisine, current, "id");
            return ResponseEntity.ok(commandService.update(current));
        } catch (EntityUniqueViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (CuisineNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        try {
            Cuisine cuisine = queryService.findById(id);
            mergeFieldsMapInObject(patchMap, cuisine);
            return ResponseEntity.ok(commandService.update(cuisine));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityUniqueViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (CuisineNotFoundException e) {
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
        } catch (CuisineNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
