package org.uet.database.dao;

import org.uet.database.connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class StatisticDao {

    public CompletableFuture<Integer> getTotalDocumentsAsync() {
        return CompletableFuture.supplyAsync(() -> {
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
        });
    }

    public CompletableFuture<Integer> getBorrowedDocumentsAsync() {
        return CompletableFuture.supplyAsync(() -> {
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
        });
    }

    public CompletableFuture<Integer> getAvailableDocumentsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int totalDocuments = getTotalDocumentsAsync().join();
                int borrowedDocuments = getBorrowedDocumentsAsync().join();
                return totalDocuments - borrowedDocuments;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }
}
