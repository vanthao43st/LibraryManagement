package org.uet.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    protected static final String URL = "jdbc:mysql://localhost:3306/library_management";
    protected static final String USER = "root";
    protected static final String PASSWORD = "123456";

    protected static DBConnection instance;

    protected DBConnection() {
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
