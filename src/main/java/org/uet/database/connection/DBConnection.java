package org.uet.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_management";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    private static DBConnection instance;

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
