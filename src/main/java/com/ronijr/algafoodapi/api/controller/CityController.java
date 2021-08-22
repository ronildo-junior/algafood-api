package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.service.CityCommandService;
import com.ronijr.algafoodapi.domain.service.CityQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.ronijr.algafoodapi.config.utils.AppUtils.mergeFieldsMapInObject;

@RestController
@RequestMapping("/cities")
public class CityController {
    @Autowired
    private CityQueryService queryService;
    @Autowired
    private CityCommandService commandService;

    @GetMapping
    public List<City> list() {
        return queryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> get(@PathVariable Long id) {
        City city = queryService.findById(id);
        if (city != null) {
            return ResponseEntity.ok(city);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody City city) {
        if (city.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST");
        try {
            City result = commandService.create(city);
            URI location = ServletUriComponentsBuilder.
                    fromCurrentRequest().
                    path("/{id}").
                    buildAndExpand(result.getId()).
                    toUri();
            return ResponseEntity.created(location).body(result);
        } catch (EntityNotFoundException | EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody City city) {
        if (queryService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            city.setId(id);
            return ResponseEntity.ok(commandService.update(city));
        } catch (EntityNotFoundException | EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> pathMap) {
        City city = queryService.findById(id);
        if (city == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            mergeFieldsMapInObject(pathMap, city);
            city = commandService.update(city);
            return ResponseEntity.ok(city);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }  catch (EntityNotFoundException | EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<City> delete(@PathVariable Long id) {
        try {
            commandService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}