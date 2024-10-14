package org.uet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class LibraryManagement implements Initializable {


    @FXML
    private Button closeBtn;

    @FXML
    private ToggleGroup genderGroup;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeBtn.setOnMouseClicked(e -> {
            System.exit(0);
        });
    }

    @FXML
    public void handleUpdate(ActionEvent actionEvent) {
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
    }
}
