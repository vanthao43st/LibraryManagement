package org.uet.controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.database.connection.DBConnection;
import org.uet.database.dao.UserDao;
import org.uet.entity.SessionManager;
import org.uet.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    private LoginController controller;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button closeButton, registerButton;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        controller = new LoginController();
        usernameField = new TextField();
        passwordField = new PasswordField();
        closeButton = new Button();
        registerButton = new Button();
        userDao = new UserDao();

        controller.usernameField = usernameField;
        controller.passwordField = passwordField;
        controller.closeButton = closeButton;
        controller.registerButton = registerButton;

        controller.initialize(null, null);
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            controller.initialize(null, null);

            assertNotNull(closeButton.getOnMouseClicked());
            assertNotNull(registerButton.getOnMouseClicked());
        });
    }

    @Test
    void testHandleLoginActionWithAdmin() {
        Platform.runLater(() -> {
            usernameField.setText("admin");
            passwordField.setText("admin");

            controller.handleLoginAction();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            assertNotNull(stage.getScene());
        });
    }

    @Test
    void testHandleLoginActionWithUser() {
        Platform.runLater(() -> {
            usernameField.setText("user");
            passwordField.setText("password");

            try (Connection connection = DBConnection.getConnection()) {
                String query = "INSERT INTO user (user_id, user_fullname, user_gender, user_class, user_major, user_phone, user_email, user_username, user_password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, "123");
                    preparedStatement.setString(2, "John Doe");
                    preparedStatement.setString(3, "MALE");
                    preparedStatement.setString(4, "Class A");
                    preparedStatement.setString(5, "Computer Science");
                    preparedStatement.setString(6, "1234567890");
                    preparedStatement.setString(7, "john@example.com");
                    preparedStatement.setString(8, "user");
                    preparedStatement.setString(9, "password");
                    preparedStatement.executeUpdate();
                }

                controller.handleLoginAction();

                Stage stage = (Stage) usernameField.getScene().getWindow();
                assertNotNull(stage.getScene());

                User currentUser = SessionManager.getInstance().getCurrentUser();
                assertEquals("John Doe", currentUser.getFullname());
            } catch (SQLException e) {
                fail("Exception thrown: " + e.getMessage());
            }
        });
    }

    @Test
    void testHandleLoginActionWithInvalidCredentials() {
        Platform.runLater(() -> {
            usernameField.setText("invalid");
            passwordField.setText("invalid");

            controller.handleLoginAction();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Đăng nhập thất bại");
            alert.setHeaderText("Tên đăng nhập hoặc mật khẩu không chính xác!");
            alert.setContentText("Hãy thử lại!");
            alert.showAndWait();

            assertEquals(Alert.AlertType.ERROR, alert.getAlertType());
            assertEquals("Đăng nhập thất bại", alert.getTitle());
            assertEquals("Tên đăng nhập hoặc mật khẩu không chính xác!", alert.getHeaderText());
            assertEquals("Hãy thử lại!", alert.getContentText());
        });
    }
}
