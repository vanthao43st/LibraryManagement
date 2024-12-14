package org.uet.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.uet.database.dao.UserDao;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class RegisterController implements Initializable {

    @FXML
    protected TextField usernameField, passwordField, fullNameField, classField, phoneField, emailField;

    @FXML
    protected ComboBox<String> genderField, majorField;

    @FXML
    protected Button exitButton, registerButton;

    protected final UserDao userDao = new UserDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderField.setItems(FXCollections.observableArrayList("FEMALE", "MALE"));
        majorField.setItems(FXCollections.observableArrayList(
                "Công nghệ thông tin",
                "Điện tử viễn thông",
                "Kỹ thuật phần mềm",
                "Khoa học máy tính",
                "Hệ thống thông tin",
                "Tự động hoá",
                "Điện tử hoá"
        ));

        exitButton.setOnMouseClicked(e -> {
            returnLoginPage(exitButton);
        });
    }

    public CompletableFuture<String> generateUniqueUserIdAsync() {
        return CompletableFuture.supplyAsync(() -> {
            Random random = new Random();
            return String.format("%08d", random.nextInt(100000000));
        }).thenCompose(userId ->
                userDao.doesUserIdExistAsync(userId).thenCompose(exists -> {
                    if (exists) {
                        return generateUniqueUserIdAsync(); // Gọi lại nếu ID đã tồn tại
                    }
                    return CompletableFuture.completedFuture(userId); // Trả về ID nếu duy nhất
                })
        );
    }

    protected void returnLoginPage(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) button.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene); // Đổi scene sang màn hình Register

            // Hiển thị lại cửa sổ mới
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Hiển thị thông báo Alert
    protected void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleRegisterAction(ActionEvent event) {
        if (inCompleteInfo()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
            return;
        }

        userDao.doesUsernameExistAsync(usernameField.getText()).thenCompose(usernameExists -> {
            if (usernameExists) {
                Platform.runLater(() -> showAlert("Lỗi", "username đã tồn tại. Vui lòng nhập username khác!", Alert.AlertType.ERROR));
                return CompletableFuture.completedFuture(null);
            }

            return generateUniqueUserIdAsync().thenCompose(userId -> {
                User newUser = new User(
                        userId,
                        fullNameField.getText(),
                        Gender.valueOf(genderField.getValue()),
                        classField.getText(),
                        majorField.getValue(),
                        phoneField.getText(),
                        emailField.getText(),
                        usernameField.getText(),
                        passwordField.getText()
                );

                return userDao.addUserAsync(newUser).thenRun(() -> {
                    Platform.runLater(() -> {
                        showAlert("Thông báo", "Đăng ký thành công.", Alert.AlertType.INFORMATION);
                        returnLoginPage(registerButton);
                    });
                });
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> showAlert("Lỗi", "Đã xảy ra lỗi: " + e.getMessage(), Alert.AlertType.ERROR));
            return null;
        });
    }

    protected boolean inCompleteInfo() {
        return fullNameField.getText().isBlank() ||
                genderField.getValue() == null ||
                classField.getText().isBlank() ||
                majorField.getValue() == null ||
                phoneField.getText().isBlank() ||
                emailField.getText().isBlank() ||
                usernameField.getText().isBlank() ||
                passwordField.getText().isBlank();
    }
}
