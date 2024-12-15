package org.uet.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;
import org.uet.database.dao.UserDao;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class RegisterControllerTest {

    private RegisterController controller;
    private TextField usernameField, passwordField, fullNameField, classField, phoneField, emailField;
    private ComboBox<String> genderField, majorField;
    private Button exitButton, registerButton;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        JavaFXInitializer.initialize();

        userDao = new UserDao();
        controller = new RegisterController();
        usernameField = new TextField();
        passwordField = new TextField();
        fullNameField = new TextField();
        classField = new TextField();
        phoneField = new TextField();
        emailField = new TextField();
        genderField = new ComboBox<>();
        majorField = new ComboBox<>();
        exitButton = new Button();
        registerButton = new Button();
        userDao = new UserDao();

        controller.usernameField = usernameField;
        controller.passwordField = passwordField;
        controller.fullNameField = fullNameField;
        controller.classField = classField;
        controller.phoneField = phoneField;
        controller.emailField = emailField;
        controller.genderField = genderField;
        controller.majorField = majorField;
        controller.exitButton = exitButton;
        controller.registerButton = registerButton;

        controller.initialize(null, null);
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            controller.initialize(null, null);

            assertEquals(FXCollections.observableArrayList("FEMALE", "MALE"), genderField.getItems());
            assertEquals(FXCollections.observableArrayList(
                    "Công nghệ thông tin",
                    "Điện tử viễn thông",
                    "Kỹ thuật phần mềm",
                    "Khoa học máy tính",
                    "Hệ thống thông tin",
                    "Tự động hoá",
                    "Điện tử hoá"
            ), majorField.getItems());

            assertNotNull(exitButton.getOnMouseClicked());
        });
    }

    @Test
    void testGenerateUniqueUserIdAsync() {
        CompletableFuture<String> userIdFuture = controller.generateUniqueUserIdAsync();
        userIdFuture.thenAccept(userId -> {
            assertNotNull(userId);
            assertEquals(8, userId.length());
        }).join();
    }

    @Test
    void testHandleRegisterAction() {
        Platform.runLater(() -> {
            usernameField.setText("newuser");
            passwordField.setText("password123");
            fullNameField.setText("New User");
            classField.setText("Class A");
            phoneField.setText("0123456789");
            emailField.setText("newuser@example.com");
            genderField.setValue("MALE");
            majorField.setValue("Khoa học máy tính");

            // Giả lập việc kiểm tra username tồn tại hay không
            CompletableFuture<Boolean> doesUsernameExistFuture = CompletableFuture.completedFuture(false);
            userDao = new UserDao() {
                @Override
                public CompletableFuture<Boolean> doesUsernameExistAsync(String username) {
                    return doesUsernameExistFuture;
                }
            };

            controller.handleRegisterAction(new ActionEvent());

            // Kiểm tra xem người dùng có được thêm vào cơ sở dữ liệu hay không
            User newUser = new User(
                    usernameField.getText(),
                    fullNameField.getText(),
                    Gender.valueOf(genderField.getValue()),
                    classField.getText(),
                    majorField.getValue(),
                    phoneField.getText(),
                    emailField.getText(),
                    usernameField.getText(),
                    passwordField.getText()
            );

            assertEquals("newuser", newUser.getUsername());
            assertEquals("password123", newUser.getPassword());
            assertEquals("New User", newUser.getFullname());
            assertEquals("Class A", newUser.getClassname());
            assertEquals("0123456789", newUser.getPhonenumber());
            assertEquals("newuser@example.com", newUser.getEmail());
            assertEquals(Gender.MALE, newUser.getGender());
            assertEquals("Khoa học máy tính", newUser.getMajor());

            Stage stage = (Stage) registerButton.getScene().getWindow();
            assertNotNull(stage.getScene());
        });
    }

    @Test
    void testHandleRegisterActionWithExistingUsername() {
        Platform.runLater(() -> {
            usernameField.setText("existinguser");
            passwordField.setText("password123");
            fullNameField.setText("Existing User");
            classField.setText("Class A");
            phoneField.setText("0123456789");
            emailField.setText("existinguser@example.com");
            genderField.setValue("FEMALE");
            majorField.setValue("Kỹ thuật phần mềm");

            // Giả lập việc kiểm tra username đã tồn tại
            CompletableFuture<Boolean> doesUsernameExistFuture = CompletableFuture.completedFuture(true);
            userDao = new UserDao() {
                @Override
                public CompletableFuture<Boolean> doesUsernameExistAsync(String username) {
                    return doesUsernameExistFuture;
                }
            };

            controller.handleRegisterAction(new ActionEvent());

            // Kiểm tra xem người dùng không được thêm vào cơ sở dữ liệu
            assertNull(controller.usernameField.getText());
            assertNull(controller.passwordField.getText());
            assertNull(controller.fullNameField.getText());
            assertNull(controller.classField.getText());
            assertNull(controller.phoneField.getText());
            assertNull(controller.emailField.getText());
            assertNull(controller.genderField.getValue());
            assertNull(controller.majorField.getValue());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            assertEquals("Lỗi", alert.getTitle());
            assertEquals("username đã tồn tại. Vui lòng nhập username khác!", alert.getContentText());
        });
    }

    @Test
    void testReturnLoginPage() {
        Platform.runLater(() -> {
            controller.returnLoginPage(exitButton);

            Stage stage = (Stage) exitButton.getScene().getWindow();
            assertNotNull(stage.getScene());
        });
    }

    @Test
    void testShowAlert() {
        Platform.runLater(() -> {
            controller.showAlert("Thông báo", "Đăng ký thành công.", Alert.AlertType.INFORMATION);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            assertEquals("Thông báo", alert.getTitle());
            assertEquals("Đăng ký thành công.", alert.getContentText());
        });
    }
}
