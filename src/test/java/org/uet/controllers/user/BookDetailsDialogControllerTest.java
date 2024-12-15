package org.uet.controllers.user;

import javafx.application.Platform;
import javafx.scene.control.Label;
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
            Book book = new Book("123", "Effective Java", "A programming book", "Programming", "Joshua Bloch", 50000, 10);

            controller.setBookDetails(book);

            assertEquals("123", codeLabel.getText());
            assertEquals("Effective Java", titleLabel.getText());
            assertEquals("A programming book", descriptionLabel.getText());
            assertEquals("Programming", categoryLabel.getText());
            assertEquals("Joshua Bloch", authorLabel.getText());
            assertEquals("50000.00 VND", priceLabel.getText());
            assertEquals("10", quantityLabel.getText());
        });
    }

    @Test
    void testSetBookDetailsWithLongDescription() {
        Platform.runLater(() -> {
            String longDescription = "This is a very long description that exceeds the 200 character limit. " +
                    "It should be truncated and appended with ellipses to indicate that there is more text.";
            Book book = new Book("123", "Effective Java", longDescription, "Programming", "Joshua Bloch", 50000, 10);

            controller.setBookDetails(book);

            String expectedDescription = longDescription.substring(0, 200) + " ...";
            assertEquals(expectedDescription, descriptionLabel.getText());
        });
    }
}
