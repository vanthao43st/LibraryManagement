package org.uet.controllers.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.uet.entity.Thesis;

public class ThesisDetailsDialogController {

    @FXML
    private Label codeLabel, titleLabel, descriptionLabel, quantityLabel, majorLabel,
            authorLabel, supervisorLabel, universityLabel, degreeLabel, submissionYearLabel;

    // Method to set thesis details to the dialog fields
    public void setThesisDetails(Thesis thesis) {
        codeLabel.setText(thesis.getCode());
        titleLabel.setText(thesis.getTitle());
        descriptionLabel.setText(thesis.getDescription());
        quantityLabel.setText(String.valueOf(thesis.getQuantity()));
        majorLabel.setText(thesis.getMajor());
        authorLabel.setText(thesis.getAuthor());
        supervisorLabel.setText(thesis.getSupervisor());
        universityLabel.setText(thesis.getUniversity());
        degreeLabel.setText(thesis.getDegree());
        submissionYearLabel.setText(String.valueOf(thesis.getSubmissionYear()));

        String description = thesis.getDescription();
        String newDescription;
        if (description!=null && description.length() > 200) {
            newDescription = description.substring(0,200) + " ...";
        } else {
            newDescription = description;
        }
        descriptionLabel.setText(newDescription);
    }

    @FXML
    private void onClose(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }
}
