package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.model.PaymentMethodModel;
import com.ronijr.algafoodapi.core.AbstractTestRest;
import com.ronijr.algafoodapi.core.DataTest;
import com.ronijr.algafoodapi.domain.model.PaymentMethod;
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

class PaymentMethodRestIT extends AbstractTestRest {
    private final DataTest testData;
    private final String PAYMENT_METHOD_VALID_PATH = "/json/valid/payment-method.json";

    @Autowired
    PaymentMethodRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = "/payment-methods";
        cleaner.clearTables();
        testData.createPaymentMethodBaseData();
    }

    @Test
    void shouldStatus200AndResponseBody_WhenGettingExistingPaymentMethodById() {
        final int id = DataTest.PAYMENT_METHOD_COUNT;
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("description", equalTo(testData.getPaymentMethodDescription(id)));
    }

    @Test
    void shouldStatus404AndResponseBody_WhenGettingNonExistingPaymentMethodById() {
        given().
            pathParam("id", DataTest.PAYMENT_METHOD_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            assertThat().
            statusCode(HttpStatus.NOT_FOUND.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus201AndResponseBody_WhenCreatingValidPaymentMethod() {
        final PaymentMethodModel.Input paymentMethod =
                (PaymentMethodModel.Input) getObjectFromJson(this.PAYMENT_METHOD_VALID_PATH, PaymentMethodModel.Input.class);
        assert paymentMethod != null;
        given().
            body(paymentMethod).contentType(ContentType.JSON).accept(ContentType.JSON).
        when().
            post().
        then().
            statusCode(HttpStatus.CREATED.value()).
            body("description", equalTo(paymentMethod.getDescription()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenCreatingPaymentMethodWithBlankName(final String description) {
        given().
            body(PaymentMethod.builder().description(description).build()).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenUpdatingValidPaymentMethod() {
        final PaymentMethodModel.Input paymentMethod =
                (PaymentMethodModel.Input) getObjectFromJson(this.PAYMENT_METHOD_VALID_PATH, PaymentMethodModel.Input.class);
        assert paymentMethod != null;
        given().
            pathParam("id", DataTest.PAYMENT_METHOD_COUNT).
            body(paymentMethod).contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("description", equalTo(paymentMethod.getDescription()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenUpdatingPaymentMethodWithBlankName(final String description) {
        final PaymentMethod paymentMethod = PaymentMethod.builder().description(description).build();
        given().
            pathParam("id", DataTest.PAYMENT_METHOD_COUNT).
            body(paymentMethod).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenPatchingValidPaymentMethod() {
        final PaymentMethodModel.Input paymentMethod =
                (PaymentMethodModel.Input) getObjectFromJson(this.PAYMENT_METHOD_VALID_PATH, PaymentMethodModel.Input.class);
        assert paymentMethod != null;
        given().
            pathParam("id", DataTest.PAYMENT_METHOD_COUNT).
            body(paymentMethod).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("description", equalTo(paymentMethod.getDescription()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenPatchingPaymentMethodWithBlankName(final String description) {
        final PaymentMethod paymentMethod = PaymentMethod.builder().description(description).build();
        given().
            pathParam("id", DataTest.PAYMENT_METHOD_COUNT).
            body(paymentMethod).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus204_WhenDeletingExistentPaymentMethodNoRelationship() {
        given().
            pathParam("id", DataTest.PAYMENT_METHOD_RELATIONSHIP_BEGIN - 1).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404AndResponseBody_WhenDeletingNonExistentPaymentMethod() {
        given().
            pathParam("id", DataTest.PAYMENT_METHOD_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus409_WhenDeletingPaymentMethodWithRelationship() {
        given().
            pathParam("id", DataTest.PAYMENT_METHOD_RELATIONSHIP_BEGIN).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.CONFLICT.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.CONFLICT.value()));
    }
}