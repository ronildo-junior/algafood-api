package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.core.AbstractTest;
import com.ronijr.algafoodapi.core.DataTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

class RestaurantUserRestIT extends AbstractTest {
    @LocalServerPort
    protected int randomPort;
    private final DataTest testData;

    @Autowired
    RestaurantUserRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = String.format("/restaurants/%s/managers", DataTest.USER_COUNT);
        cleaner.clearTables();
        testData.createRestaurantBaseData();
    }

    @Test
    void shouldStatus204_WhenAddingExistingUserToRestaurant() {
        given().
            pathParam("managerId", DataTest.USER_GROUP_RELATIONSHIP_BEGIN).
        when().put("/{managerId}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404_WhenAddingNonExistingUserToRestaurant() {
        given().
            pathParam("managerId", DataTest.USER_GROUP_COUNT).
        when().put("/{managerId}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldStatus204_WhenRemovingExistingUserToRestaurant() {
        given().
            pathParam("managerId", DataTest.USER_GROUP_RELATIONSHIP_BEGIN).
        when().put("/{managerId}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404_WhenRemovingNonExistingUserToRestaurant() {
        given().
            pathParam("managerId", DataTest.USER_GROUP_COUNT).
        when().put("/{managerId}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value());
    }
}