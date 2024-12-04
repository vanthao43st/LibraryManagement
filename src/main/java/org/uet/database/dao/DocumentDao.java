package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Document;

import java.sql.*;
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
                document.setDescription(resultSet.getString("document_description"));
                document.setCategory(resultSet.getString("document_category"));
                document.setAuthor(resultSet.getString("document_author"));
                document.setPrice(resultSet.getDouble("document_price"));
                document.setQuantity(resultSet.getInt("document_quantity"));

                documents.add(document);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return documents;
    }

    public void addDocument(Document document) throws SQLException {
        String query = "INSERT INTO document (document_code, document_title, document_description, " +
                "document_category, document_author, document_price, document_quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, document.getCode());
            ps.setString(2, document.getTitle());
            ps.setString(3, document.getDescription());
            ps.setString(4, document.getCategory());
            ps.setString(5, document.getAuthor());
            ps.setDouble(6, document.getPrice());
            ps.setInt(7, document.getQuantity());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        }
    }

    public Document getDocumentByCode(String documentCode) {
        Document document = null;
        String query = "SELECT * FROM document WHERE document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, documentCode);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    document = new Document();
                    document.setCode(resultSet.getString("document_code"));
                    document.setTitle(resultSet.getString("document_title"));
                    document.setDescription(resultSet.getString("document_description"));
                    document.setCategory(resultSet.getString("document_category"));
                    document.setAuthor(resultSet.getString("document_author"));
                    document.setPrice(resultSet.getDouble("document_price"));
                    document.setQuantity(resultSet.getInt("document_quantity"));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return document;
    }

    public void updateDocument(Document document) throws SQLException {
        String query = "UPDATE document SET document_title = ?, document_description = ?, " +
                "document_category = ?, " +
                "document_author = ?, document_price = ?, document_quantity = ? " +
                "WHERE document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, document.getTitle());
            ps.setString(2, document.getDescription());
            ps.setString(3, document.getCategory());
            ps.setString(4, document.getAuthor());
            ps.setDouble(5, document.getPrice());
            ps.setInt(6, document.getQuantity());
            ps.setString(7, document.getCode());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        }
    }

    public void deleteDocument(String documentCode) {
        String query = "DELETE FROM document WHERE document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, documentCode);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Document> searchDocuments(String title, String author, String category) {
        ArrayList<Document> result = new ArrayList<>();
        String query = "SELECT * FROM document WHERE 1=1";

        if (title != null && !title.isEmpty()) {
            query += " AND document_title LIKE ?";
        }
        if (author != null && !author.isEmpty()) {
            query += " AND document_author LIKE ?";
        }
        if (category != null && !category.isEmpty()) {
            query += " AND document_category LIKE ?";
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            int paramIndex = 1;

            // Gán giá trị tham số
            if (title != null && !title.isEmpty()) {
                ps.setString(paramIndex++, "%" + title + "%");
            }
            if (author != null && !author.isEmpty()) {
                ps.setString(paramIndex++, "%" + author + "%");
            }
            if (category != null && !category.isEmpty()) {
                ps.setString(paramIndex++, "%" + category + "%");
            }

            // Thực thi truy vấn
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Document document = new Document();
                document.setCode(resultSet.getString("document_code"));
                document.setTitle(resultSet.getString("document_title"));
                document.setDescription(resultSet.getString("document_description"));
                document.setCategory(resultSet.getString("document_category"));
                document.setAuthor(resultSet.getString("document_author"));
                document.setPrice(resultSet.getDouble("document_price"));
                document.setQuantity(resultSet.getInt("document_quantity"));
                result.add(document);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }
}
