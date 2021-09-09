package com.ronijr.algafoodapi.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ronijr.algafoodapi.api.assembler.RestaurantAssembler;
import com.ronijr.algafoodapi.api.assembler.RestaurantDisassembler;
import com.ronijr.algafoodapi.api.model.RestaurantModel;
import com.ronijr.algafoodapi.api.model.view.RestaurantView;
import com.ronijr.algafoodapi.domain.exception.BusinessException;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.service.RestaurantCommand;
import com.ronijr.algafoodapi.domain.service.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.ronijr.algafoodapi.api.utils.MapperUtils.mergeFieldsMapInObject;
import static com.ronijr.algafoodapi.api.utils.MapperUtils.verifyMapContainsOnlyFieldsOfClass;

@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantQuery queryService;
    private final RestaurantCommand commandService;
    private final RestaurantAssembler assembler;
    private final RestaurantDisassembler disassembler;

    @GetMapping
    public List<RestaurantModel.Summary> list() {
        return assembler.toCollectionModel(queryService.findAll());
    }

    @GetMapping(params = "projection=by-name")
    @JsonView(RestaurantView.OnlyName.class)
    public List<RestaurantModel.Summary> listByName() {
        return list();
    }


    @GetMapping("/{id}")
    public ResponseEntity<RestaurantModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toOutput(queryService.findByIdOrElseThrow(id)));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid RestaurantModel.Input input) {
        Restaurant created = commandService.create(disassembler.toDomain(input));
        RestaurantModel.Output output = assembler.toOutput(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(output.getId()).
                toUri();
        return ResponseEntity.created(location).body(output);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantModel.Output> update(
            @PathVariable Long id, @RequestBody @Valid RestaurantModel.Input input) {
        Restaurant current = queryService.findByIdOrElseThrow(id);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantModel.Output> updatePartial(
            @PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        verifyMapContainsOnlyFieldsOfClass(patchMap, RestaurantModel.Input.class);
        Restaurant current = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }

    @GetMapping("/custom")
    public List<RestaurantModel.Summary> customQueryTest(@RequestBody Map<String, Object> map) {
        return assembler.toCollectionModel(queryService.customQuery(map));
    }

    @GetMapping("/with-free-delivery")
    public List<RestaurantModel.Summary> listRestaurantsWithFreeDelivery() {
        return assembler.toCollectionModel(queryService.findAllWithFreeDelivery());
    }

    @GetMapping("/first")
    public ResponseEntity<RestaurantModel.Output> getFirst() {
        return ResponseEntity.ok(assembler.toOutput(queryService.findFirst()));
    }

    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Long id) {
        commandService.activateRestaurant(id);
    }

    @DeleteMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactivate(@PathVariable Long id) {
        commandService.inactivateRestaurant(id);
    }

    @PutMapping("/activations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void multipleActivation(@RequestBody List<Long> restaurantIds) {
        try {
            commandService.activateRestaurant(restaurantIds);
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/activations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void multipleInactivation(@RequestBody List<Long> restaurantIds) {
        try {
            commandService.inactivateRestaurant(restaurantIds);
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}/opening")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void open(@PathVariable Long id) {
        commandService.openRestaurant(id);
    }

    @PutMapping("/{id}/closing")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void close(@PathVariable Long id) {
        commandService.closeRestaurant(id);
    }
}