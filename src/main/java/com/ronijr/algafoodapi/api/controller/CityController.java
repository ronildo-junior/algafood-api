package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.CityAssembler;
import com.ronijr.algafoodapi.api.assembler.CityDisassembler;
import com.ronijr.algafoodapi.api.model.CityResource;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.service.CityCommand;
import com.ronijr.algafoodapi.domain.service.CityQuery;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CityController {
    private final CityQuery queryService;
    private final CityCommand commandService;
    private final CityAssembler assembler;
    private final CityDisassembler disassembler;

    @GetMapping
    public List<CityResource.Summary> list() {
        return assembler.toCollectionModel(queryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResource.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toOutput(queryService.findByIdOrElseThrow(id)));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CityResource.Input input) {
        City current = disassembler.toDomain(input);
        CityResource.Output city = assembler.toOutput(commandService.create(current));
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(city.getId()).
                toUri();
        return ResponseEntity.created(location).body(city);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResource.Output> update(@PathVariable Long id, @RequestBody CityResource.Input city) {
        City current = queryService.findByIdOrElseThrow(id);
        disassembler.copyToDomainObject(city, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CityResource.Output> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        CityResource.Input input = assembler.toInput(queryService.findByIdOrElseThrow(id));
        mergeFieldsMapInObject(patchMap, input);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(disassembler.toDomain(input))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}