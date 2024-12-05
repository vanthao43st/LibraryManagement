package org.uet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.uet.entity.Book;

public class BookDetailsDialogController {

    @FXML
    private Label codeLabel, titleLabel, descriptionLabel, categoryLabel, authorLabel, priceLabel, quantityLabel;

    // Truyền dữ liệu từ bảng sang các trường trong hộp thoại
    public void setBookDetails(Book book) {
        codeLabel.setText(book.getCode());
        titleLabel.setText(book.getTitle());
        descriptionLabel.setText(book.getDescription());
        categoryLabel.setText(book.getCategory());
        authorLabel.setText(book.getAuthor());
        priceLabel.setText(String.format("%.2f VND", book.getPrice()));
        quantityLabel.setText(String.valueOf(book.getQuantity()));
    }

    @FXML
    private void onClose(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }
}
