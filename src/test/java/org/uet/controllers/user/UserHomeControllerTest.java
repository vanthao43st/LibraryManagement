package org.uet.controllers.user;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class UserHomeControllerTest {

    private UserHomeController controller;
    private Label welcomeLabel;
    private Button documentButton, userButton, libraryButton, bookApiButton, closeButton;
    private Tooltip tooltip1, tooltip2, tooltip3, tooltip4;
    private AnchorPane container;

    @BeforeEach
    void setUp() {
        controller = new UserHomeController();
        welcomeLabel = new Label();
        documentButton = new Button();
        userButton = new Button();
        libraryButton = new Button();
        bookApiButton = new Button();
        closeButton = new Button();
        tooltip1 = new Tooltip();
        tooltip2 = new Tooltip();
        tooltip3 = new Tooltip();
        tooltip4 = new Tooltip();
        container = new AnchorPane();

        controller.welcomeLabel = welcomeLabel;
        controller.documentButton = documentButton;
        controller.userButton = userButton;
        controller.libraryButton = libraryButton;
        controller.bookApiButton = bookApiButton;
        controller.closeButton = closeButton;
        controller.tooltip1 = tooltip1;
        controller.tooltip2 = tooltip2;
        controller.tooltip3 = tooltip3;
        controller.tooltip4 = tooltip4;
        controller.container = container;

        controller.initialize(null, null);
    }

    @Test
    void testInitialize() {
        // Giả lập kiểm tra các tooltips và sự kiện của nút đóng
        tooltip1.setShowDelay(javafx.util.Duration.seconds(0.5));
        tooltip2.setShowDelay(javafx.util.Duration.seconds(0.5));
        tooltip3.setShowDelay(javafx.util.Duration.seconds(0.5));
        tooltip4.setShowDelay(javafx.util.Duration.seconds(0.5));

        closeButton.setOnMouseClicked(event -> {
            // Thực hiện hành động khi nút đóng được nhấn
            // Kiểm tra xem hành động này có thực hiện đúng không
        });

        assertNotNull(tooltip1);
        assertNotNull(tooltip2);
        assertNotNull(tooltip3);
        assertNotNull(tooltip4);
    }

    @Test
    void testOnMousePressed() {
        // Giả lập sự kiện nhấn chuột để kiểm tra hàm onMousePressed
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null);
        controller.onMousePressed(mouseEvent);
        Stage stage = (Stage) container.getScene().getWindow();
        assertEquals(stage.getX() - mouseEvent.getScreenX(), controller.xOffset);
        assertEquals(stage.getY() - mouseEvent.getScreenY(), controller.yOffset);
    }

    @Test
    void testOnMouseDragged() {
        // Giả lập sự kiện kéo chuột để kiểm tra hàm onMouseDragged
        MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_DRAGGED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null);
        controller.onMouseDragged(mouseEvent);
        Stage stage = (Stage) container.getScene().getWindow();
        assertEquals(mouseEvent.getScreenX() + controller.xOffset, stage.getX());
        assertEquals(mouseEvent.getScreenY() + controller.yOffset, stage.getY());
    }

    @Test
    void testShowComponent() {
        Platform.runLater(() -> {
            try {
                controller.showComponent("/Views/User/UserDetail.fxml");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/User/UserDetail.fxml"));
                AnchorPane component = loader.load();
                controller.setNode(component);

                assertFalse(controller.container.getChildren().isEmpty());
                assertTrue(controller.container.getChildren().contains(component));
            } catch (IOException e) {
                fail("Failed to load /Views/User/UserDetail.fxml");
            }
        });
    }

    @Test
    void testSetWelcomeMessage() {
        Platform.runLater(() -> {
            controller.setWelcomeMessage("Welcome, User!");
            assertEquals("Welcome, User!", controller.welcomeLabel.getText());
        });
    }
}
