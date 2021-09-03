package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.model.CuisineModel;
import com.ronijr.algafoodapi.core.AbstractTestRest;
import com.ronijr.algafoodapi.core.DataTest;
import com.ronijr.algafoodapi.domain.model.Cuisine;
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

class CuisineRestIT extends AbstractTestRest {
    private final DataTest testData;
    private final String CUISINE_VALID_PATH = "/json/valid/cuisine.json";

    @Autowired
    CuisineRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = "/cuisines";
        cleaner.clearTables();
        testData.createCuisineBaseData();
    }

    @Test
    void shouldStatus200AndContainCountCuisines_WhenListingAllByName() {
        given().
            queryParam("name", "Cuisine").
            accept(ContentType.JSON).
        when().get("/by-name").
        then().
            statusCode(HttpStatus.OK.value()).
            body("", hasSize(DataTest.CUISINE_COUNT));
    }

    @Test
    void shouldContainCountCuisines_WhenListingAllCuisines() {
        given().accept(ContentType.JSON).
        when().get().
        then().body("", hasSize(DataTest.CUISINE_COUNT));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenGettingExistingCuisineById() {
        final int id = DataTest.CUISINE_COUNT;
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(testData.getCuisineName(id)));
    }

    @Test
    void shouldStatus404AndResponseBody_WhenGettingNonExistingCuisineById() {
        given().
            pathParam("id", DataTest.CUISINE_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus201AndResponseBody_WhenCreatingValidCuisine() {
        final CuisineModel.Input cuisine =
                (CuisineModel.Input) getObjectFromJson(this.CUISINE_VALID_PATH, CuisineModel.Input.class);
        assert cuisine != null;
        given().
            body(cuisine).contentType(ContentType.JSON).accept(ContentType.JSON).
        when().
            post().
        then().
            statusCode(HttpStatus.CREATED.value()).
            body("name", equalTo(cuisine.getName()));
    }

    @Test
    void shouldStatus409_WhenCreatingExistingCuisineName() {
        final CuisineModel.Input FIRST_CUISINE_NAME = new CuisineModel.Input(testData.getCuisineName(1));
        given().
            body(FIRST_CUISINE_NAME).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.CONFLICT.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.CONFLICT.value()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenCreatingCuisineWithBlankName(final String name) {
        given().
            body(Cuisine.builder().name(name).build()).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenUpdatingValidCuisine() {
        final CuisineModel.Input cuisine =
                (CuisineModel.Input) getObjectFromJson(this.CUISINE_VALID_PATH, CuisineModel.Input.class);
        assert cuisine != null;
        given().
            pathParam("id", DataTest.CUISINE_COUNT).
            body(cuisine).contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(cuisine.getName()));
    }

    @Test
    void shouldStatus409AndResponseBody_WhenUpdatingCuisineNameWithExistingName() {
        final CuisineModel.Input FIRST_CUISINE_NAME = new CuisineModel.Input(testData.getCuisineName(1));
        given().
            pathParam("id", DataTest.CUISINE_COUNT).
            body(FIRST_CUISINE_NAME).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            statusCode(HttpStatus.CONFLICT.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.CONFLICT.value()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenUpdatingCuisineWithBlankName(final String name) {
        final Cuisine cuisine = Cuisine.builder().name(name).build();
        given().
            pathParam("id", DataTest.CUISINE_COUNT).
            body(cuisine).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenPatchingValidCuisine() {
        final CuisineModel.Input cuisine =
                (CuisineModel.Input) getObjectFromJson(this.CUISINE_VALID_PATH, CuisineModel.Input.class);
        assert cuisine != null;
        given().
            pathParam("id", DataTest.CUISINE_COUNT).
            body(cuisine).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(cuisine.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenPatchingCuisineWithBlankName(final String name) {
        final Cuisine cuisine = Cuisine.builder().name(name).build();
        given().
            pathParam("id", DataTest.CUISINE_COUNT).
            body(cuisine).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus400AndResponseBody_WhenPatchingCuisineNameWithExistingName() {
        final CuisineModel.Input FIRST_CUISINE_NAME = new CuisineModel.Input(testData.getCuisineName(1));
        given().
            pathParam("id", DataTest.CUISINE_COUNT).
            body(FIRST_CUISINE_NAME).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            statusCode(HttpStatus.CONFLICT.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.CONFLICT.value()));
    }

    @Test
    void shouldStatus204_WhenDeletingExistentCuisineNoRelationship() {
        given().
            pathParam("id", DataTest.CUISINE_RELATIONSHIP_BEGIN - 1).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404AndResponseBody_WhenDeletingNonExistentCuisine() {
        given().
            pathParam("id", DataTest.CUISINE_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus409_WhenDeletingCuisineWithRelationship() {
        given().
            pathParam("id", DataTest.CUISINE_RELATIONSHIP_BEGIN).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            statusCode(HttpStatus.CONFLICT.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.CONFLICT.value()));
    }
}