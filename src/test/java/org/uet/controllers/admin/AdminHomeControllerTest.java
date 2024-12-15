package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;

import static org.junit.jupiter.api.Assertions.*;

class AdminHomeControllerTest {

    private AdminHomeController controller;
    private Label welcomeLabel;
    private Button userButton, documentButton, libraryButton, bookApiButton, closeButton;
    private AnchorPane container;

    @BeforeEach
    void setUp() {
        // Khởi tạo JavaFX Toolkit
        JavaFXInitializer.initialize();

        // Khởi tạo các component JavaFX
        Platform.runLater(() -> {
            controller = new AdminHomeController();
            welcomeLabel = new Label();
            userButton = new Button();
            documentButton = new Button();
            libraryButton = new Button();
            bookApiButton = new Button();
            closeButton = new Button();
            container = new AnchorPane();

            controller.welcomeLabel = welcomeLabel;
            controller.documentButton = documentButton;
            controller.userButton = userButton;
            controller.libraryButton = libraryButton;
            controller.bookApiButton = bookApiButton;
            controller.closeButton = closeButton;
            controller.container = container;
        });
    }

    @Test
    void testSetWelcomeMessage() {
        Platform.runLater(() -> {
            String message = "Welcome, Admin!";
            controller.setWelcomeMessage(message);
            assertEquals(message, welcomeLabel.getText());
        });
    }

    @Test
    void testRemoveHighlightFromAllButtons() {
        Platform.runLater(() -> {
            userButton.getStyleClass().add("highlighted-button");
            documentButton.getStyleClass().add("highlighted-button");
            libraryButton.getStyleClass().add("highlighted-button");
            bookApiButton.getStyleClass().add("highlighted-button");

            controller.removeHighlightFromAllButtons();

            assertFalse(userButton.getStyleClass().contains("highlighted-button"));
            assertFalse(documentButton.getStyleClass().contains("highlighted-button"));
            assertFalse(libraryButton.getStyleClass().contains("highlighted-button"));
            assertFalse(bookApiButton.getStyleClass().contains("highlighted-button"));
        });
    }
}
