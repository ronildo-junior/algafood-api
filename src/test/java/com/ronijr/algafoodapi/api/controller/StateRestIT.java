package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.api.model.StateModel;
import com.ronijr.algafoodapi.core.AbstractTestRest;
import com.ronijr.algafoodapi.core.DataTest;
import com.ronijr.algafoodapi.domain.model.State;
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

class StateRestIT extends AbstractTestRest {
    private final DataTest testData;
    private final String STATE_VALID_PATH = "/json/valid/state.json";

    @Autowired
    StateRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = "/states";
        cleaner.clearTables();
        testData.createStateBaseData();
    }

    @Test
    void shouldStatus200AndResponseBody_WhenGettingExistingStateById() {
        final int id = DataTest.STATE_COUNT;
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(testData.getStateName(id)));
    }

    @Test
    void shouldStatus404AndResponseBody_WhenGettingNonExistingStateById() {
        given().
            pathParam("id", DataTest.STATE_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus201AndResponseBody_WhenCreatingValidState() {
        final StateModel.Input state =
                (StateModel.Input) getObjectFromJson(this.STATE_VALID_PATH, StateModel.Input.class);
        assert state != null;
        given().
            body(state).contentType(ContentType.JSON).accept(ContentType.JSON).
        when().
            post().
        then().
            statusCode(HttpStatus.CREATED.value()).
            body("name", equalTo(state.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenCreatingStateWithBlankName(final String name) {
        given().
            body(State.builder().name(name).build()).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenUpdatingValidState() {
        final StateModel.Input state =
                (StateModel.Input) getObjectFromJson(this.STATE_VALID_PATH, StateModel.Input.class);
        assert state != null;
        given().
            pathParam("id", DataTest.STATE_COUNT).
            body(state).contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("name", equalTo(state.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenUpdatingStateWithBlankName(final String name) {
        final State state = State.builder().name(name).build();
        given().
            pathParam("id", DataTest.STATE_COUNT).
            body(state).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenPatchingValidState() {
        final StateModel.Input state =
                (StateModel.Input) getObjectFromJson(this.STATE_VALID_PATH, StateModel.Input.class);
        assert state != null;
        given().
            pathParam("id", DataTest.STATE_COUNT).
            body(state).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("name", equalTo(state.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenPatchingStateWithBlankName(final String name) {
        final State state = State.builder().name(name).build();
        given().
            pathParam("id", DataTest.STATE_COUNT).
            body(state).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus204_WhenDeletingExistentStateNoRelationship() {
        given().
            pathParam("id", DataTest.STATE_RELATIONSHIP_BEGIN - 1).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404AndResponseBody_WhenDeletingNonExistentState() {
        given().
            pathParam("id", DataTest.STATE_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus409_WhenDeletingStateWithRelationship() {
        given().
            pathParam("id", DataTest.STATE_RELATIONSHIP_BEGIN).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.CONFLICT.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.CONFLICT.value()));
    }
}