package com.ronijr.algafoodapi.api.v2.hateoas;

import com.ronijr.algafoodapi.api.v2.controller.CityControllerV2;
import lombok.experimental.UtilityClass;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@UtilityClass
public class AlgaLinksV2 {
    public static Link linkToCity(Long cityId, String rel) {
        return linkTo(methodOn(CityControllerV2.class).get(cityId)).withRel(rel);
    }

    public static Link linkToCity(Long cityId) {
        return linkToCity(cityId, IanaLinkRelations.SELF.value());
    }

    public static Link linkToCities(String rel) {
        return linkTo(CityControllerV2.class).withRel(rel);
    }

    public static Link linkToCities() {
        return linkToCities(IanaLinkRelations.SELF.value());
    }
}