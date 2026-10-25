package com.qa.framework.api;

import com.qa.framework.api.models.Booking;
import com.qa.framework.config.ConfigReader;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** Thin wrapper over the Restful-Booker endpoints. Returns raw Responses so tests own the assertions. */
public final class BookingApi {

    private BookingApi() {
    }

    @Step("Get auth token")
    public static String getToken() {
        return given().spec(ApiClient.spec())
                .body(Map.of("username", ConfigReader.get("api.username"),
                        "password", ConfigReader.get("api.password")))
                .post("/auth")
                .then().statusCode(200)
                .extract().path("token");
    }

    @Step("Create booking")
    public static Response create(Booking booking) {
        return given().spec(ApiClient.spec()).body(booking).post("/booking");
    }

    @Step("Get booking {id}")
    public static Response get(int id) {
        return given().spec(ApiClient.spec()).get("/booking/{id}", id);
    }

    @Step("Update booking {id}")
    public static Response update(int id, String token, Booking booking) {
        return given().spec(ApiClient.spec())
                .header("Cookie", "token=" + token)
                .body(booking)
                .put("/booking/{id}", id);
    }

    @Step("Update booking {id} without auth")
    public static Response updateWithoutAuth(int id, Booking booking) {
        return given().spec(ApiClient.spec()).body(booking).put("/booking/{id}", id);
    }

    @Step("Delete booking {id}")
    public static Response delete(int id, String token) {
        return given().spec(ApiClient.spec())
                .header("Cookie", "token=" + token)
                .delete("/booking/{id}", id);
    }
}
