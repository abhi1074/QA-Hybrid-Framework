package com.qa.framework.steps;

import com.qa.framework.api.BookingApi;
import com.qa.framework.data.BookingFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class ApiSteps {

    private final TestContext ctx;

    public ApiSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("a new booking is created via the API")
    public void createBooking() {
        ctx.booking = BookingFactory.random();
        ctx.response = BookingApi.create(ctx.booking);
        ctx.bookingId = ctx.response.jsonPath().getInt("bookingid");
    }

    @Given("a valid auth token")
    public void validToken() {
        ctx.token = BookingApi.getToken();
    }

    @When("the booking is retrieved by id")
    public void retrieveBooking() {
        ctx.response = BookingApi.get(ctx.bookingId);
    }

    @When("the booking first name is updated to {string}")
    public void updateFirstName(String newName) {
        ctx.response = BookingApi.update(ctx.bookingId, ctx.token, ctx.booking.withFirstname(newName));
    }

    @When("the booking is deleted")
    public void deleteBooking() {
        ctx.response = BookingApi.delete(ctx.bookingId, ctx.token);
    }

    @Then("the API responds with status {int}")
    public void statusIs(int expected) {
        Assert.assertEquals(ctx.response.statusCode(), expected);
    }

    @Then("the response contains a booking id")
    public void responseHasBookingId() {
        Assert.assertTrue(ctx.response.jsonPath().getInt("bookingid") > 0);
    }

    @Then("the retrieved booking matches the one created")
    public void retrievedMatchesCreated() {
        Assert.assertEquals(ctx.response.jsonPath().getString("firstname"), ctx.booking.firstname());
        Assert.assertEquals(ctx.response.jsonPath().getString("lastname"), ctx.booking.lastname());
        Assert.assertEquals(ctx.response.jsonPath().getInt("totalprice"), ctx.booking.totalprice());
    }

    @Then("the response first name is {string}")
    public void responseFirstNameIs(String expected) {
        Assert.assertEquals(ctx.response.jsonPath().getString("firstname"), expected);
    }
}
