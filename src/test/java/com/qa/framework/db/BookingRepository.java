package com.qa.framework.db;

import com.qa.framework.api.models.Booking;
import com.qa.framework.config.ConfigReader;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * JDBC helper for SQL-based data validation.
 * Defaults to in-memory H2. Change db.url/db.user/db.password (and add the JDBC driver
 * to the pom) to validate a real MySQL or Oracle schema instead.
 */
public final class BookingRepository {

    private BookingRepository() {
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                ConfigReader.get("db.url"), ConfigReader.get("db.user"), ConfigReader.get("db.password"));
    }

    public static void reset() {
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute("DROP TABLE IF EXISTS bookings");
            s.execute("CREATE TABLE bookings ("
                    + "id INT PRIMARY KEY, "
                    + "firstname VARCHAR(100), "
                    + "lastname VARCHAR(100), "
                    + "total_price INT, "
                    + "deposit_paid BOOLEAN, "
                    + "checkin DATE, "
                    + "checkout DATE)");
        } catch (SQLException e) {
            throw new IllegalStateException("Could not reset bookings table", e);
        }
    }

    public static void save(int id, Booking b) {
        String sql = "INSERT INTO bookings (id, firstname, lastname, total_price, deposit_paid, checkin, checkout) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, b.firstname());
            ps.setString(3, b.lastname());
            ps.setInt(4, b.totalprice());
            ps.setBoolean(5, b.depositpaid());
            ps.setDate(6, Date.valueOf(LocalDate.parse(b.bookingdates().checkin())));
            ps.setDate(7, Date.valueOf(LocalDate.parse(b.bookingdates().checkout())));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save booking " + id, e);
        }
    }

    public static long queryForLong(String sql, Object... params) {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0L;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Query failed: " + sql, e);
        }
    }

    public static String queryForString(String sql, Object... params) {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Query failed: " + sql, e);
        }
    }

    private static void bind(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}
