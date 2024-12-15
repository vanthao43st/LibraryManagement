package org.uet.controllers.user;

import javafx.scene.control.DialogPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.Thesis;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ThesisManagementControllerTest {

    private ThesisManagementController controller;
    private TextField searchField;
    private ComboBox<String> searchCriteria;
    private TableView<Thesis> thesisTable;
    private TableColumn<Thesis, String> codeColumn, titleColumn, majorColumn, authorColumn, supervisorColumn, universityColumn, degreeColumn, descriptionColumn;
    private TableColumn<Thesis, Integer> submissionYearColumn, quantityColumn;
    private ObservableList<Thesis> thesisData;

    @BeforeEach
    void setUp() {
        controller = new ThesisManagementController();
        searchField = new TextField();
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

        controller.searchField = searchField;
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

        controller.initialize();
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
            assertNotNull(descriptionColumn.getCellValueFactory());
            assertNotNull(submissionYearColumn.getCellValueFactory());
            assertNotNull(quantityColumn.getCellValueFactory());

            assertTrue(thesisTable.getItems().isEmpty());
        });
    }

    @Test
    void testOnSearch() {
        Platform.runLater(() -> {
            searchCriteria.setValue("Code");
            searchField.setText("T123");

            // Giả lập dữ liệu trả về
            ArrayList<Thesis> theses = new ArrayList<>();
            theses.add(new Thesis("John Doe", "T123", "Description", "Thesis Title",
                    "Master", "Computer Science", 10, 2022, "Dr. Smith", "UET"));
            thesisData.setAll(theses);

            // Thực hiện tìm kiếm
            controller.onSearch(new ActionEvent());

            // Kiểm tra kết quả
            assertEquals(1, thesisTable.getItems().size());
            assertEquals("Thesis Title", thesisTable.getItems().get(0).getTitle());
        });
    }

    @Test
    void testOnShowDetail() {
        Platform.runLater(() -> {
            Thesis thesis = new Thesis("John Doe", "T123", "Description", "Thesis Title",
                    "Master", "Computer Science", 10, 2022, "Dr. Smith", "UET");
            thesisData.add(thesis);
            thesisTable.setItems(thesisData);
            thesisTable.getSelectionModel().select(thesis);

            controller.onShowDetail(new ActionEvent());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/User/ThesisDetailsDialog.fxml"));
                loader.load();
                ThesisDetailsDialogController dialogController = loader.getController();
                dialogController.setThesisDetails(thesis);

                assertEquals("T123", dialogController.codeLabel.getText());
                assertEquals("Thesis Title", dialogController.titleLabel.getText());
            } catch (IOException e) {
                fail("Failed to load /Views/User/ThesisDetailsDialog.fxml");
            }
        });
    }

    @Test
    void testShowAlert() {
        Platform.runLater(() -> {
            controller.showAlert("Test Alert", "This is a test alert message", AlertType.INFORMATION);

            Alert alert = new Alert(AlertType.INFORMATION);
            assertEquals("Test Alert", alert.getTitle());
            assertEquals("This is a test alert message", alert.getContentText());
        });
    }

    @Test
    void testEnableDragging() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            DialogPane dialogPane = new DialogPane();

            controller.enableDragging(stage, dialogPane);

            dialogPane.fireEvent(new javafx.scene.input.MouseEvent(javafx.scene.input.MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null));
            stage.setX(100);
            stage.setY(100);
            dialogPane.fireEvent(new javafx.scene.input.MouseEvent(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, 200, 200, 200, 200, null, 0, false, false, false, false, false, false, false, false, false, false, null));

            assertEquals(300, stage.getX());
            assertEquals(300, stage.getY());
        });
    }
}
