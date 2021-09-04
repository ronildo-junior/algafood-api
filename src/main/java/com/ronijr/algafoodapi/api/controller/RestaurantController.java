package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.RestaurantAssembler;
import com.ronijr.algafoodapi.api.assembler.RestaurantDisassembler;
import com.ronijr.algafoodapi.api.model.RestaurantModel;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.service.RestaurantCommand;
import com.ronijr.algafoodapi.domain.service.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.ronijr.algafoodapi.api.utils.MapperUtils.mergeFieldsMapInObject;

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

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toOutput(queryService.findByIdOrElseThrow(id)));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody RestaurantModel.Input input) {
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
            @PathVariable Long id, @RequestBody RestaurantModel.Input input) {
        Restaurant current = queryService.findByIdOrElseThrow(id);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantModel.Output> updatePartial(
            @PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        Restaurant current = queryService.findByIdOrElseThrow(id);
        RestaurantModel.Input input = assembler.toInput(current);
        mergeFieldsMapInObject(patchMap, input);
        disassembler.copyToDomainObject(input, current);
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
        commandService.activate(id);
    }

    @DeleteMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactivate(@PathVariable Long id) {
        commandService.inactivate(id);
    }
}