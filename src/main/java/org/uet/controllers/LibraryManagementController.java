package org.uet.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LibraryManagementController implements Initializable {

    @FXML
    private Button bookButton, userButton, libraryButton, bookApiButton, closeButton;

    @FXML
    private Tooltip tooltip1, tooltip2, tooltip3, tooltip4;

    @FXML
    private AnchorPane container;

    // Tọa độ chuột để tính toán di chuyển
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Xử lý sự kiện di chuyển ứng dụng
        container.setOnMousePressed(this::onMousePressed);
        container.setOnMouseDragged(this::onMouseDragged);

        userButton.setOnAction(actionEvent -> showComponent("/Views/UserManagement.fxml"));

        bookApiButton.setOnAction(actionEvent -> {
            showComponent("/Views/BookAPI.fxml");
        });

        tooltip1.setShowDelay(Duration.seconds(0.5));
        tooltip2.setShowDelay(Duration.seconds(0.5));
        tooltip3.setShowDelay(Duration.seconds(0.5));
        tooltip4.setShowDelay(Duration.seconds(0.5));

        closeButton.setOnMouseClicked(e -> {
            System.exit(-1);
        });
    }

    // Ghi lại tọa độ chuột khi nhấn
    private void onMousePressed(MouseEvent event) {
        Stage stage = (Stage) container.getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    // Cập nhật vị trí ứng dụng khi kéo
    private void onMouseDragged(MouseEvent event) {
        Stage stage = (Stage) container.getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }


    private void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }

    @FXML
    public void showComponent(String path) {
        try {
            AnchorPane component = FXMLLoader.load(getClass().getResource(path));
            setNode(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
