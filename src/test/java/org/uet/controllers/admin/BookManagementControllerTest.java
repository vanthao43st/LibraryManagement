package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;
import org.uet.database.dao.BookDao;
import org.uet.entity.Book;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class BookManagementControllerTest {

    private BookManagementController controller;
    private TextField bookCodeField, bookTitleField, bookCategoryField, bookAuthorField, bookPriceField, bookQuantityField, bookDescriptionField, searchField;
    private ComboBox<String> searchCriteria;
    private TableView<Book> bookTable;
    private TableColumn<Book, String> codeColumn, titleColumn, categoryColumn, authorColumn, descriptionColumn;
    private TableColumn<Book, Double> priceColumn;
    private TableColumn<Book, Integer> quantityColumn;
    private ObservableList<Book> bookData;

    @BeforeEach
    void setUp() {
        // Khởi tạo JavaFX Toolkit
        JavaFXInitializer.initialize();

        controller = new BookManagementController();
        bookCodeField = new TextField();
        bookTitleField = new TextField();
        bookCategoryField = new TextField();
        bookAuthorField = new TextField();
        bookPriceField = new TextField();
        bookQuantityField = new TextField();
        bookDescriptionField = new TextField();
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

        controller.bookCodeField = bookCodeField;
        controller.bookTitleField = bookTitleField;
        controller.bookCategoryField = bookCategoryField;
        controller.bookAuthorField = bookAuthorField;
        controller.bookPriceField = bookPriceField;
        controller.bookQuantityField = bookQuantityField;
        controller.bookDescriptionField = bookDescriptionField;
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
        controller.bookData.addAll(bookData);
    }

    @Test
    void testInitialize() {
        controller.initialize();

        assertNotNull(codeColumn.getCellValueFactory());
        assertNotNull(titleColumn.getCellValueFactory());
        assertNotNull(categoryColumn.getCellValueFactory());
        assertNotNull(authorColumn.getCellValueFactory());
        assertNotNull(descriptionColumn.getCellValueFactory());
        assertNotNull(priceColumn.getCellValueFactory());
        assertNotNull(quantityColumn.getCellValueFactory());
    }

    @Test
    void testOnAdd() {
        bookCodeField.setText("001");
        bookTitleField.setText("Test Book");
        bookCategoryField.setText("Test Category");
        bookAuthorField.setText("Test Author");
        bookPriceField.setText("10.0");
        bookQuantityField.setText("5");
        bookDescriptionField.setText("Test Description");

        controller.onAdd(new ActionEvent());

        assertEquals(1, controller.bookData.size());
        assertEquals("001", controller.bookData.get(0).getCode());
    }

    @Test
    void testOnTableClick() {
        Book book = new Book("002", "Clicked Book", "Clicked Description", "Clicked Author", "Clicked Category", 20.0, 10);
        bookData.add(book);
        bookTable.setItems(bookData);

        MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null);

        bookTable.getSelectionModel().select(book);
        controller.onTableClick(mouseEvent);

        assertEquals("002", bookCodeField.getText());
        assertEquals("Clicked Book", bookTitleField.getText());
    }

    @Test
    void testOnEdit() {
        Book book = new Book("003", "Editable Book", "Editable Description", "Editable Author", "Editable Category", 30.0, 15);
        bookData.add(book);
        bookTable.setItems(bookData);
        bookTable.getSelectionModel().select(book);

        controller.selectedBook = book;

        bookTitleField.setText("Edited Book");
        bookCategoryField.setText("Edited Category");
        bookAuthorField.setText("Edited Author");
        bookPriceField.setText("35.0");
        bookQuantityField.setText("20");
        bookDescriptionField.setText("Edited Description");

        controller.onEdit(new ActionEvent());

        assertEquals("Edited Book", book.getTitle());
        assertEquals("Edited Category", book.getCategory());
    }

    @Test
    void testOnDelete() {
        Book book = new Book("004", "Deletable Book", "Deletable Description", "Deletable Author", "Deletable Category", 40.0, 20);
        bookData.add(book);
        bookTable.setItems(bookData);
        bookTable.getSelectionModel().select(book);

        controller.selectedBook = book;

        Platform.runLater(() -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setResult(ButtonType.OK);
        });

        controller.onDelete(new ActionEvent());

        assertFalse(controller.bookData.contains(book));
    }

    @Test
    void testOnSearch() {
        Book book1 = new Book("005", "Searchable Book 1", "Description 1", "Author 1", "Category 1", 50.0, 25);
        Book book2 = new Book("006", "Searchable Book 2", "Description 2", "Author 2", "Category 2", 60.0, 30);
        bookData.addAll(book1, book2);
        bookTable.setItems(bookData);

        searchCriteria.setValue("Title");
        searchField.setText("Searchable Book 1");

        controller.onSearch(new ActionEvent());

        assertEquals(1, controller.bookTable.getItems().size());
        assertEquals("005", controller.bookTable.getItems().get(0).getCode());
    }

    @Test
    void testOnShowDetail() {
        Book book = new Book("007", "Detail Book", "Detail Description", "Detail Author", "Detail Category", 70.0, 35);
        bookData.add(book);
        bookTable.setItems(bookData);
        bookTable.getSelectionModel().select(book);

        controller.onShowDetail(new ActionEvent());

        assertNotNull(book);
    }
}
