package org.uet.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_management";
    private static final String USER = "root"; // Thay bằng username của bạn
    private static final String PASSWORD = "123456"; // Thay bằng password của bạn

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
