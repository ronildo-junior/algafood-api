package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.service.CityCommandService;
import com.ronijr.algafoodapi.domain.service.CityQueryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.ronijr.algafoodapi.api.utils.MapperUitls.mergeFieldsMapInObject;

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
        return ResponseEntity.ok(queryService.findByIdOrElseThrow(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody City city) {
        if (city.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST.");
        City result = commandService.create(city);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(result.getId()).
                toUri();
        return ResponseEntity.created(location).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> update(@PathVariable Long id, @RequestBody City city) {
        City current = queryService.findByIdOrElseThrow(id);
        BeanUtils.copyProperties(city, current, "id");
        return ResponseEntity.ok(commandService.update(current));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<City> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        City city = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, city);
        return ResponseEntity.ok(commandService.update(city));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}