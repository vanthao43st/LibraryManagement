package org.uet.controllers.user;

import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;
import org.uet.entity.Library;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LibraryManagementControllerTest {

    private LibraryManagementController controller;
    private ComboBox<String> searchCriteria, documentTypeField;
    private TextField searchField, documentCodeField, quantityField;
    private TableView<Library> libraryTable;
    private TableColumn<Library, String> documentCodeColumn, titleColumn, descriptionColumn, borrowDateColumn, dueDateColumn, statusColumn, documentTypeColumn;
    private TableColumn<Library, Integer> quantityColumn;
    private ObservableList<Library> libraryData;

    @BeforeEach
    void setUp() {
        JavaFXInitializer.initialize();

        controller = new LibraryManagementController();
        searchCriteria = new ComboBox<>();
        documentTypeField = new ComboBox<>();
        searchField = new TextField();
        documentCodeField = new TextField();
        quantityField = new TextField();
        libraryTable = new TableView<>();
        documentCodeColumn = new TableColumn<>();
        titleColumn = new TableColumn<>();
        descriptionColumn = new TableColumn<>();
        borrowDateColumn = new TableColumn<>();
        dueDateColumn = new TableColumn<>();
        statusColumn = new TableColumn<>();
        documentTypeColumn = new TableColumn<>();
        quantityColumn = new TableColumn<>();
        libraryData = FXCollections.observableArrayList();

        controller.searchCriteria = searchCriteria;
        controller.documentTypeField = documentTypeField;
        controller.searchField = searchField;
        controller.documentCodeField = documentCodeField;
        controller.quantityField = quantityField;
        controller.libraryTable = libraryTable;
        controller.documentCodeColumn = documentCodeColumn;
        controller.titleColumn = titleColumn;
        controller.descriptionColumn = descriptionColumn;
        controller.borrowDateColumn = borrowDateColumn;
        controller.dueDateColumn = dueDateColumn;
        controller.statusColumn = statusColumn;
        controller.documentTypeColumn = documentTypeColumn;
        controller.quantityColumn = quantityColumn;
        controller.libraryData.addAll(libraryData);

        controller.initialize();
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            controller.initialize();

            assertNotNull(documentCodeColumn.getCellValueFactory());
            assertNotNull(titleColumn.getCellValueFactory());
            assertNotNull(descriptionColumn.getCellValueFactory());
            assertNotNull(borrowDateColumn.getCellValueFactory());
            assertNotNull(dueDateColumn.getCellValueFactory());
            assertNotNull(statusColumn.getCellValueFactory());
            assertNotNull(documentTypeColumn.getCellValueFactory());
            assertNotNull(quantityColumn.getCellValueFactory());

            assertTrue(libraryTable.getItems().isEmpty());
        });
    }

    @Test
    void testOnSearch() {
        Platform.runLater(() -> {
            searchCriteria.setValue("Document Code");
            searchField.setText("123");

            // Giả lập dữ liệu trả về
            ArrayList<Library> libraries = new ArrayList<>();
            libraries.add(new Library("123", "Book", "Document 1", "Description 1", 10, "01-01-2022", "01-02-2022", "Available"));
            libraryData.setAll(libraries);

            // Thực hiện tìm kiếm
            controller.onSearch(new ActionEvent());

            // Kiểm tra kết quả
            assertEquals(1, libraryTable.getItems().size());
            assertEquals("Document 1", libraryTable.getItems().get(0).getTitle());
        });
    }

    @Test
    void testOnBorrow() {
        Platform.runLater(() -> {
            documentCodeField.setText("123");
            documentTypeField.setValue("Sách");
            quantityField.setText("1");

            controller.onBorrow(new ActionEvent());

            // Kiểm tra xem việc mượn tài liệu có thành công không
            assertEquals("", documentCodeField.getText());
            assertEquals("", quantityField.getText());
            assertNull(documentTypeField.getValue());
        });
    }

    @Test
    void testOnReturn() {
        Platform.runLater(() -> {
            documentCodeField.setText("123");
            documentTypeField.setValue("Sách");
            quantityField.setText("1");

            controller.selectedLibrary = new Library("123", "Book", "Document 1", "Description 1", 1, "01-01-2022", "01-02-2022", "Borrowed");

            controller.onReturn(new ActionEvent());

            // Kiểm tra xem việc trả tài liệu có thành công không
            assertEquals("", documentCodeField.getText());
            assertEquals("", quantityField.getText());
            assertNull(documentTypeField.getValue());
        });
    }

    @Test
    void testOnTableClick() {
        Platform.runLater(() -> {
            Library library = new Library("123", "Book", "Document 1", "Description 1", 1, "01-01-2022", "01-02-2022", "Borrowed");
            libraryData.add(library);
            libraryTable.setItems(libraryData);
            libraryTable.getSelectionModel().select(library);

            libraryTable.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null));

            assertEquals("Book", documentTypeField.getValue());
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
}
