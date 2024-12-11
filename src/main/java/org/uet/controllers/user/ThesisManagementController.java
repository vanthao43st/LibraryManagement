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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.uet.database.dao.ThesisDao;
import org.uet.entity.Thesis;

import java.io.IOException;
import java.util.List;

public class ThesisManagementController {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> searchCriteria;

    @FXML
    private TableView<Thesis> thesisTable;

    @FXML
    private TableColumn<Thesis, String> codeColumn, titleColumn,
            majorColumn, authorColumn, supervisorColumn, universityColumn, degreeColumn, descriptionColumn;

    @FXML
    private TableColumn<Thesis, Integer> submissionYearColumn, quantityColumn;

    private final ObservableList<Thesis> thesisData = FXCollections.observableArrayList();

    private final ThesisDao thesisDao = new ThesisDao();

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
    }

    private void loadTheses() {
        List<Thesis> theses = thesisDao.getAllThesis();
        thesisData.setAll(theses);
        thesisTable.setItems(thesisData);
    }

    @FXML
    private void onSearch(ActionEvent event) {
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

    private void showAlert(String title, String content, Alert.AlertType type) {
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

    private void showThesisDetails(Thesis thesis) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/User/ThesisDetailsDialog.fxml"));
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