package com.ronijr.algafoodapi.api.controller;

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
        return ResponseEntity.ok(queryService.findByIdOrElseThrow(id));
    }

    @GetMapping("/by-name")
    public List<Cuisine> cuisinesByName(@RequestParam String name) {
        return queryService.findByName(name);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Cuisine cuisine) {
        if (cuisine.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST.");
        Cuisine result = commandService.create(cuisine);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuisine> update(@PathVariable Long id, @RequestBody Cuisine cuisine) {
        Cuisine current = queryService.findByIdOrElseThrow(id);
        BeanUtils.copyProperties(cuisine, current, "id");
        return ResponseEntity.ok(commandService.update(current));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cuisine> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        Cuisine cuisine = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, cuisine);
        return ResponseEntity.ok(commandService.update(cuisine));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}
