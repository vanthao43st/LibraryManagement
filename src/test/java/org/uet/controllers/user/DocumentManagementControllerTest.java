package org.uet.controllers.user;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class DocumentManagementControllerTest {

    private DocumentManagementController controller;
    private Button showBookButton, showThesisButton;
    private AnchorPane container;

    @BeforeEach
    void setUp() {
        JavaFXInitializer.initialize();

        controller = new DocumentManagementController();
        showBookButton = new Button();
        showThesisButton = new Button();
        container = new AnchorPane();

        controller.showBookButton = showBookButton;
        controller.showThesisButton = showThesisButton;
        controller.container = container;

        controller.initialize(null, null);
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            showBookButton.fire();
            showThesisButton.fire();
        });
    }

    @Test
    void testShowComponent() {
        Platform.runLater(() -> {
            try {
                controller.showComponent("/Views/User/BookManagement.fxml");

                AnchorPane component = FXMLLoader.load(getClass().getResource("/Views/User/BookManagement.fxml"));

                controller.container.getChildren().clear();
                controller.container.getChildren().add(component);

                assertFalse(controller.container.getChildren().isEmpty());
                assertTrue(controller.container.getChildren().contains(component));
            } catch (IOException e) {
                fail("Failed to load /Views/User/BookManagement.fxml");
            }
        });
    }
}
