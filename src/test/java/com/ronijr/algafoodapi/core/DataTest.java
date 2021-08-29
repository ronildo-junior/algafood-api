package com.ronijr.algafoodapi.core;

import com.ronijr.algafoodapi.domain.model.*;
import com.ronijr.algafoodapi.domain.repository.CityRepository;
import com.ronijr.algafoodapi.domain.repository.CuisineRepository;
import com.ronijr.algafoodapi.domain.repository.RestaurantRepository;
import com.ronijr.algafoodapi.domain.repository.StateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public final class DataTest {
    private final CuisineRepository cuisineRepository;
    private final RestaurantRepository restaurantRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    public final static Integer CUISINE_COUNT = 6;
    public final static Integer CUISINE_RELATIONSHIP_BEGIN = 3;
    public final static Integer CUISINE_NON_EXISTENT_ID = CUISINE_COUNT + 1;
    public final static Integer RESTAURANT_COUNT = 6;
    public final static Integer RESTAURANT_RELATIONSHIP_BEGIN = 3;
    public final static Integer RESTAURANT_NON_EXISTENT_ID = RESTAURANT_COUNT + 1;
    public final static Integer STATE_COUNT = 6;
    public final static Integer STATE_RELATIONSHIP_BEGIN = 3;
    public final static Integer STATE_NON_EXISTENT_ID = STATE_COUNT + 1;

    public Cuisine createCuisine(int id) {
        Cuisine cuisine = Cuisine.builder().name(getCuisineName(id)).build();
        return cuisineRepository.save(cuisine);
    }

    public State createState(int id) {
        State state = State.builder().
                name(getStateName(id)).
                abbreviation("XX").
                build();
        return stateRepository.save(state);
    }

    public City createCity(int id) {
        City city = City.builder().
                name(getCityName(id)).
                state(createState(id)).
                build();
        return cityRepository.save(city);
    }

    public City createCity(int id, State state) {
        City city = City.builder().
                name(getCityName(id)).
                state(state).
                build();
        return cityRepository.save(city);
    }

    public Address createAddress(int id) {
        return Address.builder().
                postalCode("" + id).
                addressName("Street " + id).
                number("" + id).
                neighborhood("Neighborhood " + id).
                city(createCity(id)).
                build();
    }

    public Restaurant createRestaurant(int id) {
        Restaurant restaurant = Restaurant.builder().
                name(getRestaurantName(id)).
                deliveryFee(new BigDecimal("" + id)).
                cuisine(createCuisine(id)).
                address(createAddress(id)).
                build();
        return restaurantRepository.save(restaurant);
    }

    public Restaurant createRestaurant(int id, Cuisine cuisine) {
        Restaurant restaurant = Restaurant.builder().
                name(getRestaurantName(id)).
                deliveryFee(new BigDecimal("" + id)).
                cuisine(cuisine).
                address(createAddress(id)).
                build();
        return restaurantRepository.save(restaurant);
    }

    public Restaurant createRestaurant(int id, Cuisine cuisine, Address address) {
        Restaurant restaurant = Restaurant.builder().
                name(getRestaurantName(id)).
                deliveryFee(new BigDecimal("" + id)).
                cuisine(cuisine).
                address(address).
                build();
        return restaurantRepository.save(restaurant);
    }

    public String getCuisineName(int id){
        return "Cuisine Test" + id;
    }

    public String getRestaurantName(int id){
        return "Restaurant Test" + id;
    }

    public String getStateName(int id){
        return "State Test" + id;
    }

    public String getCityName(int id){
        return "City Test" + id;
    }

    public void createCuisineBaseData() {
        for (int i = 1; i <= CUISINE_COUNT; i++) {
            Cuisine cuisine = createCuisine(i);
            if (i >= CUISINE_RELATIONSHIP_BEGIN) {
                createRestaurant(i + 1 - CUISINE_RELATIONSHIP_BEGIN, cuisine);
            }
        }
    }

    public void createRestaurantBaseData() {
        for (int i = 1; i <= RESTAURANT_COUNT; i++) {
            createRestaurant(i);
        }
    }

    public void createStateBaseData() {
        for (int i = 1; i <= STATE_COUNT; i++) {
            State state = createState(i);
            if (i >= STATE_RELATIONSHIP_BEGIN) {
                createCity(i + 1 - STATE_RELATIONSHIP_BEGIN, state);
            }
        }
    }
}