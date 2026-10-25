package com.qa.framework.tests.db;

import com.qa.framework.api.BookingApi;
import com.qa.framework.api.models.Booking;
import com.qa.framework.data.BookingFactory;
import com.qa.framework.db.BookingRepository;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * SQL data-integrity checks.
 * The public demo API does not expose its own database, so API responses are mirrored
 * into H2 and validated with SQL. Point db.url at a real MySQL/Oracle schema to run the
 * same style of checks against an actual system.
 */
@Epic("Database")
@Feature("Data integrity")
public class BookingDbTest {

    @BeforeMethod(alwaysRun = true)
    public void resetTable() {
        BookingRepository.reset();
    }

    @Test(groups = {"smoke", "regression"})
    @Description("A booking created through the API is stored with identical values")
    public void apiBookingIsStoredWithMatchingValues() {
        Booking booking = BookingFactory.random();
        int id = BookingApi.create(booking).jsonPath().getInt("bookingid");

        BookingRepository.save(id, booking);

        Assert.assertEquals(
                BookingRepository.queryForLong("SELECT total_price FROM bookings WHERE id = ?", id),
                booking.totalprice());
        Assert.assertEquals(
                BookingRepository.queryForString("SELECT firstname FROM bookings WHERE id = ?", id),
                booking.firstname());
    }

    @Test(groups = "regression")
    @Description("No stored booking has a checkout date on or before its checkin date")
    public void checkoutIsAlwaysAfterCheckin() {
        for (int i = 1; i <= 5; i++) {
            BookingRepository.save(i, BookingFactory.random());
        }
        long invalid = BookingRepository.queryForLong("SELECT COUNT(*) FROM bookings WHERE checkout <= checkin");
        Assert.assertEquals(invalid, 0L, "Found bookings with invalid date ranges");
    }

    @Test(groups = "regression")
    @Description("Aggregate totals in SQL match the source data")
    public void aggregateTotalsMatchSourceData() {
        long expectedSum = 0;
        for (int i = 1; i <= 3; i++) {
            Booking b = BookingFactory.random();
            BookingRepository.save(i, b);
            expectedSum += b.totalprice();
        }
        Assert.assertEquals(BookingRepository.queryForLong("SELECT COUNT(*) FROM bookings"), 3L);
        Assert.assertEquals(BookingRepository.queryForLong("SELECT SUM(total_price) FROM bookings"), expectedSum);
    }
}
