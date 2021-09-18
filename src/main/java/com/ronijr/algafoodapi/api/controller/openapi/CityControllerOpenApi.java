package com.ronijr.algafoodapi.api.controller.openapi;

import com.ronijr.algafoodapi.api.exception.handler.ProblemDetails;
import com.ronijr.algafoodapi.api.model.CityModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Api(tags = "City")
public interface CityControllerOpenApi {
    @ApiOperation("List All Cities")
    List<CityModel.Summary> list();

    @ApiOperation("Get City by id")
    @ApiResponses({
            @ApiResponse(code = 400, message = "City id invalid", response = ProblemDetails.class),
            @ApiResponse(code = 404, message = "City not found", response = ProblemDetails.class),
    })
    ResponseEntity<CityModel.Output> get(Long id);

    @ApiOperation("Create a City")
    @ApiResponse(code = 201, message = "City created")
    ResponseEntity<CityModel.Output> create(CityModel.Input input);

    @ApiOperation("Update a City by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "City updated"),
            @ApiResponse(code = 404, message = "City not found", response = ProblemDetails.class),
    })
    ResponseEntity<CityModel.Output> update(Long id, CityModel.Input input);

    @ApiOperation("Update Partially a City by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "City updated"),
            @ApiResponse(code = 404, message = "City not found", response = ProblemDetails.class),
    })
    ResponseEntity<CityModel.Output> updatePartial(Long id, Map<String, Object> patchMap);

    @ApiOperation("Remove a City by id")
    @ApiResponses({
            @ApiResponse(code = 204, message = "City deleted"),
            @ApiResponse(code = 404, message = "City not found", response = ProblemDetails.class),
    })
    void delete(Long id);
}