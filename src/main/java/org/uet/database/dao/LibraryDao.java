package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LibraryDao {
    public ArrayList<Library> getAllRecords() {
        ArrayList<Library> items = new ArrayList<>();
        String query = "SELECT * FROM library";
        Library item;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                item = new Library();
                item.setUserId(resultSet.getString("library_user_id"));
                item.setDocumentCode(resultSet.getString("library_document_code"));
                item.setQuantity(resultSet.getInt("library_quantity"));
                item.setBorrowDate(resultSet.getString("library_borrow_date"));
                item.setReturnDate(resultSet.getString("library_return_date"));
                item.setDueDate(resultSet.getString("library_due_date"));
                item.setStatus(resultSet.getString("library_status"));

                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public void addBorrowRecord(Library library) {
        String query = "INSERT INTO library (library_user_id, library_document_code, library_quantity, " +
                "library_borrow_date, library_return_date, library_due_date, library_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, library.getUserId());
            ps.setString(2, library.getDocumentCode());
            ps.setInt(3, library.getQuantity());
            ps.setString(4, library.getBorrowDate());
            ps.setString(5, library.getReturnDate());
            ps.setString(6, library.getDueDate());
            ps.setString(7, library.getStatus());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBorrowRecord(Library library) {
        String query = "UPDATE library SET library_quantity = ?, library_borrow_date = ?, library_return_date = ?, " +
                "library_due_date = ?, library_status = ? WHERE library_user_id = ? AND library_document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, library.getQuantity());
            ps.setString(2, library.getBorrowDate());
            ps.setString(3, library.getReturnDate());
            ps.setString(4, library.getDueDate());
            ps.setString(5, library.getStatus());
            ps.setString(6, library.getUserId());
            ps.setString(7, library.getDocumentCode());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBorrowRecord(String userId, String documentCode) {
        String query = "DELETE FROM library WHERE library_user_id = ? AND library_document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, userId);
            ps.setString(2, documentCode);

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
