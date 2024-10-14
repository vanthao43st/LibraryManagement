package org.uet.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeButton.setOnMouseClicked(e -> {
                    System.exit(-1);
                }
        );
    }

    @FXML
    private void handleLoginAction() {
        if (usernameField.getText().trim().equals("admin") || passwordField.getText().trim().equals("admin")) {
            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/Views/LibraryManagement.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Incorrect username or password");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }

    }

    @FXML
    private Button closeButton, loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
}
