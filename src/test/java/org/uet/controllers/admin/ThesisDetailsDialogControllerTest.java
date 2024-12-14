package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.Thesis;

import static org.junit.jupiter.api.Assertions.*;

class ThesisDetailsDialogControllerTest {

    private ThesisDetailsDialogController controller;
    private Label codeLabel, titleLabel, descriptionLabel, quantityLabel, majorLabel,
            authorLabel, supervisorLabel, universityLabel, degreeLabel, submissionYearLabel;

    @BeforeEach
    void setUp() {
        controller = new ThesisDetailsDialogController();
        codeLabel = new Label();
        titleLabel = new Label();
        descriptionLabel = new Label();
        quantityLabel = new Label();
        majorLabel = new Label();
        authorLabel = new Label();
        supervisorLabel = new Label();
        universityLabel = new Label();
        degreeLabel = new Label();
        submissionYearLabel = new Label();

        controller.codeLabel = codeLabel;
        controller.titleLabel = titleLabel;
        controller.descriptionLabel = descriptionLabel;
        controller.quantityLabel = quantityLabel;
        controller.majorLabel = majorLabel;
        controller.authorLabel = authorLabel;
        controller.supervisorLabel = supervisorLabel;
        controller.universityLabel = universityLabel;
        controller.degreeLabel = degreeLabel;
        controller.submissionYearLabel = submissionYearLabel;
    }

    @Test
    void testSetThesisDetails() {
        Thesis thesis = new Thesis("Author Name", "T001", "This is a description for the thesis. It may be quite long but it should be handled properly by the controller.",
                "Thesis Title", "Bachelor", "Computer Science", 5, 2023, "Supervisor Name", "University Name");

        controller.setThesisDetails(thesis);

        assertEquals("T001", codeLabel.getText());
        assertEquals("Thesis Title", titleLabel.getText());
        assertEquals("This is a description for the thesis. It may be quite long but it should be handled properly by the controller. ...", descriptionLabel.getText());
        assertEquals("5", quantityLabel.getText());
        assertEquals("Computer Science", majorLabel.getText());
        assertEquals("Author Name", authorLabel.getText());
        assertEquals("Supervisor Name", supervisorLabel.getText());
        assertEquals("University Name", universityLabel.getText());
        assertEquals("Bachelor", degreeLabel.getText());
        assertEquals("2023", submissionYearLabel.getText());
    }

    @Test
    void testOnClose() {
        Button closeButton = new Button();
        closeButton.setOnAction(event -> controller.onClose(event));

        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(new javafx.scene.layout.StackPane(closeButton), 100, 100));
            stage.show();

            assertTrue(stage.isShowing());

            closeButton.fireEvent(new ActionEvent());

            assertFalse(stage.isShowing());
        });
    }
}
