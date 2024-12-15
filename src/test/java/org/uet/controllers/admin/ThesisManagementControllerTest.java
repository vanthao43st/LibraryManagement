package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;
import org.uet.entity.Thesis;

import static org.junit.jupiter.api.Assertions.*;

class ThesisManagementControllerTest {

    private ThesisManagementController controller;
    private TextField thesisCodeField, thesisTitleField, thesisMajorField, thesisAuthorField,
            thesisSupervisorField, thesisUniversityField, thesisDegreeField,
            thesisSubmissionYearField, thesisDescriptionField, searchField, thesisQuantityField;
    private ComboBox<String> searchCriteria;
    private TableView<Thesis> thesisTable;
    private TableColumn<Thesis, String> codeColumn, titleColumn, majorColumn, authorColumn, supervisorColumn, universityColumn, degreeColumn, descriptionColumn;
    private TableColumn<Thesis, Integer> submissionYearColumn, quantityColumn;
    private ObservableList<Thesis> thesisData;

    @BeforeEach
    void setUp() {
        JavaFXInitializer.initialize();

        controller = new ThesisManagementController();
        thesisCodeField = new TextField();
        thesisTitleField = new TextField();
        thesisMajorField = new TextField();
        thesisAuthorField = new TextField();
        thesisSupervisorField = new TextField();
        thesisUniversityField = new TextField();
        thesisDegreeField = new TextField();
        thesisSubmissionYearField = new TextField();
        thesisDescriptionField = new TextField();
        searchField = new TextField();
        thesisQuantityField = new TextField();
        searchCriteria = new ComboBox<>();
        thesisTable = new TableView<>();
        codeColumn = new TableColumn<>();
        titleColumn = new TableColumn<>();
        majorColumn = new TableColumn<>();
        authorColumn = new TableColumn<>();
        supervisorColumn = new TableColumn<>();
        universityColumn = new TableColumn<>();
        degreeColumn = new TableColumn<>();
        descriptionColumn = new TableColumn<>();
        submissionYearColumn = new TableColumn<>();
        quantityColumn = new TableColumn<>();
        thesisData = FXCollections.observableArrayList();

        controller.thesisCodeField = thesisCodeField;
        controller.thesisTitleField = thesisTitleField;
        controller.thesisMajorField = thesisMajorField;
        controller.thesisAuthorField = thesisAuthorField;
        controller.thesisSupervisorField = thesisSupervisorField;
        controller.thesisUniversityField = thesisUniversityField;
        controller.thesisDegreeField = thesisDegreeField;
        controller.thesisSubmissionYearField = thesisSubmissionYearField;
        controller.thesisDescriptionField = thesisDescriptionField;
        controller.searchField = searchField;
        controller.thesisQuantityField = thesisQuantityField;
        controller.searchCriteria = searchCriteria;
        controller.thesisTable = thesisTable;
        controller.codeColumn = codeColumn;
        controller.titleColumn = titleColumn;
        controller.majorColumn = majorColumn;
        controller.authorColumn = authorColumn;
        controller.supervisorColumn = supervisorColumn;
        controller.universityColumn = universityColumn;
        controller.degreeColumn = degreeColumn;
        controller.descriptionColumn = descriptionColumn;
        controller.submissionYearColumn = submissionYearColumn;
        controller.quantityColumn = quantityColumn;
        controller.thesisData.addAll(thesisData);
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            controller.initialize();

            assertNotNull(codeColumn.getCellValueFactory());
            assertNotNull(titleColumn.getCellValueFactory());
            assertNotNull(majorColumn.getCellValueFactory());
            assertNotNull(authorColumn.getCellValueFactory());
            assertNotNull(supervisorColumn.getCellValueFactory());
            assertNotNull(universityColumn.getCellValueFactory());
            assertNotNull(degreeColumn.getCellValueFactory());
            assertNotNull(submissionYearColumn.getCellValueFactory());
            assertNotNull(descriptionColumn.getCellValueFactory());
            assertNotNull(quantityColumn.getCellValueFactory());
        });
    }

    @Test
    void testOnTableClick() {
        Platform.runLater(() -> {

            Thesis thesis = new Thesis("Author", "T001", "Description", "Title", "Bachelor", "Major", 1, 2023, "Supervisor", "University");
            thesisData.add(thesis);
            thesisTable.setItems(thesisData);

            thesisTable.getSelectionModel().select(thesis);
            controller.onTableClick(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null));

            assertEquals("T001", thesisCodeField.getText());
            assertEquals("Title", thesisTitleField.getText());
            assertEquals("Major", thesisMajorField.getText());
            assertEquals("Author", thesisAuthorField.getText());
            assertEquals("Supervisor", thesisSupervisorField.getText());
            assertEquals("University", thesisUniversityField.getText());
            assertEquals("Bachelor", thesisDegreeField.getText());
            assertEquals("2023", thesisSubmissionYearField.getText());
            assertEquals("Description", thesisDescriptionField.getText());
            assertEquals("1", thesisQuantityField.getText());
        });
    }

    @Test
    void testOnAdd() {
        Platform.runLater(() -> {
            thesisCodeField.setText("T002");
            thesisTitleField.setText("New Thesis");
            thesisMajorField.setText("New Major");
            thesisAuthorField.setText("New Author");
            thesisSupervisorField.setText("New Supervisor");
            thesisUniversityField.setText("New University");
            thesisDegreeField.setText("New Degree");
            thesisSubmissionYearField.setText("2024");
            thesisDescriptionField.setText("New Description");
            thesisQuantityField.setText("2");

            controller.onAdd(new ActionEvent());

            assertEquals(1, controller.thesisData.size());
            assertEquals("T002", controller.thesisData.get(0).getCode());
            assertEquals("New Thesis", controller.thesisData.get(0).getTitle());
            assertEquals("New Major", controller.thesisData.get(0).getMajor());
            assertEquals("New Author", controller.thesisData.get(0).getAuthor());
            assertEquals("New Supervisor", controller.thesisData.get(0).getSupervisor());
            assertEquals("New University", controller.thesisData.get(0).getUniversity());
            assertEquals("New Degree", controller.thesisData.get(0).getDegree());
            assertEquals(2024, controller.thesisData.get(0).getSubmissionYear());
            assertEquals("New Description", controller.thesisData.get(0).getDescription());
            assertEquals(2, controller.thesisData.get(0).getQuantity());

        });
    }

    @Test
    void testInCompleteInfo() {
        thesisCodeField.setText("");
        thesisTitleField.setText("Test Title");
        assertTrue(controller.inCompleteInfo());

        thesisCodeField.setText("T003");
        assertFalse(controller.inCompleteInfo());
    }

    @Test
    void testIsThesisExisted() {
        Thesis thesis = new Thesis("Author", "T003", "Description", "Title", "Bachelor", "Major", 1, 2023, "Supervisor", "University");
        thesisData.add(thesis);
        thesisTable.setItems(thesisData);

        assertTrue(controller.isThesisExisted("T003"));
        assertFalse(controller.isThesisExisted("T004"));
    }

    @Test
    void testOnEdit() {
        Platform.runLater(() -> {
            Thesis thesis = new Thesis("Author", "T001", "Description", "Title", "Bachelor", "Major", 1, 2023, "Supervisor", "University");
            thesisData.add(thesis);
            thesisTable.setItems(thesisData);
            thesisTable.getSelectionModel().select(thesis);

            controller.selectedThesis = thesis;

            thesisTitleField.setText("Edited Thesis");
            thesisMajorField.setText("Edited Major");
            thesisAuthorField.setText("Edited Author");
            thesisSupervisorField.setText("Edited Supervisor");
            thesisUniversityField.setText("Edited University");
            thesisDegreeField.setText("Edited Degree");
            thesisSubmissionYearField.setText("2024");
            thesisDescriptionField.setText("Edited Description");
            thesisQuantityField.setText("3");

            controller.onEdit(new ActionEvent());

            assertEquals("Edited Thesis", thesis.getTitle());
            assertEquals("Edited Major", thesis.getMajor());
            assertEquals("Edited Author", thesis.getAuthor());
            assertEquals("Edited Supervisor", thesis.getSupervisor());
            assertEquals("Edited University", thesis.getUniversity());
            assertEquals("Edited Degree", thesis.getDegree());
            assertEquals(2024, thesis.getSubmissionYear());
            assertEquals("Edited Description", thesis.getDescription());
            assertEquals(3, thesis.getQuantity());
        });
    }

    @Test
    void testOnDelete() {
        Platform.runLater(() -> {
            Thesis thesis = new Thesis("Author", "T004", "Description", "Title", "Bachelor", "Major", 1, 2023, "Supervisor", "University");
            thesisData.add(thesis);
            thesisTable.setItems(thesisData);
            thesisTable.getSelectionModel().select(thesis);

            controller.selectedThesis = thesis;

            controller.onDelete(new ActionEvent());

            assertFalse(controller.thesisData.contains(thesis));
        });
    }

    @Test
    void testOnSearch() {
        Platform.runLater(() -> {
            Thesis thesis1 = new Thesis("Author1", "T003", "Description1", "Title1", "Bachelor", "Major1", 1, 2022, "Supervisor1", "University1");
            Thesis thesis2 = new Thesis("Author2", "T004", "Description2", "Title2", "Bachelor", "Major2", 2, 2023, "Supervisor2", "University2");
            thesisData.addAll(thesis1, thesis2);
            thesisTable.setItems(thesisData);

            searchCriteria.setValue("Title");
            searchField.setText("Title1");

            controller.onSearch(new ActionEvent());

            assertEquals(1, thesisTable.getItems().size());
            assertEquals("Title1", thesisTable.getItems().get(0).getTitle());
        });
    }

}