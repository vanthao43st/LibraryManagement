package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.Library;

import static org.junit.jupiter.api.Assertions.*;

class LibraryManagementControllerTest {

    private LibraryManagementController controller;
    private ComboBox<String> searchCriteria, documentTypeField;
    private TextField searchField, userIdField, documentCodeField, quantityField;
    private TableView<Library> libraryTable;
    private TableColumn<Library, String> userIdColumn, documentCodeColumn, borrowDateColumn, dueDateColumn, returnDateColumn, statusColumn, documentTypeColumn;
    private TableColumn<Library, Integer> quantityColumn, lateDaysColumn;
    private TableColumn<Library, Double> fineColumn;
    private ObservableList<Library> libraryData;

    @BeforeEach
    void setUp() {
        controller = new LibraryManagementController();
        searchCriteria = new ComboBox<>();
        documentTypeField = new ComboBox<>();
        searchField = new TextField();
        userIdField = new TextField();
        documentCodeField = new TextField();
        quantityField = new TextField();
        libraryTable = new TableView<>();
        userIdColumn = new TableColumn<>();
        documentCodeColumn = new TableColumn<>();
        borrowDateColumn = new TableColumn<>();
        dueDateColumn = new TableColumn<>();
        returnDateColumn = new TableColumn<>();
        statusColumn = new TableColumn<>();
        documentTypeColumn = new TableColumn<>();
        quantityColumn = new TableColumn<>();
        lateDaysColumn = new TableColumn<>();
        fineColumn = new TableColumn<>();
        libraryData = FXCollections.observableArrayList();

        controller.searchCriteria = searchCriteria;
        controller.documentTypeField = documentTypeField;
        controller.searchField = searchField;
        controller.userIdField = userIdField;
        controller.documentCodeField = documentCodeField;
        controller.quantityField = quantityField;
        controller.libraryTable = libraryTable;
        controller.userIdColumn = userIdColumn;
        controller.documentCodeColumn = documentCodeColumn;
        controller.borrowDateColumn = borrowDateColumn;
        controller.dueDateColumn = dueDateColumn;
        controller.returnDateColumn = returnDateColumn;
        controller.statusColumn = statusColumn;
        controller.documentTypeColumn = documentTypeColumn;
        controller.quantityColumn = quantityColumn;
        controller.lateDaysColumn = lateDaysColumn;
        controller.fineColumn = fineColumn;
        controller.libraryData.addAll(libraryData);
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            controller.initialize();
            assertNotNull(userIdColumn.getCellValueFactory());
            assertNotNull(documentCodeColumn.getCellValueFactory());
            assertNotNull(documentTypeColumn.getCellValueFactory());
            assertNotNull(quantityColumn.getCellValueFactory());
            assertNotNull(borrowDateColumn.getCellValueFactory());
            assertNotNull(dueDateColumn.getCellValueFactory());
            assertNotNull(returnDateColumn.getCellValueFactory());
            assertNotNull(statusColumn.getCellValueFactory());
            assertNotNull(lateDaysColumn.getCellValueFactory());
            assertNotNull(fineColumn.getCellValueFactory());
        });
    }

    @Test
    void testOnTableClick() {
        Library library = new Library("user01", "doc001", "Book", 1, "2021-12-01", "2021-12-14", "2021-12-15", "Returned");
        libraryData.add(library);
        libraryTable.setItems(libraryData);

        libraryTable.getSelectionModel().select(library);
        controller.onTableClick(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null));

        assertEquals("user01", userIdField.getText());
        assertEquals("doc001", documentCodeField.getText());
        assertEquals("Book", documentTypeField.getValue());
    }

    @Test
    void testOnBorrow() {
        userIdField.setText("user02");
        documentCodeField.setText("doc002");
        documentTypeField.setValue("Sách");
        quantityField.setText("1");

        controller.onBorrow(new ActionEvent());

        assertEquals(1, controller.libraryData.size());
        assertEquals("user02", controller.libraryData.get(0).getUserId());
        assertEquals("doc002", controller.libraryData.get(0).getDocumentCode());
    }

    @Test
    void testOnReturn() {
        Library library = new Library("user03", "doc003", "Sách", 1, "2021-12-01", null, "2021-12-15", "Borrowed");
        libraryData.add(library);
        libraryTable.setItems(libraryData);
        libraryTable.getSelectionModel().select(library);

        controller.selectedLibrary = library;
        quantityField.setText("1");

        controller.onReturn(new ActionEvent());

        assertNotNull(controller.libraryData.get(0).getReturnDate());
    }

    @Test
    void testOnDelete() {
        Library library = new Library("user04", "doc004", "Sách", 1, "2021-12-01", null, "2021-12-15", "Borrowed");
        libraryData.add(library);
        libraryTable.setItems(libraryData);
        libraryTable.getSelectionModel().select(library);

        controller.selectedLibrary = library;

        controller.onDelete(new ActionEvent());

        assertFalse(controller.libraryData.contains(library));
    }

    @Test
    void testOnSearch() {
        Library library1 = new Library("user05", "doc005", "Sách", 1, "2021-12-01", "2021-12-14", "2021-12-15", "Returned");
        Library library2 = new Library("user06", "doc006", "Luận văn", 1, "2021-12-01", "2021-12-14", "2021-12-15", "Returned");
        libraryData.addAll(library1, library2);
        libraryTable.setItems(libraryData);

        searchCriteria.setValue("User ID");
        searchField.setText("user05");

        controller.onSearch(new ActionEvent());

        assertEquals(1, libraryTable.getItems().size());
        assertEquals("user05", libraryTable.getItems().get(0).getUserId());
    }
}
