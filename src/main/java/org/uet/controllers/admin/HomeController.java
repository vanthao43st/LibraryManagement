package org.uet.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button documentButton, userButton, libraryButton, bookApiButton, closeButton;

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

        // Gắn sự kiện highlight cho các nút
        attachHighlightToButton(userButton, "/Views/Admin/UserManagement.fxml");
        attachHighlightToButton(documentButton, "/Views/Admin/DocumentManagement.fxml");
        attachHighlightToButton(libraryButton, "/Views/Admin/LibraryManagement.fxml");
        attachHighlightToButton(bookApiButton, "/Views/Admin/BookAPI.fxml");

        tooltip1.setShowDelay(Duration.seconds(0.5));
        tooltip2.setShowDelay(Duration.seconds(0.5));
        tooltip3.setShowDelay(Duration.seconds(0.5));
        tooltip4.setShowDelay(Duration.seconds(0.5));

        closeButton.setOnMouseClicked(e -> {
            try {
                // Tải lại Login.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login.fxml"));
                Parent root = loader.load();

                // Lấy cửa sổ (Stage) hiện tại và thay đổi scene
                Stage stage = (Stage) closeButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene); // Đổi scene sang màn hình Login

                // Hiển thị lại cửa sổ mới
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
        Button[] buttons = {documentButton, userButton, libraryButton, bookApiButton};
        for (Button btn : buttons) {
            btn.getStyleClass().remove("highlighted-button");
        }
    }


    private void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }

    @FXML
    public void showComponent(String path) {
        try {
            AnchorPane component = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(path))
            );
            setNode(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
