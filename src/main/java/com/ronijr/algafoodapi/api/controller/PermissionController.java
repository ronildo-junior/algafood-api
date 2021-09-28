package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.PermissionAssembler;
import com.ronijr.algafoodapi.api.model.PermissionModel;
import com.ronijr.algafoodapi.domain.service.query.PermissionQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PermissionController {
    private final PermissionQuery queryService;
    private final PermissionAssembler assembler;

    @GetMapping
    public CollectionModel<PermissionModel.Output> list() {
        return assembler.toCollectionModel(queryService.findAll());
    }
}