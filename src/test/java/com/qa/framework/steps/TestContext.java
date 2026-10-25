package com.qa.framework.steps;

import com.qa.framework.api.models.Booking;
import io.restassured.response.Response;

/** Shared per-scenario state, injected into step classes by PicoContainer. */
public class TestContext {
    public Booking booking;
    public int bookingId;
    public String token;
    public Response response;
}
