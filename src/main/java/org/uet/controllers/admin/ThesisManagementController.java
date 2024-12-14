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
import org.uet.database.dao.ThesisDao;
import org.uet.entity.Thesis;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ThesisManagementController {

    @FXML
    protected TextField thesisCodeField, thesisTitleField, thesisMajorField, thesisAuthorField,
            thesisSupervisorField, thesisUniversityField, thesisDegreeField,
            thesisSubmissionYearField, thesisDescriptionField, searchField, thesisQuantityField;

    @FXML
    protected ComboBox<String> searchCriteria;

    @FXML
    protected TableView<Thesis> thesisTable;

    @FXML
    protected TableColumn<Thesis, String> codeColumn, titleColumn,
            majorColumn, authorColumn, supervisorColumn, universityColumn, degreeColumn, descriptionColumn;

    @FXML
    protected TableColumn<Thesis, Integer> submissionYearColumn, quantityColumn;

    protected final ObservableList<Thesis> thesisData = FXCollections.observableArrayList();

    protected Thesis selectedThesis;

    protected final ThesisDao thesisDao = new ThesisDao();

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCode()));
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        majorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMajor()));
        authorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthor()));
        supervisorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSupervisor()));
        universityColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUniversity()));
        degreeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDegree()));
        submissionYearColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getSubmissionYear()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
        quantityColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantity()));

        loadTheses();

        thesisTable.setOnMouseClicked(this::onTableClick);
    }

    protected void loadTheses() {
        thesisDao.getAllThesisAsync().thenAccept(theses -> {
            Platform.runLater(() -> {
                thesisData.setAll(theses);
                thesisTable.setItems(thesisData);
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> showAlert("Lỗi", "Không thể tải dữ liệu luận văn: " + e.getMessage(), Alert.AlertType.ERROR));
            return null;
        });
    }

    @FXML
    protected void onTableClick(MouseEvent event) {
        selectedThesis = thesisTable.getSelectionModel().getSelectedItem();
        if (selectedThesis != null) {
            thesisCodeField.setEditable(false);
            thesisCodeField.setText(selectedThesis.getCode());
            thesisTitleField.setText(selectedThesis.getTitle());
            thesisMajorField.setText(selectedThesis.getMajor());
            thesisAuthorField.setText(selectedThesis.getAuthor());
            thesisSupervisorField.setText(selectedThesis.getSupervisor());
            thesisUniversityField.setText(selectedThesis.getUniversity());
            thesisDegreeField.setText(selectedThesis.getDegree());
            thesisSubmissionYearField.setText(String.valueOf(selectedThesis.getSubmissionYear()));
            thesisDescriptionField.setText(selectedThesis.getDescription());
            thesisQuantityField.setText(String.valueOf(selectedThesis.getQuantity()));
        }
    }

    protected void clearInputFields() {
        thesisCodeField.clear();
        thesisTitleField.clear();
        thesisMajorField.clear();
        thesisAuthorField.clear();
        thesisSupervisorField.clear();
        thesisUniversityField.clear();
        thesisDegreeField.clear();
        thesisSubmissionYearField.clear();
        thesisDescriptionField.clear();
        thesisQuantityField.clear();
    }

    @FXML
    protected void onAdd(ActionEvent event) {
        if (inCompleteInfo()) {
            showAlert("Lỗi", "Hãy điền vào đầy đủ các trường!", Alert.AlertType.WARNING);
            return;
        }

        String thesisCode = thesisCodeField.getText();
        if (isThesisExisted(thesisCode)) {
            showAlert("Lỗi", "Luận văn đã tồn tại. Hãy sử dụng mã luận văn khác!", Alert.AlertType.WARNING);
            return;
        }

        try {
            Thesis thesis = getThesis();
            thesisDao.addThesisAsync(thesis).thenRun(() -> {
                Platform.runLater(() -> {
                    thesisData.add(thesis);
                    thesisTable.refresh();
                    clearInputFields();
                    showAlert("Thành công", "Đã thêm luận văn thành công!", Alert.AlertType.INFORMATION);
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> showAlert("Lỗi", "Đã xảy ra lỗi khi thêm luận văn: " + e.getMessage(), Alert.AlertType.ERROR));
                return null;
            });
        } catch (Exception e) {
            showAlert("Lỗi", "Nhập vào không hợp lệ! Hãy kiểm tra các dữ liệu nhập vào!", Alert.AlertType.ERROR);
        }
    }

    protected Thesis getThesis() {
        String code = thesisCodeField.getText();
        String title = thesisTitleField.getText();
        String major = thesisMajorField.getText();
        String author = thesisAuthorField.getText();
        String supervisor = thesisSupervisorField.getText();
        String university = thesisUniversityField.getText();
        String degree = thesisDegreeField.getText();
        int submissionYear = Integer.parseInt(thesisSubmissionYearField.getText());
        String description = thesisDescriptionField.getText();
        int quantity = Integer.parseInt(thesisQuantityField.getText());

        return new Thesis(author, code, description, title, degree, major,
                quantity, submissionYear, supervisor, university);
    }

    protected boolean inCompleteInfo() {
        return thesisCodeField.getText().isBlank() ||
                thesisTitleField.getText().isBlank() ||
                thesisMajorField.getText().isBlank() ||
                thesisAuthorField.getText().isBlank() ||
                thesisSupervisorField.getText().isBlank() ||
                thesisUniversityField.getText().isBlank() ||
                thesisDegreeField.getText().isBlank() ||
                thesisSubmissionYearField.getText().isBlank() ||
                thesisDescriptionField.getText().isBlank() ||
                thesisQuantityField.getText().isBlank();
    }

    protected boolean isThesisExisted(String thesisCode) {
        for (Thesis thesis : thesisData) {
            if (thesis.getCode().equals(thesisCode)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    protected void onEdit(ActionEvent event) {
        if (selectedThesis == null) {
            showAlert("Cảnh báo", "Hãy chọn 1 luận văn để cập nhật!", Alert.AlertType.WARNING);
            return;
        }

        try {
            selectedThesis.setCode(thesisCodeField.getText());
            selectedThesis.setTitle(thesisTitleField.getText());
            selectedThesis.setMajor(thesisMajorField.getText());
            selectedThesis.setAuthor(thesisAuthorField.getText());
            selectedThesis.setSupervisor(thesisSupervisorField.getText());
            selectedThesis.setUniversity(thesisUniversityField.getText());
            selectedThesis.setDegree(thesisDegreeField.getText());
            selectedThesis.setSubmissionYear(Integer.parseInt(thesisSubmissionYearField.getText()));
            selectedThesis.setDescription(thesisDescriptionField.getText());
            selectedThesis.setQuantity(Integer.parseInt(thesisQuantityField.getText()));

            thesisDao.updateThesisAsync(selectedThesis).thenRun(() -> {
                Platform.runLater(() -> {
                    thesisTable.refresh();
                    clearInputFields();
                    showAlert("Thành công", "Cập nhật luận văn thành công!", Alert.AlertType.INFORMATION);
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> showAlert("Lỗi", "Đã xảy ra lỗi khi cập nhật luận văn: " + e.getMessage(), Alert.AlertType.ERROR));
                return null;
            });
        } catch (Exception e) {
            showAlert("Lỗi", "Nhập vào không hợp lệ! Hãy kiểm tra các dữ liệu nhập vào!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onDelete(ActionEvent event) {
        if (selectedThesis == null) {
            showAlert("Cảnh báo", "Hãy chọn luận văn để xoá!", Alert.AlertType.WARNING);
            return;
        }

        // Hiển thị hộp thoại xác nhận
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Xác nhận xoá");
        confirmationAlert.setHeaderText("Bạn có chắc chắn muốn xoá luận văn này?");
        confirmationAlert.setContentText("Hành động này không thể hoàn tác.");

        // Chờ người dùng xác nhận
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Kiểm tra xem tài liệu có đang được mượn không (bất đồng bộ)
            thesisDao.isBorrowedThesisAsync(selectedThesis.getCode()).thenAccept(isBorrowed -> {
                Platform.runLater(() -> {
                    if (isBorrowed) {
                        showAlert("Thông báo", "Tài liệu đang được mượn! Không thể xoá được!", Alert.AlertType.WARNING);
                    } else {
                        // Xoá luận văn khỏi cơ sở dữ liệu (bất đồng bộ)
                        thesisDao.deleteThesisAsync(selectedThesis.getCode()).thenRun(() -> {
                            Platform.runLater(() -> {
                                thesisData.remove(selectedThesis);
                                thesisCodeField.setEditable(true);
                                clearInputFields();
                                showAlert("Thành công", "Xoá luận văn thành công!", Alert.AlertType.INFORMATION);
                            });
                        }).exceptionally(e -> {
                            Platform.runLater(() -> {
                                showAlert("Lỗi", "Không thể xoá sách!", Alert.AlertType.ERROR);
                                System.out.println("Lỗi: " + e.getMessage());
                            });
                            return null;
                        });
                    }
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> {
                    showAlert("Lỗi", "Không thể kiểm tra trạng thái mượn luận văn: " + e.getMessage(), Alert.AlertType.ERROR);
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
            showAlert("Cảnh báo", "Vui lòng chọn 1 tiêu chí và nhập vào từ khoá!", Alert.AlertType.WARNING);
            return;
        }

        ObservableList<Thesis> filteredThesis = FXCollections.observableArrayList();
        for (Thesis thesis : thesisData) {
            switch (criteria) {
                case "Code":
                    if (thesis.getCode().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredThesis.add(thesis);
                    }
                    break;
                case "Title":
                    if (thesis.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredThesis.add(thesis);
                    }
                    break;
                case "Major":
                    if (thesis.getMajor().toLowerCase().contains(keyword.toLowerCase())) {
                        filteredThesis.add(thesis);
                    }
                    break;
                default:
                    showAlert("Lỗi", "Tiêu chí không hợp lệ.", Alert.AlertType.ERROR);
                    return;
            }
        }

        thesisTable.setItems(filteredThesis);
        if (filteredThesis.isEmpty()) {
            showAlert("Thông báo", "Không có kết quả nào!", Alert.AlertType.INFORMATION);
        }
    }

    protected void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void onShowDetail(ActionEvent event) {
        Thesis selectedThesis = thesisTable.getSelectionModel().getSelectedItem();
        if (selectedThesis == null) {
            showAlert("Thông báo", "Vui lòng chọn một cuốn sách!", Alert.AlertType.WARNING);
            return;
        }
        showThesisDetails(selectedThesis);
    }

    protected void showThesisDetails(Thesis thesis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Admin/ThesisDetailsDialog.fxml"));
            DialogPane dialogPane = loader.load();

            ThesisDetailsDialogController controller = loader.getController();
            controller.setThesisDetails(thesis);

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

    protected void enableDragging(Stage stage, DialogPane dialogPane) {
        final ThesisManagementController.Delta dragDelta = new ThesisManagementController.Delta();

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