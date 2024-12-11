package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class UserDao {

    public CompletableFuture<ArrayList<User>> getAllUsersAsync() {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<User> items = new ArrayList<>();
            String query = "SELECT * FROM user";
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    User item = new User();
                    item.setId(resultSet.getString("user_id"));
                    item.setFullname(resultSet.getString("user_fullname"));
                    item.setGender(Gender.valueOf(resultSet.getString("user_gender")));
                    item.setClassname(resultSet.getString("user_class"));
                    item.setMajor(resultSet.getString("user_major"));
                    item.setPhonenumber(resultSet.getString("user_phone"));
                    item.setEmail(resultSet.getString("user_email"));
                    item.setUsername(resultSet.getString("user_username"));
                    item.setPassword(resultSet.getString("user_password"));

                    items.add(item);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return items;
        });
    }

    public CompletableFuture<Void> addUserAsync(User user) {
        return CompletableFuture.runAsync(() -> {
            String query = "INSERT INTO user (user_id, user_fullname, user_gender, " +
                    "user_class, user_major, user_phone, user_email, user_username, user_password ) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, user.getId());
                ps.setString(2, user.getFullname());
                ps.setString(3, user.getGender().toString());
                ps.setString(4, user.getClassname());
                ps.setString(5, user.getMajor());
                ps.setString(6, user.getPhonenumber());
                ps.setString(7, user.getEmail());
                ps.setString(8, user.getUsername());
                ps.setString(9, user.getPassword());

                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public CompletableFuture<Void> updateUserAsync(User user) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE user SET user_fullname = ?, user_gender = ?, " +
                    "user_class = ?, user_major = ?, " +
                    "user_phone = ?, user_email = ?, user_username = ?, user_password = ? " +
                    "WHERE user_id = ?";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, user.getFullname());
                ps.setString(2, user.getGender().toString());
                ps.setString(3, user.getClassname());
                ps.setString(4, user.getMajor());
                ps.setString(5, user.getPhonenumber());
                ps.setString(6, user.getEmail());
                ps.setString(7, user.getUsername());
                ps.setString(8, user.getPassword());
                ps.setString(9, user.getId());

                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " row(s) updated.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public CompletableFuture<Void> deleteUserAsync(String id) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM user WHERE user_id = ?";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, id);

                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " row(s) deleted.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public CompletableFuture<Boolean> hasUnreturnedBooksAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT 1 FROM library WHERE library_user_id = ? AND library_quantity > 0 AND library_status = 'Chưa trả' LIMIT 1";
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, userId);
                ResultSet resultSet = ps.executeQuery();

                return resultSet.next();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public CompletableFuture<Boolean> doesUserIdExistAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM user WHERE user_id = ?";
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, userId);
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Nếu số lượng > 0, ID đã tồn tại
                }
            } catch (Exception e) {
                System.out.println("Lỗi truy vấn database: " + e.getMessage());
            }
            return false;
        });
    }

    public CompletableFuture<Boolean> doesUsernameExistAsync(String username) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM user WHERE user_username = ?";
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, username);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Nếu số lượng > 0, username đã tồn tại
                }
            } catch (Exception e) {
                System.out.println("Lỗi truy vấn database: " + e.getMessage());
            }
            return false;
        });
    }
}
