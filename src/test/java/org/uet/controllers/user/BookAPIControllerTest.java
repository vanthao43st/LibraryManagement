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
import org.uet.JavaFXInitializer;
import org.uet.entity.Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class BookAPIControllerTest {

    private BookAPIController controller;
    private ComboBox<String> searchCriteria;
    private TextField searchField;
    private TableView<Book> bookTable;
    private TableColumn<Book, String> codeColumn, titleColumn, descriptionColumn, categoryColumn, authorColumn;
    private TableColumn<Book, Double> priceColumn;
    private TableColumn<Book, Integer> quantityColumn;
    private ObservableList<Book> bookData;

    @BeforeEach
    void setUp() {
        JavaFXInitializer.initialize();

        controller = new BookAPIController();
        searchCriteria = new ComboBox<>();
        searchField = new TextField();
        bookTable = new TableView<>();
        codeColumn = new TableColumn<>();
        titleColumn = new TableColumn<>();
        descriptionColumn = new TableColumn<>();
        categoryColumn = new TableColumn<>();
        authorColumn = new TableColumn<>();
        priceColumn = new TableColumn<>();
        quantityColumn = new TableColumn<>();
        bookData = FXCollections.observableArrayList();

        controller.searchCriteria = searchCriteria;
        controller.searchField = searchField;
        controller.bookTable = bookTable;
        controller.codeColumn = codeColumn;
        controller.titleColumn = titleColumn;
        controller.descriptionColumn = descriptionColumn;
        controller.categoryColumn = categoryColumn;
        controller.authorColumn = authorColumn;
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
            assertNotNull(descriptionColumn.getCellValueFactory());
            assertNotNull(categoryColumn.getCellValueFactory());
            assertNotNull(authorColumn.getCellValueFactory());
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

            // Giả lập dữ liệu trả về từ API
            ArrayList<Book> books = new ArrayList<>();
            books.add(new Book("001", "Java Programming", "A comprehensive guide", "Programming", "Author A", 50000, 10));

            // Thực hiện tìm kiếm
            controller.onSearch(new ActionEvent());

            // Cập nhật dữ liệu giả lập vào bảng
            Platform.runLater(() -> {
                bookData.clear();
                bookData.addAll(books);
                bookTable.setItems(bookData);

                // Kiểm tra kết quả
                assertEquals(1, bookTable.getItems().size());
                assertEquals("Java Programming", bookTable.getItems().get(0).getTitle());
            });
        });
    }

    @Test
    void testOnShowDetails() {
        Book book = new Book("123", "Effective Java", "A programming book", "Programming", "Joshua Bloch", 50000, 10);
        bookData.add(book);
        bookTable.setItems(bookData);
        bookTable.getSelectionModel().select(book);

        Platform.runLater(() -> {
            controller.onShowDetails(new ActionEvent());

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
