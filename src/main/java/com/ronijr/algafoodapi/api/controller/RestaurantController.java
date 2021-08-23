package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.exception.CuisineNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.exception.EntityRequiredPropertyEmptyException;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.service.RestaurantCommandService;
import com.ronijr.algafoodapi.domain.service.RestaurantQueryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.ronijr.algafoodapi.config.utils.AppUtils.mergeFieldsMapInObject;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantQueryService queryService;
    @Autowired
    private RestaurantCommandService commandService;

    @GetMapping
    public List<Restaurant> list() {
        return queryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(queryService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Restaurant restaurant) {
        if (restaurant.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST.");
        try {
            Restaurant result = commandService.create(restaurant);
            URI location = ServletUriComponentsBuilder.
                    fromCurrentRequest().
                    path("/{id}").
                    buildAndExpand(result.getId()).
                    toUri();
            return ResponseEntity.created(location).body(result);
        } catch (CuisineNotFoundException | EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        try {
            Restaurant current = queryService.findById(id);
            BeanUtils.copyProperties(restaurant, current,
                    "id", "paymentMethods", "createdAt");
            return ResponseEntity.ok(commandService.update(current));
        } catch (CuisineNotFoundException | EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        try {
            Restaurant restaurant = queryService.findById(id);
            mergeFieldsMapInObject(patchMap, restaurant);
            return ResponseEntity.ok(commandService.update(restaurant));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (CuisineNotFoundException | EntityRequiredPropertyEmptyException e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Restaurant> delete(@PathVariable Long id) {
        try {
            commandService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/custom")
    public List<Restaurant> customQueryTest(@RequestBody Map<String, Object> map) {
        return queryService.customQuery(map);
    }

    @GetMapping("/with-free-delivery")
    public List<Restaurant> listRestaurantsWithFreeDelivery() {
        return queryService.findAllWithFreeDelivery();
    }

    @GetMapping("/first")
    public ResponseEntity<Restaurant> getFirst() {
        try {
            return ResponseEntity.ok(queryService.findFirst());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
