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
<<<<<<< Updated upstream

=======
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream

        // Xử lý sự kiện di chuyển ứng dụng
        container.setOnMousePressed(this::onMousePressed);
        container.setOnMouseDragged(this::onMouseDragged);

        // Gắn sự kiện highlight cho các nút
        attachHighlightToButton(userButton, "/Views/UserManagement.fxml");
        attachHighlightToButton(bookButton, "/Views/BookManagement.fxml");
        attachHighlightToButton(libraryButton, "/Views/LibraryManagement.fxml");
        attachHighlightToButton(bookApiButton, "/Views/BookAPI.fxml");
=======
        userButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showComponent("/Views/UserManagement.fxml");
            }
        });
>>>>>>> Stashed changes

        bookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showComponent("/Views/BookManagement.fxml");
            }
        });

        libraryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showComponent("/Views/StudentSearch.fxml");
            }
        });

        setTooltipDelay(tooltip1, Duration.seconds(0.5));
        setTooltipDelay(tooltip2, Duration.seconds(0.5));
        setTooltipDelay(tooltip3, Duration.seconds(0.5));
        setTooltipDelay(tooltip4, Duration.seconds(0.5));

        closeButton.setOnMouseClicked(e -> {
            System.exit(-1);
        });
    }

<<<<<<< Updated upstream
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

    // Highlight nút khi được nhấn
    private void attachHighlightToButton(Button button, String fxmlPath) {
        button.setOnAction(actionEvent -> {
            removeHighlightFromAllButtons(); // Loại bỏ highlight từ tất cả các nút
            button.getStyleClass().add("highlighted-button"); // Thêm class highlight vào nút hiện tại
            showComponent(fxmlPath);
        });
    }

    // Loại bỏ highlight từ tất cả các nút
    private void removeHighlightFromAllButtons() {
        Button[] buttons = {bookButton, userButton, libraryButton, bookApiButton};
        for (Button btn : buttons) {
            btn.getStyleClass().remove("highlighted-button");
        }
    }

=======
    private void setTooltipDelay(Tooltip tooltip, Duration delay) {
        Timeline timeline = new Timeline(new KeyFrame(delay, e -> tooltip.hide()));
        tooltip.setOnShown(event -> timeline.playFromStart());
    }
>>>>>>> Stashed changes

    private void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }

    @FXML
    public void showComponent(String path) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(path));
            AnchorPane component = loader.load();
            setNode(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
