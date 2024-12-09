package org.uet.controllers.admin;

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

import java.io.IOException;
import java.util.List;

public class LibraryManagementController {

    @FXML
    private ComboBox<String> searchCriteria, documentTypeField;

    @FXML
    private TextField searchField, userIdField, documentCodeField, quantityField;

    @FXML
    private Button searchButton, borrowButton, returnButton, deleteButton, statisticButton;

    @FXML
    private TableView<Library> libraryTable;

    @FXML
    private TableColumn<Library, String> userIdColumn, documentCodeColumn, borrowDateColumn, dueDateColumn, returnDateColumn, statusColumn, documentTypeColumn;

    @FXML
    private TableColumn<Library, Integer> quantityColumn, lateDaysColumn;

    @FXML
    private TableColumn<Library, Double> fineColumn;

    private static final LibraryDao libraryDao = new LibraryDao();

    private final ObservableList<Library> libraryData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize search criteria
        searchCriteria.setItems(FXCollections.observableArrayList("User ID", "Document Code", "Status"));
        documentTypeField.setItems(FXCollections.observableArrayList("Sách", "Luận văn"));

        // Link table columns to Library fields
        userIdColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUserId()));
        documentCodeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDocumentCode()));
        documentTypeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDocumentType()));
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));
        borrowDateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBorrowDate()));
        dueDateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDueDate()));
        returnDateColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getReturnDate()));
        statusColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus()));
        lateDaysColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLateDays()));
        fineColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFine()));

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
            userIdField.setText(selectedLibrary.getUserId());
            documentCodeField.setText(selectedLibrary.getDocumentCode());
            quantityField.setText(String.valueOf(selectedLibrary.getQuantity()));
            documentTypeField.setValue(selectedLibrary.getDocumentType());
        }
    }

    private void clearForm() {
        userIdField.clear();
        documentCodeField.clear();
        quantityField.clear();
        documentTypeField.setValue(null);
    }

    private void loadLibraryData() {
        List<Library> records = libraryDao.getAllLibraryRecords();
        libraryData.setAll(records);
    }

    @FXML
    private void onBorrow(ActionEvent event) {
        try {
            if (incompleteInfo()) {
                showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
                return;
            }

            String userId = userIdField.getText();
            String documentCode = documentCodeField.getText();
            int quantity;
            String type = documentTypeField.getValue();

            // Kiểm tra sự tồn tại của user_id
            if (!libraryDao.isUserExisted(userId)) {
                showAlert("Lỗi", "User không tồn tại! Vui lòng nhập User ID hợp lệ.", Alert.AlertType.WARNING);
                return;
            }

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

//            if (isLibraryRecordExisted(userId, documentCode)) {
//                showAlert("Lỗi", "Bản ghi đã tồn tại! Vui lòng nhập bản ghi khác.", Alert.AlertType.WARNING);
//                return;
//            }

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

            boolean success = libraryDao.borrowDocument(userId, documentCode, type, quantity);
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

    // Kiểm tra xem bản ghi có tồn tại trong bảng hay chưa thông qua user_id và document_code
//    private boolean isLibraryRecordExisted(String userId, String documentCode) {
//        for (Library library : libraryData) {
//            if (library.getUserId().equals(userId)
//                    && library.getDocumentCode().equals(documentCode)) {
//                return true;
//            }
//        }
//        return false;
//    }

    @FXML
    private void onReturn(ActionEvent event) {
        try {
            if (incompleteInfo()) {
                showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
                return;
            }

            String userId = userIdField.getText();
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

            boolean success = libraryDao.returnDocument(userId, documentCode, quantity, returnDate);
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

    @FXML
    private void onDelete(ActionEvent event) {
        libraryDao.deleteLibraryRecord();
        loadLibraryData();
        clearForm();
        showAlert("Thành công", "Đã xoá các bản ghi không cần thiết!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String criteria = searchCriteria.getValue();
        String keyword = searchField.getText().trim();

        if (criteria == null || keyword.isEmpty()) {
            showAlert("Lỗi", "Hãy chọn 1 trường tìm kiếm và nhập từ khoá cần tìm!", Alert.AlertType.WARNING);
            return;
        }

        ObservableList<Library> filteredData = libraryData.filtered(record -> switch (criteria) {
            case "User ID" -> record.getUserId().toLowerCase().contains(keyword.toLowerCase());
            case "Document Code" -> record.getDocumentCode().toLowerCase().contains(keyword.toLowerCase());
            case "Status" -> record.getStatus().toLowerCase().contains(keyword.toLowerCase());
            default -> false;
        });

        libraryTable.setItems(filteredData);
    }

    private boolean incompleteInfo() {
        return userIdField.getText().isBlank()
                || documentCodeField.getText().isBlank()
                || quantityField.getText().isBlank();
    }

    @FXML
    private void onStatistic(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Admin/StatisticManagement.fxml"));
            DialogPane dialogPane = loader.load();

            StatisticManagementController controller = loader.getController();
            controller.setStatisticDetails();

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
        final LibraryManagementController.Delta dragDelta = new LibraryManagementController.Delta();

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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
