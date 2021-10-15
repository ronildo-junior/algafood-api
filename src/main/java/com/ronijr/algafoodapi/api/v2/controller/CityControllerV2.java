package com.ronijr.algafoodapi.api.v2.controller;

import com.ronijr.algafoodapi.api.v2.assembler.CityAssemblerV2;
import com.ronijr.algafoodapi.api.v2.assembler.CityDisassemblerV2;
import com.ronijr.algafoodapi.api.v2.assembler.CitySummaryAssemblerV2;
import com.ronijr.algafoodapi.api.v2.model.CityModel;
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
@RequestMapping(value = "/v2/cities", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class CityControllerV2 {
    private final CityQuery queryService;
    private final CityCommand commandService;
    private final CityAssemblerV2 assembler;
    private final CitySummaryAssemblerV2 assemblerSummary;
    private final CityDisassemblerV2 disassembler;

    @CheckSecurity.Cities.AllowRead
    @GetMapping
    public CollectionModel<CityModel.SummaryV2> list() {
        return assemblerSummary.toCollectionModel(queryService.findAll());
    }

    @CheckSecurity.Cities.AllowRead
    @GetMapping("/{id}")
    public ResponseEntity<CityModel.OutputV2> get(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(queryService.findByIdOrElseThrow(id)));
    }

    @CheckSecurity.Cities.AllowCreate
    @PostMapping
    public ResponseEntity<CityModel.OutputV2> create(@RequestBody @Valid CityModel.InputV2 input) {
        City created = commandService.create(disassembler.toDomain(input));
        CityModel.OutputV2 outputV2 = assembler.toModel(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(outputV2.getCityId()).
                toUri();
        return ResponseEntity.created(location).body(outputV2);
    }

    @CheckSecurity.Cities.AllowEdit
    @PutMapping("/{id}")
    public ResponseEntity<CityModel.OutputV2> update(@PathVariable Long id, @RequestBody @Valid CityModel.InputV2 input) {
        City current = queryService.findByIdOrElseThrow(id);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @CheckSecurity.Cities.AllowEdit
    @PatchMapping("/{id}")
    public ResponseEntity<CityModel.OutputV2> updatePartial(@PathVariable Long id, @RequestBody Map<String, Object> patchMap) {
        verifyMapContainsOnlyFieldsOfClass(patchMap, CityModel.InputV2.class);
        City current = queryService.findByIdOrElseThrow(id);
        mergeFieldsMapInObject(patchMap, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @CheckSecurity.Cities.AllowDelete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commandService.delete(id);
    }
}