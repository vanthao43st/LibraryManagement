package org.uet.controllers.user;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.Book;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BookManagementControllerTest {

    private BookManagementController controller;
    private TextField searchField;
    private ComboBox<String> searchCriteria;
    private TableView<Book> bookTable;
    private TableColumn<Book, String> codeColumn, titleColumn, categoryColumn, authorColumn, descriptionColumn;
    private TableColumn<Book, Double> priceColumn;
    private TableColumn<Book, Integer> quantityColumn;
    private ObservableList<Book> bookData;

    @BeforeEach
    void setUp() {
        controller = new BookManagementController();
        searchField = new TextField();
        searchCriteria = new ComboBox<>();
        bookTable = new TableView<>();
        codeColumn = new TableColumn<>();
        titleColumn = new TableColumn<>();
        categoryColumn = new TableColumn<>();
        authorColumn = new TableColumn<>();
        descriptionColumn = new TableColumn<>();
        priceColumn = new TableColumn<>();
        quantityColumn = new TableColumn<>();
        bookData = FXCollections.observableArrayList();

        controller.searchField = searchField;
        controller.searchCriteria = searchCriteria;
        controller.bookTable = bookTable;
        controller.codeColumn = codeColumn;
        controller.titleColumn = titleColumn;
        controller.categoryColumn = categoryColumn;
        controller.authorColumn = authorColumn;
        controller.descriptionColumn = descriptionColumn;
        controller.priceColumn = priceColumn;
        controller.quantityColumn = quantityColumn;

        controller.initialize();
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            controller.initialize();

            assertNotNull(codeColumn.getCellValueFactory());
            assertNotNull(titleColumn.getCellValueFactory());
            assertNotNull(categoryColumn.getCellValueFactory());
            assertNotNull(authorColumn.getCellValueFactory());
            assertNotNull(descriptionColumn.getCellValueFactory());
            assertNotNull(priceColumn.getCellValueFactory());
            assertNotNull(quantityColumn.getCellValueFactory());

            assertTrue(bookTable.isEditable());
        });
    }

    @Test
    void testOnSearch() {
        Platform.runLater(() -> {
            searchCriteria.setValue("Title");
            searchField.setText("Java");

            // Giả lập dữ liệu trả về
            ArrayList<Book> books = new ArrayList<>();
            books.add(new Book("001", "Java Programming", "A comprehensive guide", "Programming", "Author A", 50000, 10));
            bookData.setAll(books);

            // Thực hiện tìm kiếm
            controller.onSearch(new ActionEvent());

            // Kiểm tra kết quả
            assertEquals(1, bookTable.getItems().size());
            assertEquals("Java Programming", bookTable.getItems().get(0).getTitle());
        });
    }

    @Test
    void testOnShowDetail() {
        Book book = new Book("123", "Effective Java", "A programming book", "Programming", "Joshua Bloch", 50000, 10);
        bookData.add(book);
        bookTable.setItems(bookData);
        bookTable.getSelectionModel().select(book);

        Platform.runLater(() -> {
            controller.onShowDetail(new ActionEvent());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/User/BookDetailsDialog.fxml"));
            try {
                loader.load();
                BookDetailsDialogController dialogController = loader.getController();
                dialogController.setBookDetails(book);

                assertEquals("123", dialogController.getCodeLabel().getText());
                assertEquals("Effective Java", dialogController.getTitleLabel().getText());
            } catch (IOException e) {
                fail("Failed to load BookDetailsDialog.fxml");
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
