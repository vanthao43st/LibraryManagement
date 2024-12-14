package org.uet.controllers.admin;

import javafx.application.Platform;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class LibraryManagementController {

    @FXML
    protected ComboBox<String> searchCriteria, documentTypeField;

    @FXML
    protected TextField searchField, userIdField, documentCodeField, quantityField;

    @FXML
    protected TableView<Library> libraryTable;

    @FXML
    protected TableColumn<Library, String> userIdColumn, documentCodeColumn, borrowDateColumn, dueDateColumn, returnDateColumn, statusColumn, documentTypeColumn;

    @FXML
    protected TableColumn<Library, Integer> quantityColumn, lateDaysColumn;

    @FXML
    protected TableColumn<Library, Double> fineColumn;

    protected static final LibraryDao libraryDao = new LibraryDao();

    protected final ObservableList<Library> libraryData = FXCollections.observableArrayList();

    Library selectedLibrary;

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
    protected void onTableClick(MouseEvent event) {
        selectedLibrary = libraryTable.getSelectionModel().getSelectedItem();
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
        libraryDao.getAllLibraryRecordsAsync().thenAccept(records -> {
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

            String userId = userIdField.getText();
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
                //Kiểm tra user tồn tại
                boolean isUserExisted = libraryDao.isUserExistedAsync(userId).join();
                if (!isUserExisted) {
                    Platform.runLater(() -> showAlert("Lỗi", "User không tồn tại! Vui lòng nhập User ID hợp lệ.", Alert.AlertType.WARNING));
                    throw new RuntimeException("User không tồn tại");
                }
            }).thenRun(() -> {
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

            String userId = userIdField.getText();
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
                //Kiểm tra user tồn tại
                boolean isUserExisted = libraryDao.isUserExistedAsync(userId).join();
                if (!isUserExisted) {
                    Platform.runLater(() -> showAlert("Lỗi", "User không tồn tại! Vui lòng nhập User ID hợp lệ.", Alert.AlertType.WARNING));
                    throw new RuntimeException("User không tồn tại");
                }
            }).thenRun(() -> {
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
    protected void onDelete(ActionEvent event) {
        // Hiển thị hộp thoại xác nhận
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Xác nhận xoá");
        confirmationAlert.setHeaderText("Bạn có chắc chắn muốn xoá các bản ghi này?");
        confirmationAlert.setContentText("Hành động này không thể hoàn tác.");

        // Chờ người dùng xác nhận
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            libraryDao.deleteLibraryRecordAsync()
                    .thenRun(() -> {
                        Platform.runLater(() -> {
                            loadLibraryData();
                            clearForm();
                            showAlert("Thành công", "Đã xoá các bản ghi không cần thiết!", Alert.AlertType.INFORMATION);
                        });
                    })
                    .exceptionally(e -> {
                        Platform.runLater(() -> {
                            showAlert("Lỗi", "Đã xảy ra lỗi khi xoá các bản ghi: " + e.getMessage(), Alert.AlertType.ERROR);
                        });
                        return null;
                    });
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
                || quantityField.getText().isBlank()
                || documentTypeField.getValue() == null;
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