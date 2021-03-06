package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.assembler.CityAssembler;
import com.ronijr.algafoodapi.api.v1.assembler.CityDisassembler;
import com.ronijr.algafoodapi.api.v1.assembler.CitySummaryAssembler;
import com.ronijr.algafoodapi.api.v1.model.CityModel;
import com.ronijr.algafoodapi.config.security.CheckSecurity;
import com.ronijr.algafoodapi.domain.model.City;
import com.ronijr.algafoodapi.domain.service.command.CityCommand;
import com.ronijr.algafoodapi.domain.service.query.CityQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.util.Map;

import static com.ronijr.algafoodapi.api.utils.MapperUtils.mergeFieldsMapInObject;
import static com.ronijr.algafoodapi.api.utils.MapperUtils.verifyMapContainsOnlyFieldsOfClass;

@RestController
@RequestMapping(value = "/v1/cities", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class CityController {
    private final CityQuery queryService;
    private final CityCommand commandService;
    private final CityAssembler assembler;
    private final CitySummaryAssembler assemblerSummary;
    private final CityDisassembler disassembler;

    @CheckSecurity.Cities.AllowRead
    @Deprecated
    @GetMapping
    public CollectionModel<CityModel.Summary> list() {
        return assemblerSummary.toCollectionModel(queryService.findAll());
    }

    @CheckSecurity.Cities.AllowRead
    @Deprecated
    @GetMapping("/{id}")
    public ResponseEntity<CityModel.Output> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(queryService.findByIdOrElseThrow(id)));
    }

    @CheckSecurity.Cities.AllowCreate
    @Deprecated
    @PostMapping
    public ResponseEntity<CityModel.Output> create(@RequestBody @Valid CityModel.Input input) {
        City created = commandService.create(disassembler.toDomain(input));
        CityModel.Output output = assembler.toModel(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(output.getId()).
                toUri();
        return ResponseEntity.created(location).body(output);
    }

    @CheckSecurity.Cities.AllowEdit
    @Deprecated
    @PutMapping("/{id}")
    public ResponseEntity<CityModel.Output> update(@PathVariable Long id, @RequestBody @Valid CityModel.Input input) {
        City current = queryService.findForUpdate(id);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @CheckSecurity.Cities.AllowEdit
    @Deprecated
    @PatchMapping("/{id}")
    public ResponseEntity<CityModel.Output> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        verifyMapContainsOnlyFieldsOfClass(patchMap, CityModel.Input.class);
        City current = queryService.findForUpdate(id);
        mergeFieldsMapInObject(patchMap, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @CheckSecurity.Cities.AllowDelete
    @Deprecated
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}