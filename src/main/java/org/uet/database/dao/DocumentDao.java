package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Document;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DocumentDao {
    public ArrayList<Document> getAllDocument() {
        ArrayList<Document> documents = new ArrayList<>();
        String query = "SELECT * FROM document";
        Document document = null;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                document = new Document();
                document.setCode(resultSet.getString("document_code"));
                document.setTitle(resultSet.getString("document_title"));
                document.setCategory(resultSet.getString("document_category"));
                document.setAuthor(resultSet.getString("document_author"));
                document.setPublisher(resultSet.getString("document_publisher"));
                document.setPrice(resultSet.getDouble("document_price"));
                document.setQuantity(resultSet.getInt("document_quantity"));

                documents.add(document);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }

    public void addDocument(Document document) throws SQLException {
        String query = "INSERT INTO document (document_code, document_title, document_category, document_author, " +
                "document_publisher, document_price, document_quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, document.getCode());
            ps.setString(2, document.getTitle());
            ps.setString(3, document.getCategory());
            ps.setString(4, document.getAuthor());
            ps.setString(5, document.getPublisher());
            ps.setDouble(6, document.getPrice());
            ps.setInt(7, document.getQuantity());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        }
    }

    public void updateDocument(Document document) throws SQLException {
        String query = "UPDATE document SET document_title = ?, document_category = ?, " +
                "document_author = ?, document_publisher = ?, document_price = ?, document_quantity = ? " +
                "WHERE document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, document.getTitle());
            ps.setString(2, document.getCategory());
            ps.setString(3, document.getAuthor());
            ps.setString(4, document.getPublisher());
            ps.setDouble(5, document.getPrice());
            ps.setInt(6, document.getQuantity());
            ps.setString(7, document.getCode());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        }
    }

    public void deleteBorrowRecord(String documentCode) {
        String query = "DELETE FROM document WHERE document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, documentCode);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
