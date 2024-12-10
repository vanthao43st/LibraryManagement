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
import org.uet.database.dao.LibraryDao;
import org.uet.entity.Library;
import org.uet.entity.SessionManager;

import java.io.IOException;
import java.util.List;

public class LibraryManagementController {

    @FXML
    private ComboBox<String> searchCriteria, documentTypeField;

    @FXML
    private TextField searchField, documentCodeField, quantityField;

    @FXML
    private TableView<Library> libraryTable;

    @FXML
    private TableColumn<Library, String> documentCodeColumn, titleColumn, descriptionColumn, borrowDateColumn, dueDateColumn, statusColumn, documentTypeColumn;

    @FXML
    private TableColumn<Library, Integer> quantityColumn;

    private static final LibraryDao libraryDao = new LibraryDao();

    private final ObservableList<Library> libraryData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize search criteria
        searchCriteria.setItems(FXCollections.observableArrayList("Document Code", "Type", "Status"));
        documentTypeField.setItems(FXCollections.observableArrayList("Sách", "Luận văn"));

        // Link table columns to Library fields
        documentCodeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDocumentCode()));
        documentTypeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDocumentType()));
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));
        borrowDateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBorrowDate()));
        dueDateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDueDate()));
        statusColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus()));

        // Assign data to the table
        libraryTable.setItems(libraryData);

        // Add click event to table rows
        libraryTable.setOnMouseClicked(this::onTableClick);

        // Load initial data
        loadLibraryData();
    }

    @FXML
    private void onTableClick(MouseEvent event) {
        Library selectedLibrary = libraryTable.getSelectionModel().getSelectedItem();
        if (selectedLibrary != null) {
            documentCodeField.setText(selectedLibrary.getDocumentCode());
            quantityField.setText(String.valueOf(selectedLibrary.getQuantity()));
            documentTypeField.setValue(selectedLibrary.getDocumentType());
        }
    }

    private void clearForm() {
        documentCodeField.clear();
        quantityField.clear();
        documentTypeField.setValue(null);
    }

    private void loadLibraryData() {
        List<Library> records = libraryDao.getAllLibraryRecordsForUser();
        libraryData.setAll(records);
    }

    @FXML
    private void onBorrow(ActionEvent event) {
        try {
            if (incompleteInfo()) {
                showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
                return;
            }

            String documentCode = documentCodeField.getText();
            int quantity;
            String type = documentTypeField.getValue();

            // Kiểm tra sự tồn tại của document_code
            if (!libraryDao.isCodeExisted(documentCode)) {
                showAlert("Lỗi", "Tài liệu không tồn tại! Vui lòng nhập mã tài liệu hợp lệ.", Alert.AlertType.WARNING);
                return;
            }

            //Check
            if ((libraryDao.checkIfBook(documentCode) && !type.equals("Sách"))
                    || !libraryDao.checkIfBook(documentCode) && !type.equals("Luận văn")) {
                showAlert("Lỗi", "Loại tài liệu không hợp lệ! Hãy xem lại mã tài liệu!", Alert.AlertType.WARNING);
                return;
            }

            try {
                quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    showAlert("Lỗi", "Số lượng mượn phải lớn hơn 0!", Alert.AlertType.WARNING);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Số lượng mượn phải là một số nguyên!", Alert.AlertType.ERROR);
                return;
            }

            boolean success = libraryDao.borrowDocument(SessionManager.getInstance().getCurrentUser().getId(),
                    documentCode, type, quantity);
            if (success) {
                loadLibraryData();
                showAlert("Thành công", "Mượn tài liệu thành công!", Alert.AlertType.INFORMATION);
                clearForm();
            } else {
                showAlert("Lỗi", "Lỗi mượn tài liệu. Kiểm tra số lượng có sẵn!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onReturn(ActionEvent event) {
        try {
            if (incompleteInfo()) {
                showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
                return;
            }

            String documentCode = documentCodeField.getText();
            int quantity;
            String returnDate = java.time.LocalDate.now().toString();

            try {
                quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    showAlert("Lỗi", "Số lượng mượn phải lớn hơn 0!", Alert.AlertType.WARNING);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Số lượng mượn phải là một số nguyên!", Alert.AlertType.ERROR);
                return;
            }

            boolean success = libraryDao.returnDocument(SessionManager.getInstance().getCurrentUser().getId(),
                    documentCode, quantity, returnDate);
            if (success) {
                loadLibraryData();
                showAlert("Thành công", "Trả tài liệu thành công!", Alert.AlertType.INFORMATION);
                clearForm();
            } else {
                showAlert("Lỗi", "Lỗi trả tài liệu!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

//    @FXML
//    private void onDelete(ActionEvent event) {
//        libraryDao.deleteLibraryRecord();
//        loadLibraryData();
//        clearForm();
//        showAlert("Thành công", "Đã xoá các bản ghi không cần thiết!", Alert.AlertType.INFORMATION);
//    }

    @FXML
    private void onSearch(ActionEvent event) {
        String criteria = searchCriteria.getValue();
        String keyword = searchField.getText().trim();

        if (criteria == null || keyword.isEmpty()) {
            showAlert("Lỗi", "Hãy chọn 1 trường tìm kiếm và nhập từ khoá cần tìm!", Alert.AlertType.WARNING);
            return;
        }

        ObservableList<Library> filteredData = libraryData.filtered(record -> switch (criteria) {
            case "Document Code" -> record.getDocumentCode().toLowerCase().contains(keyword.toLowerCase());
            case "Type" -> record.getDocumentType().toLowerCase().contains(keyword.toLowerCase());
            case "Status" -> record.getStatus().toLowerCase().contains(keyword.toLowerCase());
            default -> false;
        });

        libraryTable.setItems(filteredData);
    }

    private boolean incompleteInfo() {
        return documentCodeField.getText().isBlank()
                || quantityField.getText().isBlank()
                || documentTypeField.getValue() == null;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
