package org.uet.database.connection;

import org.junit.jupiter.api.Test;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    void testGetConnection() {
        try {
            Connection connection = DBConnection.getConnection();
            assertNotNull(connection);
            assertFalse(connection.isClosed());

            connection.close();
            assertTrue(connection.isClosed());
        } catch (SQLException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testSingletonInstance() {
        try {
            Connection connection1 = DBConnection.getConnection();
            Connection connection2 = DBConnection.getConnection();

            assertSame(connection1, connection2);

            connection1.close();
            connection2.close();
        } catch (SQLException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testInvalidConnection() {
        DBConnection invalidConnection = new DBConnection();
        try {
            String invalidUrl = "jdbc:mysql://invalid_url:3306/invalid_database";
            Connection connection = DriverManager.getConnection(invalidUrl, "invalid_user", "invalid_password");
            fail("SQLException should be thrown");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Communications link failure"));
        }
    }
}
