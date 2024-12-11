package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Book;
import org.uet.entity.Document;

import java.sql.*;
import java.util.ArrayList;

public class BookDao {
    public ArrayList<Book> getAllBook() {
        ArrayList<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book";
        Book book;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                book = new Book();
                book.setCode(resultSet.getString("book_code"));
                book.setTitle(resultSet.getString("book_title"));
                book.setDescription(resultSet.getString("book_description"));
                book.setCategory(resultSet.getString("book_category"));
                book.setAuthor(resultSet.getString("book_author"));
                book.setPrice(resultSet.getDouble("book_price"));
                book.setQuantity(resultSet.getInt("book_quantity"));

                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return books;
    }

    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO book (book_code, book_title, book_description, " +
                "book_category, book_author, book_price, book_quantity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, book.getCode());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getDescription());
            ps.setString(4, book.getCategory());
            ps.setString(5, book.getAuthor());
            ps.setDouble(6, book.getPrice());
            ps.setInt(7, book.getQuantity());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        }
    }

    public Document getBookByCode(String bookCode) {
        Book book = null;
        String query = "SELECT * FROM book WHERE document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, bookCode);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    book = new Book();
                    book.setCode(resultSet.getString("document_code"));
                    book.setTitle(resultSet.getString("document_title"));
                    book.setDescription(resultSet.getString("document_description"));
                    book.setCategory(resultSet.getString("document_category"));
                    book.setAuthor(resultSet.getString("document_author"));
                    book.setPrice(resultSet.getDouble("document_price"));
                    book.setQuantity(resultSet.getInt("document_quantity"));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return book;
    }

    public void updateBook(Book book) throws SQLException {
        String query = "UPDATE book SET book_title = ?, book_description = ?, " +
                "book_category = ?, " +
                "book_author = ?, book_price = ?, book_quantity = ? " +
                "WHERE book_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getDescription());
            ps.setString(3, book.getCategory());
            ps.setString(4, book.getAuthor());
            ps.setDouble(5, book.getPrice());
            ps.setInt(6, book.getQuantity());
            ps.setString(7, book.getCode());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        }
    }

    public void deleteBook(String bookCode) {
        String query = "DELETE FROM book WHERE book_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, bookCode);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isBorrowedBook(String documentCode) {
        String query = "SELECT 1 FROM library WHERE library_document_code = ? AND library_quantity > 0 AND library_status = 'Chưa trả' LIMIT 1";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, documentCode);
            ResultSet resultSet = ps.executeQuery();

            return resultSet.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
