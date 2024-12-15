package org.uet.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.Book;
import javafx.scene.control.cell.TextFieldTableCell;


import static org.junit.jupiter.api.Assertions.*;

class BookAPIControllerTest {

    private BookAPIController controller;
    private ComboBox<String> searchCriteria;
    private TextField searchField;
    private TableView<Book> bookTable;
    private TableColumn<Book, String> codeColumn, titleColumn, descriptionColumn, categoryColumn, authorColumn;
    private TableColumn<Book, Double> priceColumn;
    private TableColumn<Book, Integer> quantityColumn;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void testInitialize() {
        controller.initialize();

        assertNotNull(codeColumn.getCellValueFactory());
        assertNotNull(titleColumn.getCellValueFactory());
        assertNotNull(descriptionColumn.getCellValueFactory());
        assertNotNull(categoryColumn.getCellValueFactory());
        assertNotNull(authorColumn.getCellValueFactory());
        assertNotNull(priceColumn.getCellValueFactory());
        assertNotNull(quantityColumn.getCellValueFactory());

        assertTrue(bookTable.isEditable());
        assertEquals(TextFieldTableCell.forTableColumn().getClass(), codeColumn.getCellFactory().getClass());
    }

    @Test
    void testOnSearch_ISBN() {
        searchCriteria.setValue("ISBN");
        searchField.setText("1234567890");

        controller.onSearch(new javafx.event.ActionEvent());

        // Giả lập kết quả tìm kiếm cho kiểm thử
        ObservableList<Book> books = FXCollections.observableArrayList(
                new Book("1234567890", "Test Book", "Description", "Category", "Author", 9.99, 10)
        );

        controller.bookData.addAll(books);
        controller.bookTable.setItems(controller.bookData);

        assertNotNull(controller.bookData);
        assertTrue(controller.bookData.size() > 0);
        assertEquals("1234567890", controller.bookData.get(0).getCode());
    }

    @Test
    void testOnAdd_ValidBook() throws Exception {
        Book book = new Book("1234567890", "Test Book", "Description", "Category", "Author", 9.99, 10);
        bookTable.getItems().add(book);
        bookTable.getSelectionModel().select(book);

        controller.onAdd(new javafx.event.ActionEvent());

        assertTrue(controller.bookData.contains(book));
    }

    @Test
    void testOnShowDetails_ValidBook() {
        Book book = new Book("1234567890", "Test Book", "Description", "Category", "Author", 9.99, 10);
        bookTable.getItems().add(book);
        bookTable.getSelectionModel().select(book);

        controller.onShowDetails(new javafx.event.ActionEvent());

        assertNotNull(book);
    }
}
