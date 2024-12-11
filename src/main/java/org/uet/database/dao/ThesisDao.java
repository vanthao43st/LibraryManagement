package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Thesis;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ThesisDao {
    public CompletableFuture<ArrayList<Thesis>> getAllThesisAsync() {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<Thesis> theses = new ArrayList<>();
            String query = "SELECT * FROM thesis";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Thesis thesis = new Thesis();
                    thesis.setCode(resultSet.getString("thesis_code"));
                    thesis.setTitle(resultSet.getString("thesis_title"));
                    thesis.setDescription(resultSet.getString("thesis_description"));
                    thesis.setAuthor(resultSet.getString("thesis_author"));
                    thesis.setSupervisor(resultSet.getString("thesis_supervisor"));
                    thesis.setUniversity(resultSet.getString("thesis_university"));
                    thesis.setDegree(resultSet.getString("thesis_degree"));
                    thesis.setSubmissionYear(resultSet.getInt("thesis_submission_year"));
                    thesis.setMajor(resultSet.getString("thesis_major"));
                    thesis.setQuantity(resultSet.getInt("thesis_quantity"));

                    theses.add(thesis);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return theses;
        });
    }

    public CompletableFuture<Void> addThesisAsync(Thesis thesis) {
        return CompletableFuture.runAsync(() -> {
            String query = "INSERT INTO thesis (thesis_code, thesis_title, thesis_description, " +
                    "thesis_author, thesis_supervisor, thesis_university, " +
                    "thesis_degree, thesis_submission_year, thesis_major, thesis_quantity) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, thesis.getCode());
                ps.setString(2, thesis.getTitle());
                ps.setString(3, thesis.getDescription());
                ps.setString(4, thesis.getAuthor());
                ps.setString(5, thesis.getSupervisor());
                ps.setString(6, thesis.getUniversity());
                ps.setString(7, thesis.getDegree());
                ps.setInt(8, thesis.getSubmissionYear());
                ps.setString(9, thesis.getMajor());
                ps.setInt(10, thesis.getQuantity());

                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public CompletableFuture<Void> updateThesisAsync(Thesis thesis) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE thesis SET thesis_title = ?, thesis_description = ?, thesis_author = ?, " +
                    "thesis_supervisor = ?, thesis_university = ?, thesis_degree = ?, thesis_submission_year = ?, " +
                    "thesis_major = ?, thesis_quantity = ? " +
                    "WHERE thesis_code = ?";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, thesis.getTitle());
                ps.setString(2, thesis.getDescription());
                ps.setString(3, thesis.getAuthor());
                ps.setString(4, thesis.getSupervisor());
                ps.setString(5, thesis.getUniversity());
                ps.setString(6, thesis.getDegree());
                ps.setInt(7, thesis.getSubmissionYear());
                ps.setString(8, thesis.getMajor());
                ps.setInt(9, thesis.getQuantity());
                ps.setString(10, thesis.getCode());

                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " row(s) updated.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public CompletableFuture<Void> deleteThesisAsync(String thesisCode) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM thesis WHERE thesis_code = ?";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, thesisCode);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) deleted.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
    }


    // Kiểm tra xem luận văn có đang được mượn không (bất đồng bộ)
    public CompletableFuture<Boolean> isBorrowedThesisAsync(String documentCode) {
        return CompletableFuture.supplyAsync(() -> {
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
        });
    }
}
