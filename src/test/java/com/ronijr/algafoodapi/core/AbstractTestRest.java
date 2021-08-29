package com.ronijr.algafoodapi.core;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static com.ronijr.algafoodapi.core.ResourceUtils.getContentFromResource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public abstract class AbstractTestRest extends AbstractTest {
    @LocalServerPort
    protected int randomPort;
    protected final String STATUS_PROPERTY = "status";
    protected final String INVALID = "1nv@lid";
    protected final String BAD_FORMAT_JSON = "/json/invalid/bad_format.json";

    @Test
    protected void shouldStatus200_WhenListingAll() {
        given().accept(ContentType.JSON).
        when().get().
        then().statusCode(HttpStatus.OK.value());
    }

    @Test
    protected final void shouldStatus400AndResponseBody_WhenInvalidPath() {
        given().
            basePath(this.INVALID).
        when().get().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
        body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    protected final void shouldStatus400AndResponseBody_WhenGettingByIdInvalidType() {
        given().
            pathParam("id", this.INVALID).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().get("/{id}").
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    protected final void shouldStatus400_WhenReceiveIdInPost() {
        given().
            body("{\"id\" : \"1\"}").
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    protected final void shouldStatus400AndResponseBody_WhenReceiveInvalidJson() {
        String content = getContentFromResource(BAD_FORMAT_JSON);
        given().
            body(content).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    protected final void shouldStatus400AndResponseBody_WhenCreatingNoBody() {
        given().
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
        when().post().
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    protected final void shouldStatus400AndResponseBody_WhenUpdatingNoBody() {
        given().
            pathParam("id", 1).
            accept(ContentType.JSON).
        when().put("/{id}").
        then().
            statusCode(HttpStatus.BAD_REQUEST.value()).
            body(this.STATUS_PROPERTY, equalTo(HttpStatus.BAD_REQUEST.value()));
    }
}