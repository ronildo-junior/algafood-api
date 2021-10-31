package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.model.UserModel;
import com.ronijr.algafoodapi.core.AbstractTestRest;
import com.ronijr.algafoodapi.core.DataTest;
import com.ronijr.algafoodapi.domain.model.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static com.ronijr.algafoodapi.core.ResourceUtils.getContentFromResource;
import static com.ronijr.algafoodapi.core.ResourceUtils.getObjectFromJson;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class UserRestIT extends AbstractTestRest {
    private final DataTest testData;
    private final String USER_TO_UPDATE_PATH = "/json/valid/user-update.json";

    @Autowired
    UserRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = "/v1/users";
        cleaner.clearTables();
        testData.createUserBaseData();
    }

    @Test
    void shouldStatus200AndResponseBody_WhenGettingExistingUserById() {
        final int id = DataTest.USER_COUNT;
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(testData.getUserName(id)));
    }

    @Test
    void shouldStatus404AndResponseBody_WhenGettingNonExistingUserById() {
        given().
            pathParam("id", DataTest.USER_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus201AndResponseBody_WhenCreatingValidUser() {
        final String USER_TO_CREATE_PATH = "/json/valid/user-create.json";
        final UserModel.Create user =
                (UserModel.Create) getObjectFromJson(USER_TO_CREATE_PATH, UserModel.Create.class);
        assert user != null;
        given().
            body(user).contentType(ContentType.JSON).accept(ContentType.JSON).
        when().
            post().
        then().
            statusCode(HttpStatus.CREATED.value()).
            body("name", equalTo(user.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenCreatingUserWithBlankName(final String name) {
        given().
            body(User.builder().name(name).build()).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenUpdatingValidUser() {
        final UserModel.Update user =
                (UserModel.Update) getObjectFromJson(this.USER_TO_UPDATE_PATH, UserModel.Update.class);
        assert user != null;
        given().
            pathParam("id", DataTest.USER_COUNT).
            body(user).contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("name", equalTo(user.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenUpdatingUserWithBlankName(final String name) {
        final User user = User.builder().name(name).build();
        given().
            pathParam("id", DataTest.USER_COUNT).
            body(user).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus204AndResponseBody_WhenUpdatingPasswordCorrect() {
        final String json = getContentFromResource("/json/valid/password.json");
        given().
            pathParam("id", DataTest.USER_COUNT).
            body(json).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}/password").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus400AndResponseBody_WhenUpdatingPasswordIncorrect() {
        final String json = getContentFromResource("/json/invalid/password.json");
        given().
            pathParam("id", DataTest.USER_COUNT).
            body(json).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}/password").
        then().
            assertThat().
                statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenPatchingValidUser() {
        final UserModel.Update user =
                (UserModel.Update) getObjectFromJson(this.USER_TO_UPDATE_PATH, UserModel.Update.class);
        assert user != null;
        given().
            pathParam("id", DataTest.USER_COUNT).
            body(user).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("name", equalTo(user.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenPatchingUserWithBlankName(final String name) {
        final User user = User.builder().name(name).build();
        given().
            pathParam("id", DataTest.USER_COUNT).
            body(user).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus204_WhenDeletingExistentUserNoRelationship() {
        given().
            pathParam("id", DataTest.USER_RELATIONSHIP_BEGIN - 1).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404AndResponseBody_WhenDeletingNonExistentUser() {
        given().
            pathParam("id", DataTest.USER_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }
}