package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.CuisineAssembler;
import com.ronijr.algafoodapi.api.assembler.CuisineDisassembler;
import com.ronijr.algafoodapi.api.model.CuisineModel;
import com.ronijr.algafoodapi.domain.model.Cuisine;
import com.ronijr.algafoodapi.domain.service.CuisineCommand;
import com.ronijr.algafoodapi.domain.service.CuisineQuery;
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
@RequestMapping("/cuisines")
@AllArgsConstructor
public class CuisineController {
    private final CuisineCommand commandService;
    private final CuisineQuery queryService;
    private final CuisineAssembler assembler;
    private final CuisineDisassembler disassembler;

    @GetMapping
    public List<CuisineModel.Output> list() {
        return assembler.toCollectionModel(queryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuisineModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toOutput(queryService.findByIdOrElseThrow(id)));
    }

    @GetMapping("/by-name")
    public List<CuisineModel.Output> cuisinesByName(@RequestParam String name) {
        return assembler.toCollectionModel(queryService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid CuisineModel.Input input) {
        Cuisine created = commandService.create(disassembler.toDomain(input));
        CuisineModel.Output response = assembler.toOutput(created);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuisineModel.Output> update(@PathVariable Long id, @RequestBody @Valid CuisineModel.Input input) {
        Cuisine current = queryService.findByIdOrElseThrow(id);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CuisineModel.Output> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        verifyMapContainsOnlyFieldsOfClass(patchMap, CuisineModel.Input.class);
        Cuisine current = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, current);
        return ResponseEntity.ok(assembler.toOutput(commandService.update(current)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}