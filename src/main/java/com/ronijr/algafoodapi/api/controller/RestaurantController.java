package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.service.RestaurantCommandService;
import com.ronijr.algafoodapi.domain.service.RestaurantQueryService;
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
        return ResponseEntity.ok(queryService.findByIdOrElseThrow(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Restaurant restaurant) {
        if (restaurant.getId() != null) return ResponseEntity.badRequest().body("id not allow in POST.");
        Restaurant result = commandService.create(restaurant);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(result.getId()).
                toUri();
        return ResponseEntity.created(location).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> update(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        Restaurant current = queryService.findByIdOrElseThrow(id);
        BeanUtils.copyProperties(restaurant, current,
                "id", "paymentMethods", "createdAt", "products");
        return ResponseEntity.ok(commandService.update(current));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Restaurant> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        Restaurant restaurant = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, restaurant);
        return ResponseEntity.ok(commandService.update(restaurant));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
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
        return ResponseEntity.ok(queryService.findFirst());
    }
}
