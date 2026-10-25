package com.qa.framework.tests.api;

import com.qa.framework.api.BookingApi;
import com.qa.framework.api.models.Booking;
import com.qa.framework.data.BookingFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@Epic("API")
@Feature("Bookings")
public class BookingApiTest {

    private static final String SCHEMA = "schemas/booking-schema.json";

    @Test(groups = {"smoke", "regression"})
    @Description("POST /booking returns an id and echoes the payload")
    public void createBookingReturnsIdAndEchoesPayload() {
        Booking booking = BookingFactory.random();

        BookingApi.create(booking).then()
                .statusCode(200)
                .body("bookingid", greaterThan(0))
                .body("booking.firstname", equalTo(booking.firstname()))
                .body("booking.lastname", equalTo(booking.lastname()))
                .body("booking.totalprice", equalTo(booking.totalprice()))
                .body("booking.bookingdates.checkin", equalTo(booking.bookingdates().checkin()));
    }

    @Test(groups = "regression")
    @Description("GET /booking/{id} response matches the JSON contract")
    public void getBookingMatchesSchema() {
        int id = BookingApi.create(BookingFactory.random()).jsonPath().getInt("bookingid");

        BookingApi.get(id).then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(SCHEMA));
    }

    @Test(groups = "regression")
    @Description("PUT /booking/{id} with a valid token updates the booking")
    public void updateBookingWithTokenChangesFields() {
        Booking original = BookingFactory.random();
        int id = BookingApi.create(original).jsonPath().getInt("bookingid");
        String token = BookingApi.getToken();

        BookingApi.update(id, token, original.withFirstname("Updated")).then()
                .statusCode(200)
                .body("firstname", equalTo("Updated"));

        BookingApi.get(id).then().body("firstname", equalTo("Updated"));
    }

    @Test(groups = "regression")
    @Description("PUT without a token is rejected")
    public void updateBookingWithoutTokenIsForbidden() {
        Booking booking = BookingFactory.random();
        int id = BookingApi.create(booking).jsonPath().getInt("bookingid");

        BookingApi.updateWithoutAuth(id, booking.withFirstname("Hacker")).then().statusCode(403);
    }

    @Test(groups = "regression")
    @Description("A deleted booking returns 404 afterwards")
    public void deletedBookingIsNotFound() {
        int id = BookingApi.create(BookingFactory.random()).jsonPath().getInt("bookingid");
        String token = BookingApi.getToken();

        // Restful-Booker returns 201 for a successful delete (API quirk)
        Response deleted = BookingApi.delete(id, token);
        deleted.then().statusCode(201);

        BookingApi.get(id).then().statusCode(404);
    }
}
