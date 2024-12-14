package org.uet.controllers.admin;

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
    protected AnchorPane container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showBookButton.setOnAction(actionEvent -> showComponent("/Views/Admin/BookManagement.fxml"));
        showThesisButton.setOnAction(actionEvent -> showComponent("/Views/Admin/ThesisManagement.fxml"));
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

    protected void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }
}
