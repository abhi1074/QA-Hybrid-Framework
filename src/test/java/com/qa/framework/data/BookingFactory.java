package com.qa.framework.data;

import com.qa.framework.api.models.Booking;
import com.qa.framework.api.models.BookingDates;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/** Generates unique, valid booking payloads so tests never depend on pre-existing data. */
public final class BookingFactory {

    private static final String[] FIRST = {"Asha", "Ravi", "Meera", "Karan", "Neha", "Arjun"};
    private static final String[] LAST = {"Sharma", "Verma", "Singh", "Gupta", "Mehta", "Rao"};

    private BookingFactory() {
    }

    public static Booking random() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        LocalDate checkin = LocalDate.now().plusDays(1 + r.nextInt(30));
        LocalDate checkout = checkin.plusDays(1 + r.nextInt(10));
        return new Booking(
                FIRST[r.nextInt(FIRST.length)],
                LAST[r.nextInt(LAST.length)],
                100 + r.nextInt(900),
                r.nextBoolean(),
                new BookingDates(checkin.toString(), checkout.toString()),
                "Breakfast");
    }
}
