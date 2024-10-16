package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.User;
import org.uet.enums.Gender;
import java.sql.*;
import java.util.ArrayList;

public class UserDao {

    public ArrayList<User> getAllUsers() {
        ArrayList<User> items = new ArrayList<>();
        String query = "SELECT * FROM user";
        User item;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                item = new User();
                item.setId(resultSet.getString("user_id"));
                item.setName(resultSet.getString("user_name"));
                item.setGender(Gender.valueOf(resultSet.getString("user_gender")));
                item.setBirthDate(resultSet.getString("user_birthdate"));
                item.setClassName(resultSet.getString("user_class"));
                item.setAddress(resultSet.getString("user_address"));
                item.setMajor(resultSet.getString("user_major"));
                item.setPhoneNumber(resultSet.getString("user_phone"));
                item.setEmail(resultSet.getString("user_email"));

                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO user (user_id, user_name, user_gender, user_birthdate," +
                "user_class, user_address, user_major, user_phone, user_email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getGender().toString());
            ps.setDate(4, Date.valueOf(user.getBirthDate()));
            ps.setString(5, user.getClassName());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getMajor());
            ps.setString(8, user.getPhoneNumber());
            ps.setString(9, user.getEmail());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE user SET user_name = ?, user_gender = ?, " +
                "user_birthdate = ?, user_class = ?, user_address = ?, user_major = ? " +
                "user_phone = ?, user_email = ? " +
                "WHERE document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getGender().toString());
            ps.setString(3, user.getBirthDate());
            ps.setString(4, user.getClassName());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getMajor());
            ps.setString(7, user.getPhoneNumber());
            ps.setString(8, user.getEmail());
            ps.setString(9, user.getId());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        }
    }

    public void deleteUser(String id) {
        String query = "DELETE FROM user WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);) {

            ps.setString(1, id);

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
