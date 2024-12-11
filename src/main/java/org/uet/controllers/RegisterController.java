package org.uet.controllers;

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

public class RegisterController implements Initializable {

    @FXML
    private TextField usernameField, passwordField, fullNameField, classField, phoneField, emailField;

    @FXML
    private ComboBox<String> genderField, majorField;

    @FXML
    private Button exitButton, registerButton;

    private final UserDao userDao = new UserDao();

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

    public String generateUniqueUserId() {
        Random random = new Random();
        String userId;

        do {
            // Random một ID có 8 chữ số
            userId = String.format("%08d", random.nextInt(100000000));

        } while (userDao.doesUserIdExist(userId));

        return userId;
    }

    private void returnLoginPage(Button button) {
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
    private void showAlert(String title, String message, Alert.AlertType type) {
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

        if (userDao.doesUsernameExist(usernameField.getText())) {
            showAlert("Lỗi", "username đã tồn tại. Vui lòng nhập username khác!", Alert.AlertType.ERROR);
            return;
        }

        try {
            userDao.addUser(
                    new User(
                            generateUniqueUserId(),
                            fullNameField.getText(),
                            Gender.valueOf(genderField.getValue()),
                            classField.getText(),
                            majorField.getValue(),
                            phoneField.getText(),
                            emailField.getText(),
                            usernameField.getText(),
                            passwordField.getText()
                    )
            );
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        showAlert("Thông báo", "Đăng ký thành công.", Alert.AlertType.INFORMATION);

        returnLoginPage(registerButton);
    }

    private boolean inCompleteInfo() {
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
