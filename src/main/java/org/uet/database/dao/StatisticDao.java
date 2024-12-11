package org.uet.database.dao;

import org.uet.database.connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StatisticDao {
    public int getTotalDocuments() {
        String query = "SELECT (" +
                "    (SELECT SUM(book_quantity) FROM book) + " +
                "    (SELECT SUM(thesis_quantity) FROM thesis) " +
                ") AS total_items";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total_items");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getBorrowedDocuments() {
        String query = "SELECT SUM(library_quantity) AS borrowed_documents FROM library";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("borrowed_documents");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAvailableDocuments() {
        int totalDocuments = getTotalDocuments();
        int borrowedDocuments = getBorrowedDocuments();
        return totalDocuments - borrowedDocuments;
    }
}
