package com.ronijr.algafoodapi.api.v1.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ronijr.algafoodapi.api.v1.assembler.RestaurantAssembler;
import com.ronijr.algafoodapi.api.v1.assembler.RestaurantDisassembler;
import com.ronijr.algafoodapi.api.v1.assembler.RestaurantSummaryAssembler;
import com.ronijr.algafoodapi.api.v1.model.RestaurantModel;
import com.ronijr.algafoodapi.api.v1.model.view.RestaurantView;
import com.ronijr.algafoodapi.config.security.CheckSecurity;
import com.ronijr.algafoodapi.domain.exception.BusinessException;
import com.ronijr.algafoodapi.domain.exception.EntityNotFoundException;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.service.command.RestaurantCommand;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ronijr.algafoodapi.api.utils.MapperUtils.mergeFieldsMapInObject;
import static com.ronijr.algafoodapi.api.utils.MapperUtils.verifyMapContainsOnlyFieldsOfClass;

@RestController
@RequestMapping(value = "/v1/restaurants", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantQuery queryService;
    private final RestaurantCommand commandService;
    private final RestaurantAssembler assembler;
    private final RestaurantSummaryAssembler assemblerSummary;
    private final RestaurantDisassembler disassembler;

    /** List all with Deep Filter for Cache, based in Update Date from Restaurant.
     *  Checks if the last modification date is greater than the one stored in the cache. */
    @GetMapping
    public ResponseEntity<CollectionModel<RestaurantModel.Summary>> list(ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
        long lastModificationDate = queryService.getLastUpdateDate().stream().
                        map(OffsetDateTime::toEpochSecond).
                        findAny().
                        orElse(0L);
        if (request.checkNotModified(lastModificationDate)) {
            return null;
        }
        return ResponseEntity.
                ok().
                cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS)).
                lastModified(lastModificationDate).
                body(assemblerSummary.toCollectionModel(queryService.findAll()));
    }

    @GetMapping(params = "projection=by-name")
    @JsonView(RestaurantView.OnlyName.class)
    public ResponseEntity<CollectionModel<RestaurantModel.Summary>> listByName(ServletWebRequest request) {
        return list(request);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantModel.Output> get(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(assembler.toModel(queryService.findByIdOrElseThrow(restaurantId)));
    }

    @CheckSecurity.Restaurants.AllowCreate
    @PostMapping
    public ResponseEntity<RestaurantModel.Output> create(@RequestBody @Valid RestaurantModel.Input input) {
        Restaurant created = commandService.create(disassembler.toDomain(input));
        RestaurantModel.Output output = assembler.toModel(created);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{restaurantId}").
                buildAndExpand(output.getId()).
                toUri();
        return ResponseEntity.created(location).body(output);
    }

    @CheckSecurity.Restaurants.AllowManage
    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantModel.Output> update(
            @PathVariable Long restaurantId, @RequestBody @Valid RestaurantModel.Input input) {
        Restaurant current = queryService.findForUpdate(restaurantId);
        disassembler.copyToDomainObject(input, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @CheckSecurity.Restaurants.AllowManage
    @PatchMapping("/{restaurantId}")
    public ResponseEntity<RestaurantModel.Output> updatePartial(
            @PathVariable Long restaurantId, @RequestBody Map<String, Object> patchMap) {
        verifyMapContainsOnlyFieldsOfClass(patchMap, RestaurantModel.Input.class);
        Restaurant current = queryService.findForUpdate(restaurantId);
        mergeFieldsMapInObject(patchMap, current);
        return ResponseEntity.ok(assembler.toModel(commandService.update(current)));
    }

    @CheckSecurity.Restaurants.AllowDelete
    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long restaurantId) {
        commandService.delete(restaurantId);
    }

    @GetMapping("/custom")
    public CollectionModel<RestaurantModel.Summary> customQueryTest(@RequestBody Map<String, Object> map) {
        return assemblerSummary.toCollectionModel(queryService.customQuery(map));
    }

    @GetMapping("/with-free-delivery")
    public CollectionModel<RestaurantModel.Summary> listRestaurantsWithFreeDelivery() {
        return assemblerSummary.toCollectionModel(queryService.findAllWithFreeDelivery());
    }

    @GetMapping("/first")
    public ResponseEntity<RestaurantModel.Output> getFirst() {
        return ResponseEntity.ok(assembler.toModel(queryService.findFirst()));
    }

    @CheckSecurity.Restaurants.AllowEdit
    @PutMapping("/{restaurantId}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> activate(@PathVariable Long restaurantId) {
        commandService.activateRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.AllowEdit
    @DeleteMapping("/{restaurantId}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> inactivate(@PathVariable Long restaurantId) {
        commandService.inactivateRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.AllowEdit
    @PutMapping("/activations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> multipleActivation(@RequestBody List<Long> restaurantIds) {
        try {
            commandService.activateRestaurant(restaurantIds);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurants.AllowEdit
    @DeleteMapping("/activations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> multipleInactivation(@RequestBody List<Long> restaurantIds) {
        try {
            commandService.inactivateRestaurant(restaurantIds);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurants.AllowManage
    @PutMapping("/{restaurantId}/opening")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> open(@PathVariable Long restaurantId) {
        commandService.openRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.AllowManage
    @PutMapping("/{restaurantId}/closing")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> close(@PathVariable Long restaurantId) {
        commandService.closeRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }
}