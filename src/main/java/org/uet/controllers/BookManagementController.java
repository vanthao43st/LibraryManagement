package org.uet.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.uet.database.dao.BookDao;
import org.uet.entity.Book;
import org.uet.entity.Document;

import java.io.IOException;
import java.util.List;

public class BookManagementController {

    @FXML
    private TextField bookCodeField, bookTitleField,
            bookCategoryField, bookAuthorField,
            bookPriceField, bookQuantityField,
            bookDescriptionField, searchField;

    @FXML
    private ComboBox<String> searchCriteria;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> codeColumn, titleColumn, categoryColumn, authorColumn, descriptionColumn;

    @FXML
    private TableColumn<Book, Double> priceColumn;

    @FXML
    private TableColumn<Book, Integer> quantityColumn;

    private final ObservableList<Book> bookData = FXCollections.observableArrayList();

    private Book selectedBook;

    private final BookDao bookDao = new BookDao();

    @FXML
    public void initialize() {
        // Initialize columns
        codeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCode()));
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        categoryColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCategory()));
        authorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthor()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrice()));
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));

        loadDocument();

        bookTable.setOnMouseClicked(this::onTableClick);
    }

    private void loadDocument() {
        List<Book> books = bookDao.getAllBook();
        bookData.setAll(books);
        bookTable.setItems(bookData);
    }

    @FXML
    private void onTableClick(MouseEvent event) {
        selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            bookCodeField.setText(selectedBook.getCode());
            bookTitleField.setText(selectedBook.getTitle());
            bookCategoryField.setText(selectedBook.getCategory());
            bookAuthorField.setText(selectedBook.getAuthor());
            bookPriceField.setText(String.valueOf(selectedBook.getPrice()));
            bookQuantityField.setText(String.valueOf(selectedBook.getQuantity()));
            bookDescriptionField.setText(selectedBook.getDescription());
        }
    }

    private void clearInputFields() {
        bookCodeField.clear();
        bookTitleField.clear();
        bookCategoryField.clear();
        bookAuthorField.clear();
        bookPriceField.clear();
        bookQuantityField.clear();
        bookDescriptionField.clear();
    }

    @FXML
    private void onAdd(ActionEvent event) {
        if (inCompleteInfo()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
            return;
        }

        String documentCode = bookCodeField.getText();
        if (isBookExisted(documentCode)) {
            showAlert("Lỗi", "Sách đã tồn tại! Vui lòng nhập mã sách khác.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Book book = getBook();
            bookDao.addBook(book);
            bookData.add(book);
            bookTable.refresh();
            bookTable.setItems(bookData);

            clearInputFields();
            showAlert("Thành công", "Sách được thêm thành công!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Lỗi", "NHập vào không hợp lệ. Hãy nhập các trường chính xác!", Alert.AlertType.ERROR);
        }
    }

    private Book getBook() {
        String code = bookCodeField.getText();
        String title = bookTitleField.getText();
        String category = bookCategoryField.getText();
        String author = bookAuthorField.getText();
        double price = Double.parseDouble(bookPriceField.getText());
        int quantity = Integer.parseInt(bookQuantityField.getText());
        String description = bookDescriptionField.getText();

        return new Book(code, title, description, category, author, price, quantity);
    }

    private boolean inCompleteInfo() {
        return bookCodeField.getText().isBlank() ||
                bookTitleField.getText().isBlank() ||
                bookCategoryField.getText().isBlank() ||
                bookAuthorField.getText().isBlank() ||
                bookQuantityField.getText().isBlank() ||
                bookPriceField.getText().isBlank();
    }

    private boolean isBookExisted(String bookCode) {
        for (Book book : bookData) {
            if (book.getCode().equals(bookCode)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void onEdit(ActionEvent event) {
        if (selectedBook == null) {
            showAlert("Cảnh báo", "Hãy chọn 1 sách để cập nhật!", Alert.AlertType.WARNING);
            return;
        }

        try {
            selectedBook.setCode(bookCodeField.getText());
            selectedBook.setTitle(bookTitleField.getText());
            selectedBook.setCategory(bookCategoryField.getText());
            selectedBook.setAuthor(bookAuthorField.getText());
            selectedBook.setPrice(Double.parseDouble(bookPriceField.getText()));
            selectedBook.setQuantity(Integer.parseInt(bookQuantityField.getText()));
            selectedBook.setDescription(bookDescriptionField.getText());

            bookDao.updateBook(selectedBook);
            bookTable.refresh();

            clearInputFields();
            showAlert("Thành công", "Cập nhật tài liệu thành công!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Lỗi", "Nhập vào không hợp lệ. Vui lòng điền các trường chính xác!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        if (selectedBook == null) {
            showAlert("Cảnh báo", "Hãy chọn 1 tài liệu để xoá!", Alert.AlertType.WARNING);
            return;
        }

        if (bookDao.isBorrowedBook(selectedBook.getCode())) {
            showAlert("Thông báo", "Tài liệu đang được mượn! Không thể xoá được!", Alert.AlertType.WARNING);
            return;
        }

        bookDao.deleteBook(selectedBook.getCode());
        bookData.remove(selectedBook);
        clearInputFields();
        showAlert("Thành công", "Xoá sách thành công!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String criteria = searchCriteria.getValue();
        String keyword = searchField.getText().trim();

        if (criteria == null || keyword.isEmpty()) {
            showAlert("Cảnh báo", "Vui lòng chọn 1 tiêu chí và nhập vào từ khoá!", Alert.AlertType.WARNING);
            return;
        }

        ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
        for (Book book : bookData) {
            switch (criteria) {
                case "Code":
                    if (book.getCode().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredBooks.add(book);
                    }
                    break;
                case "Title":
                    if (book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredBooks.add(book);
                    }
                    break;
                case "Category":
                    if (book.getCategory().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredBooks.add(book);
                    }
                    break;
                default:
                    showAlert("Lỗi", "Tiêu chí không hợp lệ.", Alert.AlertType.ERROR);
                    return;
            }
        }

        bookTable.setItems(filteredBooks);
        if (filteredBooks.isEmpty()) {
            showAlert("Thông báo", "Không có kết quả nào!", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void onShowDetail(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Thông báo", "Vui lòng chọn một cuốn sách!", Alert.AlertType.WARNING);
            return;
        }
        showBookDetails(selectedBook);
    }

    private void showBookDetails(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/BookDetailsDialog.fxml"));
            DialogPane dialogPane = loader.load();

            BookDetailsDialogController controller = loader.getController();
            controller.setBookDetails(book);

            // Tạo Stage cho DialogPane
            Stage stage = new Stage(StageStyle.UNDECORATED); // Stage không có thanh tiêu đề
            Scene scene = new Scene(dialogPane);
            stage.setScene(scene);

            // Kích hoạt tính năng kéo
            enableDragging(stage, dialogPane);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enableDragging(Stage stage, DialogPane dialogPane) {
        final BookManagementController.Delta dragDelta = new BookManagementController.Delta();

        // Ghi lại vị trí khi nhấn chuột
        dialogPane.setOnMousePressed(event -> {
            dragDelta.x = stage.getX() - event.getScreenX();
            dragDelta.y = stage.getY() - event.getScreenY();
        });

        // Cập nhật vị trí khi kéo chuột
        dialogPane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + dragDelta.x);
            stage.setY(event.getScreenY() + dragDelta.y);
        });
    }

    // Class để lưu vị trí chuột
    private static class Delta {
        double x, y;
    }
}