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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.uet.Service.GoogleBooksAPI;
import org.uet.database.dao.DocumentDao;
import org.uet.entity.Document;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BookAPIController {

    @FXML
    private ComboBox<String> searchCriteria;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton, addButton, detailsButton;

    @FXML
    private TableView<Document> documentTable;

    @FXML
    private TableColumn<Document, String> codeColumn, titleColumn, descriptionColumn, categoryColumn, authorColumn;

    @FXML
    private TableColumn<Document, Double> priceColumn;

    @FXML
    private TableColumn<Document, Integer> quantityColumn;

    private final ObservableList<Document> documentData = FXCollections.observableArrayList();

    private static final DocumentDao documentDao = new DocumentDao();

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCode()));
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
        categoryColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCategory()));
        authorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthor()));
        priceColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrice()));
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String criteria = searchCriteria.getValue();
        String keyword = searchField.getText().trim();

        if (criteria == null || keyword.isBlank()) {
            showAlert("Lỗi", "Vui lòng chọn tiêu chí và nhập từ khóa tìm kiếm!", Alert.AlertType.WARNING);
            return;
        }

        documentData.clear();

        //Search theo tieu chi
        List<Document> documents;
        try {
            switch (criteria) {
                case "ISBN":
                    documents = GoogleBooksAPI.searchBookByISBN(keyword);
                    break;
                case "Title":
                    documents = GoogleBooksAPI.searchBookByTitle(keyword);
                    break;
                default:
                    showAlert("Lỗi", "Tiêu chí không hợp lệ!", Alert.AlertType.ERROR);
                    return;
            }

            if (documents == null || documents.isEmpty()) {
                showAlert("Thông báo", "Không tìm thấy kết quả nào phù hợp!", Alert.AlertType.INFORMATION);
                return;
            }

            // Thêm dữ liệu vào danh sách
            documentData.addAll(documents);
            documentTable.setItems(documentData);

        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi khi tìm kiếm: " + e.getMessage(), Alert.AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onAdd(ActionEvent event) throws SQLException {
        Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument == null) {
            showAlert("Lỗi", "Vui lòng chọn một cuốn sách để thêm!", AlertType.WARNING);
            return;
        }

        saveToDatabase(selectedDocument);
        showAlert("Thành công", "Sách đã được thêm vào cơ sở dữ liệu!", AlertType.INFORMATION);
    }

    private void saveToDatabase(Document document) throws SQLException {
        documentDao.addDocument(document);
        System.out.println("Sách được lưu vào database: " + document);
    }

    @FXML
    private void onShowDetails(ActionEvent event) {
        Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument == null) {
            showAlert("Thông báo", "Vui lòng chọn một cuốn sách!", AlertType.WARNING);
            return;
        }
        showDocumentDetails(selectedDocument);
    }

    private void showDocumentDetails(Document document) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/BookDetailsDialog.fxml"));
            DialogPane dialogPane = loader.load();

            BookDetailsDialogController controller = loader.getController();
            controller.setBookDetails(document);

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
