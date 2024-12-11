package org.uet.controllers.user;

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
        showBookButton.setOnAction(e -> showComponent("/Views/User/BookManagement.fxml"));
        showThesisButton.setOnAction(e -> showComponent("/Views/User/ThesisManagement.fxml"));
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
