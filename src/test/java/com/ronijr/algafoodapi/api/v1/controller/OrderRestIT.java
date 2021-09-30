package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.model.OrderModel;
import com.ronijr.algafoodapi.core.AbstractTest;
import com.ronijr.algafoodapi.core.DataTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static com.ronijr.algafoodapi.core.ResourceUtils.getObjectFromJson;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class OrderRestIT extends AbstractTest {
    @LocalServerPort
    protected int randomPort;
    private final DataTest testData;
    private final String STATUS_PROPERTY = "status";

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
        final String id = testData.getUUID(DataTest.ORDER_COUNT - 1);
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
                body(STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus201AndResponseBody_WhenCreatingValidOrder() {
        final String ORDER_VALID_PATH = "/json/valid/order.json";
        final OrderModel.Input order = (OrderModel.Input) getObjectFromJson(ORDER_VALID_PATH, OrderModel.Input.class);
        assert order != null;
        given().
            body(order).contentType(ContentType.JSON).accept(ContentType.JSON).
        when().
            post().
        then().
            statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void shouldStatus400AndResponseBody_WhenCreatingInvalidValidOrder() {
        given().
            body("").contentType(ContentType.JSON).accept(ContentType.JSON).
        when().
            post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }
}