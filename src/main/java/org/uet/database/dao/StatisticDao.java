package org.uet.database.dao;

import org.uet.database.connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StatisticDao {
    public int getTotalBooks() {
        String query = "SELECT SUM(book_quantity) AS total_books FROM book";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_books");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getBorrowedBooks() {
        String query = "SELECT SUM(library_quantity) AS borrowed_books FROM library WHERE library_status = 'Chưa trả'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("borrowed_books");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAvailableBooks() {
        int totalBooks = getTotalBooks();
        int borrowedBooks = getBorrowedBooks();
        return totalBooks - borrowedBooks;
    }
}
