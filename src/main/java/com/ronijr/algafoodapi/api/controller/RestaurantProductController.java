package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.assembler.ProductAssembler;
import com.ronijr.algafoodapi.api.assembler.ProductDisassembler;
import com.ronijr.algafoodapi.api.model.ProductModel;
import com.ronijr.algafoodapi.domain.model.Product;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.service.command.ProductCommand;
import com.ronijr.algafoodapi.domain.service.query.ProductQuery;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "restaurants/{restaurantId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RestaurantProductController {
    private final RestaurantQuery restaurantQuery;
    private final ProductCommand productCommand;
    private final ProductQuery productQuery;
    private final ProductAssembler productAssembler;
    private final ProductDisassembler productDisassembler;

    @GetMapping
    public List<ProductModel.Output> listActive(@PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantQuery.findByIdOrElseThrow(restaurantId);
        return productAssembler.toCollectionModel(productQuery.findAllActive(restaurant.getId()));
    }

    @GetMapping(params = "includeInactive")
    public List<ProductModel.Output> listAll(@RequestParam boolean includeInactive, @PathVariable Long restaurantId) {
        if (includeInactive) {
            return productAssembler.toCollectionModel(restaurantQuery.getProductList(restaurantId));
        }
        return listActive(restaurantId);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductModel.Output> get(@PathVariable Long restaurantId, @PathVariable Long productId) {
        return ResponseEntity.ok(productAssembler.toOutput(restaurantQuery.getProduct(restaurantId, productId)));
    }

    @PostMapping
    public ProductModel.Output create(@PathVariable Long restaurantId, @RequestBody ProductModel.Input input) {
        Product product = productDisassembler.toDomain(input);
        return productAssembler.toOutput(productCommand.create(restaurantId, product));
    }

    @PutMapping("/{productId}")
    public ProductModel.Output update(
            @PathVariable Long restaurantId, @PathVariable Long productId, @RequestBody ProductModel.Input input) {
        Product product = productDisassembler.toDomain(input);
        return productAssembler.toOutput(productCommand.update(restaurantId, productId, product));
    }

    @PutMapping("/{productId}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateProduct(@PathVariable Long restaurantId, @PathVariable Long productId) {
        productCommand.activate(productId, restaurantId);
    }

    @DeleteMapping("/{productId}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inactivateProduct(@PathVariable Long restaurantId, @PathVariable Long productId) {
        productCommand.inactivate(productId, restaurantId);
    }
}