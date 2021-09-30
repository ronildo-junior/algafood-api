package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.assembler.ProductAssembler;
import com.ronijr.algafoodapi.api.v1.assembler.ProductDisassembler;
import com.ronijr.algafoodapi.api.v1.model.ProductModel;
import com.ronijr.algafoodapi.domain.model.Product;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import com.ronijr.algafoodapi.domain.service.command.ProductCommand;
import com.ronijr.algafoodapi.domain.service.query.ProductQuery;
import com.ronijr.algafoodapi.domain.service.query.RestaurantQuery;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ronijr.algafoodapi.api.v1.hateoas.AlgaLinks.linkToProducts;

@RestController
@RequestMapping(value = "restaurants/{restaurantId}/products",
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
@AllArgsConstructor
public class RestaurantProductController {
    private final RestaurantQuery restaurantQuery;
    private final ProductCommand productCommand;
    private final ProductQuery productQuery;
    private final ProductAssembler productAssembler;
    private final ProductDisassembler productDisassembler;

    @GetMapping
    public CollectionModel<ProductModel.Output> listActive(@PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantQuery.findByIdOrElseThrow(restaurantId);
        return productAssembler.toCollectionModel(productQuery.findAllActive(restaurant.getId()))
                .add(linkToProducts(restaurantId));
    }

    @GetMapping(params = "includeInactive")
    public CollectionModel<ProductModel.Output> listAll(@RequestParam(required = false, defaultValue = "false") Boolean includeInactive, @PathVariable Long restaurantId) {
        if (Boolean.TRUE.equals(includeInactive)) {
            return productAssembler.toCollectionModel(restaurantQuery.getProductList(restaurantId))
                    .add(linkToProducts(restaurantId));
        }
        return listActive(restaurantId);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductModel.Output> get(@PathVariable Long restaurantId, @PathVariable Long productId) {
        return ResponseEntity.ok(productAssembler.toModel(restaurantQuery.getProduct(restaurantId, productId)));
    }

    @PostMapping
    public ProductModel.Output create(@PathVariable Long restaurantId, @RequestBody ProductModel.Input input) {
        Product product = productDisassembler.toDomain(input);
        return productAssembler.toModel(productCommand.create(restaurantId, product));
    }

    @PutMapping("/{productId}")
    public ProductModel.Output update(
            @PathVariable Long restaurantId, @PathVariable Long productId, @RequestBody ProductModel.Input input) {
        Product product = productDisassembler.toDomain(input);
        return productAssembler.toModel(productCommand.update(restaurantId, productId, product));
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