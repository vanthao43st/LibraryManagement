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
    protected ComboBox<String> searchCriteria, documentTypeField;

    @FXML
    protected TextField searchField, documentCodeField, quantityField;

    @FXML
    protected TableView<Library> libraryTable;

    @FXML
    protected TableColumn<Library, String> documentCodeColumn, titleColumn, descriptionColumn, borrowDateColumn, dueDateColumn, statusColumn, documentTypeColumn;

    @FXML
    protected TableColumn<Library, Integer> quantityColumn;

    protected static final LibraryDao libraryDao = new LibraryDao();

    protected final ObservableList<Library> libraryData = FXCollections.observableArrayList();

    Library selectedLibrary;

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
    protected void onTableClick(MouseEvent event) {
        selectedLibrary = libraryTable.getSelectionModel().getSelectedItem();
        if (selectedLibrary != null) {
            documentCodeField.setText(selectedLibrary.getDocumentCode());
            quantityField.setText(String.valueOf(selectedLibrary.getQuantity()));
            documentTypeField.setValue(selectedLibrary.getDocumentType());
        }
    }

    protected void clearForm() {
        documentCodeField.clear();
        quantityField.clear();
        documentTypeField.setValue(null);
    }

    protected void loadLibraryData() {
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
    protected void onBorrow(ActionEvent event) {
        try {
            if (incompleteInfo()) {
                showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
                return;
            }

            String userId = SessionManager.getInstance().getCurrentUser().getId();
            String documentCode = documentCodeField.getText();
            String type = documentTypeField.getValue();
            int quantity;

            // Kiểm tra số lượng nhập
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

            // Chạy các bước kiểm tra theo thứ tự
            CompletableFuture<Void> checkFuture = CompletableFuture.runAsync(() -> {
                //Kiểm tra tài liệu tồn tại
                boolean isDocumentExisted = libraryDao.isDocumentCodeExistedAsync(documentCode).join();
                if (!isDocumentExisted) {
                    Platform.runLater(() -> showAlert("Lỗi", "Tài liệu không tồn tại! Vui lòng nhập mã tài liệu hợp lệ.", Alert.AlertType.WARNING));
                    throw new RuntimeException("Tài liệu không tồn tại");
                }
            }).thenRun(() -> {
                //Kiểm tra loại tài liệu
                boolean isBook = libraryDao.checkIfBookAsync(documentCode).join();
                if ((isBook && !type.equals("Sách")) || (!isBook && !type.equals("Luận văn"))) {
                    Platform.runLater(() -> showAlert("Lỗi", "Loại tài liệu không hợp lệ! Hãy xem lại mã hoặc loại tài liệu!", Alert.AlertType.WARNING));
                    throw new RuntimeException("Loại tài liệu không hợp lệ");
                }
            }).thenRun(() -> {
                //Mượn tài liệu
                boolean success = libraryDao.borrowDocumentAsync(userId, documentCode, type, quantity).join();
                if (success) {
                    Platform.runLater(() -> {
                        loadLibraryData();
                        showAlert("Thành công", "Mượn tài liệu thành công!", Alert.AlertType.INFORMATION);
                        clearForm();
                    });
                } else {
                    Platform.runLater(() -> showAlert("Lỗi", "Lỗi mượn tài liệu. Kiểm tra số lượng có sẵn!", Alert.AlertType.ERROR));
                    throw new RuntimeException("Lỗi mượn tài liệu");
                }
            });

            // Xử lý lỗi
            checkFuture.exceptionally(e -> {
                System.out.println("Đã xảy ra lỗi trong quá trình mượn tài liệu: " + e.getMessage());
                return null;
            });

        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onReturn(ActionEvent event) {
        try {
            if (selectedLibrary == null) {
                showAlert("Lỗi", "Vui lòng chọn tài liệu muốn trả!", Alert.AlertType.WARNING);
                return;
            }

            String userId = SessionManager.getInstance().getCurrentUser().getId();
            String documentCode = documentCodeField.getText();
            String returnDate = java.time.LocalDate.now().toString();
            String type = documentTypeField.getValue();

            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    showAlert("Lỗi", "Số lượng trả phải lớn hơn 0!", Alert.AlertType.ERROR);
                    return;
                } else if (quantity > selectedLibrary.getQuantity()) {
                    showAlert("Lỗi", "Số lượng trả vượt quá số lượng mượn!", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi", "Số lượng trả phải là một số nguyên!", Alert.AlertType.ERROR);
                return;
            }

            CompletableFuture<Void> checkFuture = CompletableFuture.runAsync(() -> {
                //Kiểm tra tài liệu tồn tại
                boolean isDocumentExisted = libraryDao.isDocumentCodeExistedAsync(documentCode).join();
                if (!isDocumentExisted) {
                    Platform.runLater(() -> showAlert("Lỗi", "Tài liệu không tồn tại! Vui lòng nhập mã tài liệu hợp lệ.", Alert.AlertType.WARNING));
                    throw new RuntimeException("Tài liệu không tồn tại");
                }
            }).thenRun(() -> {
                //Kiểm tra loại tài liệu
                boolean isBook = libraryDao.checkIfBookAsync(documentCode).join();
                if ((isBook && !type.equals("Sách")) || (!isBook && !type.equals("Luận văn"))) {
                    Platform.runLater(() -> showAlert("Lỗi", "Loại tài liệu không hợp lệ! Hãy xem lại mã hoặc loại tài liệu!", Alert.AlertType.WARNING));
                    throw new RuntimeException("Loại tài liệu không hợp lệ");
                }
            }).thenRun(() -> {
                // Lấy library_id
                Integer libraryId = libraryDao.getLibraryIdAsync(userId, documentCode, selectedLibrary.getQuantity()).join();
                if (libraryId == null) {
                    Platform.runLater(() -> showAlert("Lỗi", "Không tìm thấy bản ghi trong thư viện!", Alert.AlertType.WARNING));
                    throw new RuntimeException("Không tìm thấy bản ghi trong thư viện");
                }

                // Tiến hành trả tài liệu
                boolean success = libraryDao.returnDocumentAsync(userId, documentCode, quantity, returnDate, libraryId).join();
                if (!success) {
                    Platform.runLater(() -> showAlert("Lỗi", "Lỗi trả tài liệu!", Alert.AlertType.ERROR));
                    throw new RuntimeException("Lỗi trả tài liệu");
                }

                Platform.runLater(() -> {
                    loadLibraryData();
                    showAlert("Thành công", "Trả tài liệu thành công!", Alert.AlertType.INFORMATION);
                    clearForm();
                });
            });

            // Xử lý lỗi
            checkFuture.exceptionally(e -> {
                System.out.println("Lỗi khi trả tài liệu: " + e.getMessage());
                return null;
            });

        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onSearch(ActionEvent event) {
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

    protected boolean incompleteInfo() {
        return documentCodeField.getText().isBlank()
                || quantityField.getText().isBlank()
                || documentTypeField.getValue() == null;
    }

    protected void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}