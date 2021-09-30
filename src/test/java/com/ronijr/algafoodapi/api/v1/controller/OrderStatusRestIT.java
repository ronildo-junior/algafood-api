package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.core.AbstractTest;
import com.ronijr.algafoodapi.core.DataTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

class OrderStatusRestIT extends AbstractTest {
    @LocalServerPort
    protected int randomPort;
    private final DataTest testData;

    @Autowired
    OrderStatusRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = "/orders";
        cleaner.clearTables();
        testData.createOrderBaseData();
    }

    @Test
    void shouldStatus204_WhenConfirmCreatedOrder() {
        final String id = testData.getUUID(1);
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            put("/{id}/confirmation").
        then().
            statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus204_WhenCancelCreatedOrder() {
        final String id = testData.getUUID(1);
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            put("/{id}/cancellation").
        then().
            statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus204_WhenDeliveryConfirmedOrder() {
        final String id = testData.getUUID(DataTest.ORDER_COUNT - 1);
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            put("/{id}/delivery").
        then().
            statusCode(HttpStatus.NO_CONTENT.value());
    }
}