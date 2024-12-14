package org.uet.controllers.user;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.uet.database.dao.LibraryDao;
import org.uet.entity.Library;
import org.uet.entity.SessionManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        libraryDao.getAllLibraryRecordsForUserAsync().thenAccept(records -> {
            Platform.runLater(() -> {
                libraryData.setAll(records);
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> {
                System.out.println("Lỗi khi tải dữ liệu thư viện: " + e.getMessage());
            });
            return null;
        });
    }

    @FXML
    private void onBorrow(ActionEvent event) {
        try {
            if (incompleteInfo()) {
                showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
                return;
            }

            String documentCode = documentCodeField.getText();
            String type = documentTypeField.getValue();
            String userId = SessionManager.getInstance().getCurrentUser().getId();
            int quantity;

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

            // Kiểm tra tài liệu có tồn tại hay không
            libraryDao.isDocumentCodeExistedAsync(documentCode)
                    .thenCompose(isDocumentExisted -> {
                        if (!isDocumentExisted) {
                            Platform.runLater(() -> showAlert("Lỗi", "Tài liệu không tồn tại! Vui lòng nhập mã tài liệu hợp lệ.", Alert.AlertType.WARNING));
                            return CompletableFuture.completedFuture(false); // Dừng chuỗi xử lý
                        }
                        return libraryDao.checkIfBookAsync(documentCode);
                    })
                    .thenCompose(isBook -> {
                        if (!isBook && type.equals("Sách") || (isBook && !type.equals("Sách"))) {
                            Platform.runLater(() -> showAlert("Lỗi", "Loại tài liệu không hợp lệ! Hãy xem lại mã tài liệu!", Alert.AlertType.ERROR));
                            return CompletableFuture.completedFuture(false); // Dừng chuỗi xử lý
                        }
                        return libraryDao.borrowDocumentAsync(userId, documentCode, type, quantity);
                    })
                    .thenAccept(success -> {
                        if (success) {
                            Platform.runLater(() -> {
                                loadLibraryData();
                                showAlert("Thành công", "Mượn tài liệu thành công!", Alert.AlertType.INFORMATION);
                                clearForm();
                            });
                        } else {
                            Platform.runLater(() -> showAlert("Lỗi", "Lỗi mượn tài liệu. Kiểm tra số lượng có sẵn!", Alert.AlertType.ERROR));
                        }
                    })
                    .exceptionally(e -> {
                        Platform.runLater(() -> showAlert("Lỗi", "Đã xảy ra lỗi: " + e.getMessage(), Alert.AlertType.ERROR));
                        return null;
                    });

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
            String userId = SessionManager.getInstance().getCurrentUser().getId();
            String returnDate = java.time.LocalDate.now().toString();
            int quantity;

            try {
                quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    showAlert("Lỗi", "Số lượng trả phải lớn hơn 0!", Alert.AlertType.WARNING);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Số lượng trả phải là một số nguyên!", Alert.AlertType.ERROR);
                return;
            }

            libraryDao.getLibraryIdAsync(userId, documentCode, quantity)
                    .thenCompose(libraryId -> {
                        if (libraryId == null) {
                            Platform.runLater(() -> showAlert("Lỗi", "Không tìm thấy bản ghi trong thư viện!", Alert.AlertType.WARNING));
                            return CompletableFuture.completedFuture(false);
                        }
                        return libraryDao.returnDocumentAsync(userId, documentCode, quantity, returnDate, libraryId);
                    })
                    .thenAccept(success -> {
                        if (success) {
                            Platform.runLater(() -> {
                                loadLibraryData();
                                showAlert("Thành công", "Trả tài liệu thành công!", Alert.AlertType.INFORMATION);
                                clearForm();
                            });
                        } else {
                            Platform.runLater(() -> showAlert("Lỗi", "Lỗi trả tài liệu!", Alert.AlertType.ERROR));
                        }
                    })
                    .exceptionally(e -> {
                        Platform.runLater(() -> showAlert("Lỗi", "Đã xảy ra lỗi: " + e.getMessage(), Alert.AlertType.ERROR));
                        return null;
                    });
        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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