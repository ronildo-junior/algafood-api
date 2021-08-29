package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.core.AbstractTestRest;
import com.ronijr.algafoodapi.core.DataTest;
import com.ronijr.algafoodapi.domain.model.Restaurant;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static com.ronijr.algafoodapi.core.ResourceUtils.getObjectFromJson;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class RestaurantRestIT extends AbstractTestRest {
    private final DataTest testData;
    private final String RESTAURANT_VALID_PATH = "/json/valid/restaurant.json";

    @Autowired
    RestaurantRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = "/restaurants";
        cleaner.clearTables();
        testData.createRestaurantBaseData();
    }

    @Test
    void shouldContainCountRestaurants_WhenListingAllRestaurants() {
        given().accept(ContentType.JSON).
        when().get().
        then().body("", hasSize(DataTest.RESTAURANT_COUNT));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenGettingExistingRestaurantById() {
        int id = DataTest.RESTAURANT_COUNT;
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(testData.getRestaurantName(id)));
    }

    @Test
    void shouldStatus404AndResponseBody_WhenGettingNonExistingRestaurantById() {
        given().
            pathParam("id", DataTest.RESTAURANT_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus201AndResponseBody_WhenCreatingValidRestaurant() {
        Restaurant restaurant = (Restaurant) getObjectFromJson(this.RESTAURANT_VALID_PATH, Restaurant.class);
        assert restaurant != null;
        given().
            body(restaurant).contentType(ContentType.JSON).accept(ContentType.JSON).
        when().
            post().
        then().
            statusCode(HttpStatus.CREATED.value()).
            body("name", equalTo(restaurant.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenCreatingRestaurantWithBlankName(final String name) {
        given().
            body(Restaurant.builder().name(name).build()).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenUpdatingValidRestaurant() {
        Restaurant restaurant = (Restaurant) getObjectFromJson(this.RESTAURANT_VALID_PATH, Restaurant.class);
        assert restaurant != null;
        given().
            pathParam("id", DataTest.RESTAURANT_COUNT).
            body(restaurant).contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(restaurant.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenUpdatingRestaurantWithBlankName(final String name) {
        final Restaurant restaurant = Restaurant.builder().name(name).build();
        given().
            pathParam("id", DataTest.RESTAURANT_COUNT).
            body(restaurant).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenPatchingValidRestaurant() {
        Restaurant restaurant = (Restaurant) getObjectFromJson(this.RESTAURANT_VALID_PATH, Restaurant.class);
        assert restaurant != null;
        given().
            pathParam("id", DataTest.RESTAURANT_COUNT).
            body(restaurant).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(restaurant.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenPatchingRestaurantWithBlankName(final String name) {
        final Restaurant restaurant = Restaurant.builder().name(name).build();
        given().
            pathParam("id", DataTest.RESTAURANT_COUNT).
            body(restaurant).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus204_WhenDeletingExistentRestaurant() {
        given().
            pathParam("id", DataTest.RESTAURANT_COUNT).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404AndResponseBody_WhenDeletingNonExistentRestaurant() {
        given().
            pathParam("id", DataTest.RESTAURANT_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }
}