package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Student;
import org.uet.enums.Gender;
import java.sql.*;
import java.util.ArrayList;

public class StudentDao {

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student";
        Student student;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                student = new Student();
                student.setStudentId(resultSet.getString("student_id"));
                student.setName(resultSet.getString("student_name"));
                student.setGender(resultSet.getString("gender_name"));
                student.setStudentClass(resultSet.getString("student_class"));
                student.setMajor(resultSet.getString("student_major"));
                student.setPhone(resultSet.getString("student_phone"));
                student.setEmail(resultSet.getString("student_email"));

                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public void addStudent(Student student) throws SQLException {
        String query = "INSERT INTO student (student_id, student_name, student_gender, student_birthdate, " +
                "student_class, student_address, student_major, student_phone, student_email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, student.getStudentId());
            ps.setString(2, student.getName());
            ps.setString(2, student.getGender());
            ps.setString(4, student.getStudentClass());
            ps.setString(5, student.getMajor());
            ps.setString(6, student.getPhone());
            ps.setString(7, student.getEmail());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(Student student) throws SQLException {
        String query = "UPDATE student SET student_name = ?, student_gender = ?, " +
                "student_birthdate = ?, student_class = ?, student_address = ?, student_major = ?, " +
                "student_phone = ?, student_email = ? " +
                "WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getGender());
            ps.setString(3, student.getStudentClass());
            ps.setString(4, student.getMajor());
            ps.setString(5, student.getPhone());
            ps.setString(6, student.getEmail());
            ps.setString(7, student.getStudentId());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        }
    }

    public void deleteStudent(String id) {
        String query = "DELETE FROM student WHERE student_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, id);

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
