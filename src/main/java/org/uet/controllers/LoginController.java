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
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button closeButton, loginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeButton.setOnMouseClicked(e -> System.exit(-1));

        // Hoãn thiết lập kéo thả đến khi scene sẵn sàng
        usernameField.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                addDraggableFeature();
            }
        });
    }

    @FXML
    private void handleLoginAction() {
        if (usernameField.getText().trim().equals(USERNAME)
                && passwordField.getText().trim().equals(PASSWORD)) {
            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root = FXMLLoader.load(
                        Objects.requireNonNull(
                                getClass().getResource("/Views/LibraryManagement.fxml")
                        )
                );
                Scene scene = new Scene(root, 900, 600);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Incorrect username or password");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }
    }

    private void addDraggableFeature() {
        usernameField.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((windowObservable, oldWindow, newWindow) -> {
                    if (newWindow instanceof Stage stage) {
                        // Ghi lại tọa độ khi nhấn chuột
                        newScene.setOnMousePressed(event -> {
                            xOffset = stage.getX() - event.getScreenX();
                            yOffset = stage.getY() - event.getScreenY();
                        });

                        // Cập nhật vị trí cửa sổ khi kéo chuột
                        newScene.setOnMouseDragged(event -> {
                            stage.setX(event.getScreenX() + xOffset);
                            stage.setY(event.getScreenY() + yOffset);
                        });
                    }
                });
            }
        });
    }
}
