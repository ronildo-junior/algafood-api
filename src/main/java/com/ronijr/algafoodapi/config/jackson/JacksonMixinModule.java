package com.ronijr.algafoodapi.config.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ronijr.algafoodapi.api.model.mixin.*;
import com.ronijr.algafoodapi.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public final class JacksonMixinModule extends SimpleModule {
    public JacksonMixinModule() {
        setMixInAnnotation(Address.class, AddressMixin.class);
        setMixInAnnotation(City.class, CityMixin.class);
        setMixInAnnotation(Cuisine.class, CuisineMixin.class);
        setMixInAnnotation(Restaurant.class, RestaurantMixin.class);
        setMixInAnnotation(User.class, UserMixin.class);
        setMixInAnnotation(UserGroup.class, UserGroupMixin.class);
    }
}