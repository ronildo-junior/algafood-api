package com.ronijr.algafoodapi.api.v1.controller;

import com.ronijr.algafoodapi.api.v1.model.UserGroupModel;
import com.ronijr.algafoodapi.core.AbstractTestRest;
import com.ronijr.algafoodapi.core.DataTest;
import com.ronijr.algafoodapi.domain.model.UserGroup;
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

class UserGroupRestIT extends AbstractTestRest {
    private final DataTest testData;
    private final String USER_GROUP_VALID_PATH = "/json/valid/user-group.json";

    @Autowired
    UserGroupRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = "/user-groups";
        cleaner.clearTables();
        testData.createUserGroupBaseData();
    }

    @Test
    void shouldStatus200AndResponseBody_WhenGettingExistingUserGroupById() {
        final int id = DataTest.USER_GROUP_COUNT;
        given().
            pathParam("id", id).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            statusCode(HttpStatus.OK.value()).
            body("name", equalTo(testData.getUserGroupName(id)));
    }

    @Test
    void shouldStatus404AndResponseBody_WhenGettingNonExistingUserGroupById() {
        given().
            pathParam("id", DataTest.USER_GROUP_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            get("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldStatus201AndResponseBody_WhenCreatingValidUserGroup() {
        final UserGroupModel.Input userGroup =
                (UserGroupModel.Input) getObjectFromJson(this.USER_GROUP_VALID_PATH, UserGroupModel.Input.class);
        assert userGroup != null;
        given().
            body(userGroup).contentType(ContentType.JSON).accept(ContentType.JSON).
        when().
            post().
        then().
            statusCode(HttpStatus.CREATED.value()).
            body("name", equalTo(userGroup.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenCreatingUserGroupWithBlankName(final String name) {
        given().
            body(UserGroup.builder().name(name).build()).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenUpdatingValidUserGroup() {
        final UserGroupModel.Input userGroup =
                (UserGroupModel.Input) getObjectFromJson(this.USER_GROUP_VALID_PATH, UserGroupModel.Input.class);
        assert userGroup != null;
        given().
            pathParam("id", DataTest.USER_GROUP_COUNT).
            body(userGroup).contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("name", equalTo(userGroup.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenUpdatingUserGroupWithBlankName(final String name) {
        final UserGroup userGroup = UserGroup.builder().name(name).build();
        given().
            pathParam("id", DataTest.USER_GROUP_COUNT).
            body(userGroup).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus200AndResponseBody_WhenPatchingValidUserGroup() {
        final UserGroupModel.Input userGroup =
                (UserGroupModel.Input) getObjectFromJson(this.USER_GROUP_VALID_PATH, UserGroupModel.Input.class);
        assert userGroup != null;
        given().
            pathParam("id", DataTest.USER_GROUP_COUNT).
            body(userGroup).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.OK.value()).
                body("name", equalTo(userGroup.getName()));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void shouldStatus400AndResponseBody_WhenPatchingUserGroupWithBlankName(final String name) {
        final UserGroup userGroup = UserGroup.builder().name(name).build();
        given().
            pathParam("id", DataTest.USER_GROUP_COUNT).
            body(userGroup).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().patch("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldStatus204_WhenDeletingExistentUserGroupNoRelationship() {
        given().
            pathParam("id", DataTest.USER_GROUP_RELATIONSHIP_BEGIN - 1).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404AndResponseBody_WhenDeletingNonExistentUserGroup() {
        given().
            pathParam("id", DataTest.USER_GROUP_NON_EXISTENT_ID).
            accept(ContentType.JSON).
        when().
            delete("/{id}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value()).
                body(this.STATUS_PROPERTY, equalTo(HttpStatus.NOT_FOUND.value()));
    }
}