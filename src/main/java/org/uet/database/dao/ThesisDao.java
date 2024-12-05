package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Thesis;

import java.sql.*;
import java.util.ArrayList;

public class ThesisDao {
    public ArrayList<Thesis> getAllThesis() {
        ArrayList<Thesis> theses = new ArrayList<>();
        String query = "SELECT * FROM thesis";
        Thesis thesis;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                thesis = new Thesis();
                thesis.setCode(resultSet.getString("thesis_code"));
                thesis.setTitle(resultSet.getString("thesis_title"));
                thesis.setDescription(resultSet.getString("thesis_description"));
                thesis.setAuthor(resultSet.getString("thesis_author"));
                thesis.setSupervisor(resultSet.getString("thesis_supervisor"));
                thesis.setUniversity(resultSet.getString("thesis_university"));
                thesis.setDegree(resultSet.getString("thesis_degree"));
                thesis.setSubmissionYear(resultSet.getInt("thesis_submission_year"));
                thesis.setMajor(resultSet.getString("thesis_major"));

                theses.add(thesis);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return theses;
    }

    public void addThesis(Thesis thesis) throws SQLException {
        String query = "INSERT INTO thesis (thesis_code, thesis_title, thesis_description, " +
                "thesis_author, thesis_supervisor, thesis_university, thesis_degree, thesis_submission_year, thesis_major) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        }
    }

    public Thesis getThesisByCode(String thesisCode) {
        Thesis thesis = null;
        String query = "SELECT * FROM thesis WHERE thesis_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, thesisCode);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    thesis = new Thesis();
                    thesis.setCode(resultSet.getString("thesis_code"));
                    thesis.setTitle(resultSet.getString("thesis_title"));
                    thesis.setDescription(resultSet.getString("thesis_description"));
                    thesis.setAuthor(resultSet.getString("thesis_author"));
                    thesis.setSupervisor(resultSet.getString("thesis_supervisor"));
                    thesis.setUniversity(resultSet.getString("thesis_university"));
                    thesis.setDegree(resultSet.getString("thesis_degree"));
                    thesis.setSubmissionYear(resultSet.getInt("thesis_submission_year"));
                    thesis.setMajor(resultSet.getString("thesis_major"));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return thesis;
    }

    public void updateThesis(Thesis thesis) throws SQLException {
        String query = "UPDATE thesis SET thesis_title = ?, thesis_description = ?, thesis_author = ?, " +
                "thesis_supervisor = ?, thesis_university = ?, thesis_degree = ?, thesis_submission_year = ?, thesis_major = ? " +
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
            ps.setString(9, thesis.getCode());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        }
    }

    public void deleteThesis(String thesisCode) {
        String query = "DELETE FROM thesis WHERE thesis_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, thesisCode);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Thesis> searchTheses(String title, String author, String major) {
        ArrayList<Thesis> result = new ArrayList<>();
        String query = "SELECT * FROM thesis WHERE 1=1";

        if (title != null && !title.isEmpty()) {
            query += " AND thesis_title LIKE ?";
        }
        if (author != null && !author.isEmpty()) {
            query += " AND thesis_author LIKE ?";
        }
        if (major != null && !major.isEmpty()) {
            query += " AND thesis_major LIKE ?";
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            int paramIndex = 1;

            if (title != null && !title.isEmpty()) {
                ps.setString(paramIndex++, "%" + title + "%");
            }
            if (author != null && !author.isEmpty()) {
                ps.setString(paramIndex++, "%" + author + "%");
            }
            if (major != null && !major.isEmpty()) {
                ps.setString(paramIndex++, "%" + major + "%");
            }

            ResultSet resultSet = ps.executeQuery();

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
                result.add(thesis);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }
}
