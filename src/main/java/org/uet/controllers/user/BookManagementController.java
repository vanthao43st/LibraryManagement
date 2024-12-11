package org.uet.controllers.user;

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

import java.io.IOException;
import java.util.List;

public class BookManagementController {

    @FXML
    private TextField searchField;

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
    }

    private void loadDocument() {
        List<Book> books = bookDao.getAllBook();
        bookData.setAll(books);
        bookTable.setItems(bookData);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/User/BookDetailsDialog.fxml"));
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
        final Delta dragDelta = new Delta();

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