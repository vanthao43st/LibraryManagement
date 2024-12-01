package org.uet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.uet.entity.Document;

public class BookDetailsDialogController {

    @FXML
    private Label codeLabel, titleLabel, descriptionLabel, categoryLabel, authorLabel, priceLabel, quantityLabel;

    // Truyền dữ liệu từ bảng sang các trường trong hộp thoại
    public void setBookDetails(Document document) {
        codeLabel.setText(document.getCode());
        titleLabel.setText(document.getTitle());
        descriptionLabel.setText(document.getDescription());
        categoryLabel.setText(document.getCategory());
        authorLabel.setText(document.getAuthor());
        priceLabel.setText(String.format("%.2f VND", document.getPrice()));
        quantityLabel.setText(String.valueOf(document.getQuantity()));
    }

    @FXML
    private void onClose(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }
}
