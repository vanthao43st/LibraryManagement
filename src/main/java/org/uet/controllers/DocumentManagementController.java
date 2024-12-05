package org.uet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DocumentManagementController implements Initializable {
    @FXML
    public Button showBookButton, showThesisButton;

    @FXML
    private AnchorPane container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Gắn sự kiện highlight cho các nút
        attachHighlightToButton(showBookButton, "/Views/BookManagement.fxml");
        attachHighlightToButton(showThesisButton, "/Views/BookManagement.fxml");
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
        Button[] buttons = {showBookButton, showThesisButton};
        for (Button btn : buttons) {
            btn.getStyleClass().remove("highlighted-button");
        }
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

    private void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }

}
