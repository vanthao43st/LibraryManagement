package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.User;
import org.uet.enums.Gender;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsDialogControllerTest {

    private UserDetailsDialogController controller;
    private Label userIdLabel, userNameLabel, genderLabel, classLabel, majorLabel, phoneLabel, emailLabel, statusLabel;

    @BeforeEach
    void setUp() {
        controller = new UserDetailsDialogController();
        userIdLabel = new Label();
        userNameLabel = new Label();
        genderLabel = new Label();
        classLabel = new Label();
        majorLabel = new Label();
        phoneLabel = new Label();
        emailLabel = new Label();
        statusLabel = new Label();

        controller.userIdLabel = userIdLabel;
        controller.userNameLabel = userNameLabel;
        controller.genderLabel = genderLabel;
        controller.classLabel = classLabel;
        controller.majorLabel = majorLabel;
        controller.phoneLabel = phoneLabel;
        controller.emailLabel = emailLabel;
        controller.statusLabel = statusLabel;
    }

    @Test
    void testSetUserDetails() {
        User user = new User("123", "John Doe", Gender.MALE, "Class A", "Computer Science", "1234567890", "john@example.com");

        Platform.runLater(() -> {
            controller.setUserDetails(user);

            assertEquals("123", userIdLabel.getText());
            assertEquals("John Doe", userNameLabel.getText());
            assertEquals("MALE", genderLabel.getText());
            assertEquals("Class A", classLabel.getText());
            assertEquals("Computer Science", majorLabel.getText());
            assertEquals("1234567890", phoneLabel.getText());
            assertEquals("john@example.com", emailLabel.getText());
        });
    }

    @Test
    void testOnClose() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(new javafx.scene.layout.StackPane(userIdLabel), 100, 100));
            stage.show();

            assertTrue(stage.isShowing());

            controller.onClose();

            assertFalse(stage.isShowing());
        });
    }
}
