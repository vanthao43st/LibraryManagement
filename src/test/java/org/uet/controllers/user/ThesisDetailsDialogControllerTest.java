package org.uet.controllers.user;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;
import org.uet.entity.Thesis;

import static org.junit.jupiter.api.Assertions.*;

class ThesisDetailsDialogControllerTest {

    private ThesisDetailsDialogController controller;
    private Label codeLabel, titleLabel, descriptionLabel, quantityLabel, majorLabel,
            authorLabel, supervisorLabel, universityLabel, degreeLabel, submissionYearLabel;

    @BeforeEach
    void setUp() {
        JavaFXInitializer.initialize();

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
        Thesis thesis = new Thesis("John Doe", "T123", "This is a description of the thesis.", "Thesis Title",
                "Master", "Computer Science", 10, 2022, "Dr. Smith", "UET");

        controller.setThesisDetails(thesis);

        assertEquals("T123", codeLabel.getText());
        assertEquals("Thesis Title", titleLabel.getText());
        assertEquals("This is a description of the thesis.", descriptionLabel.getText());
        assertEquals("10", quantityLabel.getText());
        assertEquals("Computer Science", majorLabel.getText());
        assertEquals("John Doe", authorLabel.getText());
        assertEquals("Dr. Smith", supervisorLabel.getText());
        assertEquals("UET", universityLabel.getText());
        assertEquals("Master", degreeLabel.getText());
        assertEquals("2022", submissionYearLabel.getText());
    }

    @Test
    void testSetThesisDetailsWithLongDescription() {
        Platform.runLater(() -> {
            String longDescription = "This is a very long description that exceeds the 200 character limit. " +
                    "It should be truncated and appended with ellipses to indicate that there is more text to be read in the actual description.";
            Thesis thesis = new Thesis("John Doe", "T123", longDescription, "Thesis Title",
                    "Master", "Computer Science", 10, 2022, "Dr. Smith", "UET");

            controller.setThesisDetails(thesis);

            String expectedDescription = longDescription.substring(0, 200) + " ...";
            assertEquals(expectedDescription, descriptionLabel.getText());
        });
    }
}
