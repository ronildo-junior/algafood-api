package com.ronijr.algafoodapi.api.controller;

import com.ronijr.algafoodapi.core.AbstractTest;
import com.ronijr.algafoodapi.core.DataTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

class UserGroupUserRestIT extends AbstractTest {
    @LocalServerPort
    protected int randomPort;
    private final DataTest testData;

    @Autowired
    UserGroupUserRestIT(DataTest dataTest) {
        this.testData = dataTest;
    }

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = randomPort;
        RestAssured.basePath = String.format("/users/%s/user-groups", DataTest.USER_COUNT);
        cleaner.clearTables();
        testData.createUserBaseData();
    }

    @Test
    void shouldStatus204_WhenLinkingExistingUserGroupToUser() {
        given().
            pathParam("userGroupId", DataTest.USER_GROUP_RELATIONSHIP_BEGIN).
        when().put("/{userGroupId}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404_WhenLinkingNonExistingUserGroupToUser() {
        given().
            pathParam("userGroupId", DataTest.USER_GROUP_COUNT).
        when().put("/{userGroupId}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldStatus204_WhenUnlinkingExistingUserGroupToUser() {
        given().
            pathParam("userGroupId", DataTest.USER_GROUP_RELATIONSHIP_BEGIN).
        when().put("/{userGroupId}").
        then().
            assertThat().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldStatus404_WhenUnlinkingUserGroupNonExistingToUser() {
        given().
            pathParam("userGroupId", DataTest.USER_GROUP_COUNT).
        when().put("/{userGroupId}").
        then().
            assertThat().
                statusCode(HttpStatus.NOT_FOUND.value());
    }
}