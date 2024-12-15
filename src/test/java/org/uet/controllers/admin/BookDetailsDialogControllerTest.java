package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;
import org.uet.entity.Book;

import static org.junit.jupiter.api.Assertions.*;

class BookDetailsDialogControllerTest {

    private BookDetailsDialogController controller;
    private Label codeLabel, titleLabel, descriptionLabel, categoryLabel, authorLabel, priceLabel, quantityLabel;

    @BeforeEach
    void setUp() {
        // Khởi tạo JavaFX Toolkit
        JavaFXInitializer.initialize();

        controller = new BookDetailsDialogController();
        codeLabel = new Label();
        titleLabel = new Label();
        descriptionLabel = new Label();
        categoryLabel = new Label();
        authorLabel = new Label();
        priceLabel = new Label();
        quantityLabel = new Label();

        controller.codeLabel = codeLabel;
        controller.titleLabel = titleLabel;
        controller.descriptionLabel = descriptionLabel;
        controller.categoryLabel = categoryLabel;
        controller.authorLabel = authorLabel;
        controller.priceLabel = priceLabel;
        controller.quantityLabel = quantityLabel;
    }

    @Test
    void testSetBookDetails() {
        Platform.runLater(() -> {
            Book book = new Book("1234567890", "Test Title", "A very long description that is more than two hundred characters long. "
                    + "This is to test if the description is properly truncated by the setBookDetails method to ensure it fits within the UI constraints.",
                    "Test Category", "Test Author", 199.99, 10);

            controller.setBookDetails(book);

            assertEquals("1234567890", codeLabel.getText());
            assertEquals("Test Title", titleLabel.getText());
            assertEquals("Test Category", categoryLabel.getText());
            assertEquals("Test Author", authorLabel.getText());
            assertEquals("199.99 VND", priceLabel.getText());
            assertEquals("10", quantityLabel.getText());
            assertEquals("A very long description that is more than two hundred characters long. This is to test if the description is properly truncated by the setBookDetails method to ensure it fits within the UI constraints. ...", descriptionLabel.getText());

        });
    }

    @Test
    void testOnClose() {
        Platform.runLater(() -> {
            Button closeButton = new Button();
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(new javafx.scene.layout.Pane(), 100, 100));
            closeButton.setOnAction(event -> controller.onClose(event));

            stage.show();
            assertTrue(stage.isShowing());

            ActionEvent event = new ActionEvent(closeButton, null);
            closeButton.fireEvent(event);

            assertFalse(stage.isShowing());
        });
    }
}
