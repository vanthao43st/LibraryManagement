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
import org.uet.entity.Document;

import java.io.IOException;
import java.util.List;

public class BookManagementController {

    @FXML
    private TextField documentCodeField, documentTitleField,
            documentCategoryField, documentAuthorField,
            documentPriceField, documentQuantityField,
            documentDescriptionField, searchField;

    @FXML
    private ComboBox<String> searchCriteria;

    @FXML
    private TableView<Document> documentTable;

    @FXML
    private TableColumn<Document, String> codeColumn, titleColumn, categoryColumn, authorColumn, descriptionColumn;

    @FXML
    private TableColumn<Document, Double> priceColumn;

    @FXML
    private TableColumn<Document, Integer> quantityColumn;

    private final ObservableList<Document> documentData = FXCollections.observableArrayList();

    private Document selectedDocument;

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

        documentTable.setOnMouseClicked(this::onTableClick);
    }

    private void loadDocument() {
        List<Document> books = bookDao.getAllDocument();
        documentData.setAll(books);
        documentTable.setItems(documentData);
    }

    @FXML
    private void onTableClick(MouseEvent event) {
        selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            documentCodeField.setText(selectedDocument.getCode());
            documentTitleField.setText(selectedDocument.getTitle());
            documentCategoryField.setText(selectedDocument.getCategory());
            documentAuthorField.setText(selectedDocument.getAuthor());
            documentPriceField.setText(String.valueOf(selectedDocument.getPrice()));
            documentQuantityField.setText(String.valueOf(selectedDocument.getQuantity()));
            documentDescriptionField.setText(selectedDocument.getDescription());
        }
    }

    private void clearInputFields() {
        documentCodeField.clear();
        documentTitleField.clear();
        documentCategoryField.clear();
        documentAuthorField.clear();
        documentPriceField.clear();
        documentQuantityField.clear();
        documentDescriptionField.clear();
    }

    @FXML
    private void onAdd(ActionEvent event) {
        if (inCompleteInfo()) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
            return;
        }

        String documentCode = documentCodeField.getText();
        if (isDocumentExisted(documentCode)) {
            showAlert("Lỗi", "Tài liệu đã tồn tại! Vui lòng nhập mã tài liệu khác.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Document document = getDocument();
            bookDao.addDocument(document);
            documentData.add(document);
            documentTable.refresh();
            documentTable.setItems(documentData);

            clearInputFields();
            showAlert("Thành công", "Sách được thêm thành công!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Lỗi", "NHập vào không hợp lệ. Hãy nhập các trường chính xác!", Alert.AlertType.ERROR);
        }
    }

    private Document getDocument() {
        String code = documentCodeField.getText();
        String title = documentTitleField.getText();
        String category = documentCategoryField.getText();
        String author = documentAuthorField.getText();
        double price = Double.parseDouble(documentPriceField.getText());
        int quantity = Integer.parseInt(documentQuantityField.getText());
        String description = documentDescriptionField.getText();

        return new Document(code, title, description, category, author, price, quantity);
    }

    private boolean inCompleteInfo() {
        return documentCodeField.getText().isBlank() ||
                documentTitleField.getText().isBlank() ||
                documentCategoryField.getText().isBlank() ||
                documentAuthorField.getText().isBlank() ||
                documentQuantityField.getText().isBlank() ||
                documentPriceField.getText().isBlank();
    }

    private boolean isDocumentExisted(String documentCode) {
        for (Document document : documentData) {
            if (document.getCode().equals(documentCode)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void onEdit(ActionEvent event) {
        if (selectedDocument == null) {
            showAlert("Cảnh báo", "Hãy chọn 1 tài liệu để cập nhật!", Alert.AlertType.WARNING);
            return;
        }

        try {
            selectedDocument.setCode(documentCodeField.getText());
            selectedDocument.setTitle(documentTitleField.getText());
            selectedDocument.setCategory(documentCategoryField.getText());
            selectedDocument.setAuthor(documentAuthorField.getText());
            selectedDocument.setPrice(Double.parseDouble(documentPriceField.getText()));
            selectedDocument.setQuantity(Integer.parseInt(documentQuantityField.getText()));
            selectedDocument.setDescription(documentDescriptionField.getText());

            bookDao.updateDocument(selectedDocument);
            documentTable.refresh();

            clearInputFields();
            showAlert("Thành công", "Cập nhật tài liệu thành công!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Lỗi", "Nhập vào không hợp lệ. Vui lòng điền các trường chính xác!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        if (selectedDocument == null) {
            showAlert("Cảnh báo", "Hãy chọn 1 tài liệu để xoá!", Alert.AlertType.WARNING);
            return;
        }

        if (bookDao.isBorrowedBook(selectedDocument.getCode())) {
            showAlert("Thông báo", "Tài liệu đang được mượn! Không thể xoá được!", Alert.AlertType.WARNING);
            return;
        }

        bookDao.deleteDocument(selectedDocument.getCode());
        documentData.remove(selectedDocument);
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

        ObservableList<Document> filteredDocuments = FXCollections.observableArrayList();
        for (Document document : documentData) {
            switch (criteria) {
                case "Code":
                    if (document.getCode().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredDocuments.add(document);
                    }
                    break;
                case "Title":
                    if (document.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredDocuments.add(document);
                    }
                    break;
                case "Category":
                    if (document.getCategory().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredDocuments.add(document);
                    }
                    break;
                default:
                    showAlert("Lỗi", "Tiêu chí không hợp lệ.", Alert.AlertType.ERROR);
                    return;
            }
        }

        documentTable.setItems(filteredDocuments);
        if (filteredDocuments.isEmpty()) {
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
        Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument == null) {
            showAlert("Thông báo", "Vui lòng chọn một cuốn sách!", Alert.AlertType.WARNING);
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