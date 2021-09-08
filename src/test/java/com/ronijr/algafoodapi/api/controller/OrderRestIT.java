package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.core.AbstractTestRest;
import com.ronijr.algafoodapi.core.DataTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class OrderRestIT extends AbstractTestRest {
    private final DataTest testData;
    private final String ORDER_VALID_PATH = "/json/valid/order.json";

    @Autowired
    OrderRestIT(DataTest dataTest) {
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
    void shouldStatus200AndResponseBody_WhenGettingExistingOrderById() {
        final int id = DataTest.ORDER_COUNT;
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldStatus404AndResponseBody_WhenGettingNonExistingOrderById() {
        given().
            pathParam("id", DataTest.ORDER_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }
}