package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.core.AbstractTestRest;
import com.ronijr.algafoodapi.core.DataTest;
import com.ronijr.algafoodapi.domain.model.City;
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

class CityRestIT extends AbstractTestRest {
    private final DataTest testData;
    private final String CITY_VALID_PATH = "/json/valid/city.json";

    @Autowired
    CityRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = "/cities";
        cleaner.clearTables();
        testData.createCityBaseData();
    }

    @Test
    void shouldStatus200AndResponseBody_WhenGettingExistingCityById() {
        int id = DataTest.CITY_COUNT;
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(testData.getCityName(id)));
    }

    @Test
    void shouldStatus404AndResponseBody_WhenGettingNonExistingCityById() {
        given().
            pathParam("id", DataTest.CITY_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus201AndResponseBody_WhenCreatingValidCity() {
        City city = (City) getObjectFromJson(this.CITY_VALID_PATH, City.class);
        assert city != null;
        given().
            body(city).contentType(ContentType.JSON).accept(ContentType.JSON).
        when().
            post().
        then().
            statusCode(HttpStatus.CREATED.value()).
            body("name", equalTo(city.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenCreatingCityWithBlankName(final String name) {
        given().
            body(City.builder().name(name).build()).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenUpdatingValidCity() {
        City city = (City) getObjectFromJson(this.CITY_VALID_PATH, City.class);
        assert city != null;
        given().
            pathParam("id", DataTest.CITY_COUNT).
            body(city).contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("name", equalTo(city.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenUpdatingCityWithBlankName(final String name) {
        final City city = City.builder().name(name).build();
        given().
            pathParam("id", DataTest.CITY_COUNT).
            body(city).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenPatchingValidCity() {
        City city = (City) getObjectFromJson(this.CITY_VALID_PATH, City.class);
        assert city != null;
        given().
            pathParam("id", DataTest.CITY_COUNT).
            body(city).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("name", equalTo(city.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenPatchingCityWithBlankName(final String name) {
        final City city = City.builder().name(name).build();
        given().
            pathParam("id", DataTest.CITY_COUNT).
            body(city).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus204_WhenDeletingExistentCity() {
        given().
            pathParam("id", DataTest.CITY_COUNT).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404AndResponseBody_WhenDeletingNonExistentCity() {
        given().
            pathParam("id", DataTest.CITY_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }
}