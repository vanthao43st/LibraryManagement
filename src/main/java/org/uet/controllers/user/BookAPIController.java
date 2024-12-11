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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.uet.database.dao.BookDao;
import org.uet.entity.Book;
import org.uet.service.GoogleBooksAPI;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BookAPIController {

    @FXML
    private ComboBox<String> searchCriteria;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, String> codeColumn, titleColumn, descriptionColumn, categoryColumn, authorColumn;

    @FXML
    private TableColumn<Book, Double> priceColumn;

    @FXML
    private TableColumn<Book, Integer> quantityColumn;

    private final ObservableList<Book> bookData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCode()));
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
        categoryColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCategory()));
        authorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthor()));
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrice()));
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));

        bookTable.setEditable(true);
        codeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String criteria = searchCriteria.getValue();
        String keyword = searchField.getText().trim();

        if (criteria == null || keyword.isBlank()) {
            showAlert("Lỗi", "Vui lòng chọn tiêu chí và nhập từ khóa tìm kiếm!", AlertType.WARNING);
            return;
        }

        bookData.clear();

        //Search theo tieu chi
        List<Book> books;
        try {
            switch (criteria) {
                case "ISBN":
                    books = GoogleBooksAPI.searchBookByISBN(keyword);
                    break;
                case "Title":
                    books = GoogleBooksAPI.searchBookByTitle(keyword);
                    break;
                default:
                    showAlert("Lỗi", "Tiêu chí không hợp lệ!", AlertType.ERROR);
                    return;
            }

            if (books == null || books.isEmpty()) {
                showAlert("Thông báo", "Không tìm thấy kết quả nào phù hợp!", AlertType.INFORMATION);
                return;
            }

            // Thêm dữ liệu vào danh sách
            bookData.addAll(books);
            bookTable.setItems(bookData);

        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi khi tìm kiếm: " + e.getMessage(), AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onShowDetails(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Thông báo", "Vui lòng chọn một cuốn sách!", AlertType.WARNING);
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

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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
