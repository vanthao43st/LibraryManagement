package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManagementControllerTest {

    private DocumentManagementController controller;
    private Button showBookButton;
    private Button showThesisButton;
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
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            controller.initialize(null, null);

            assertNotNull(showBookButton.getOnAction());
            assertNotNull(showThesisButton.getOnAction());
        });
    }

    @Test
    void testShowComponent() throws Exception {
        Platform.runLater(() -> {
            String path = "/Views/Admin/BookManagement.fxml";
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(path));
            AnchorPane component = null;
            try {
                component = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            controller.showComponent(path);

            assertFalse(container.getChildren().isEmpty());
            assertEquals(component, container.getChildren().get(0));
        });
    }

    @Test
    void testSetNode() {
        Platform.runLater(() -> {
            Node node = new AnchorPane();
            controller.setNode(node);

            assertFalse(container.getChildren().isEmpty());
            assertEquals(node, container.getChildren().get(0));
        });
    }
}
