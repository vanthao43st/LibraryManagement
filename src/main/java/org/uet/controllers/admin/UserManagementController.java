package org.uet.controllers.admin;

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
import org.uet.database.dao.UserDao;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserManagementController {

    @FXML
    private ComboBox<String> searchCriteria, userGenderField, userMajorField;

    @FXML
    private TextField searchField, userIdField, userFullNameField, userClassField, userPhoneField, userEmailField;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> idColumn, nameColumn, genderColumn,
            classColumn, majorColumn, phoneColumn, emailColumn,
            userNameColumn, passwordColumn;

    private static UserDao userDao = new UserDao();

    // Dữ liệu mẫu
    private ObservableList<User> userData = FXCollections.observableArrayList();

    // Theo dõi người dùng được chọn
    private User selectedUser;

    @FXML
    public void initialize() {
        // Khởi tạo các giá trị cho ComboBox
        searchCriteria.setItems(FXCollections.observableArrayList("ID", "Tên", "Lớp"));
        userGenderField.setItems(FXCollections.observableArrayList("FEMALE", "MALE"));
        userMajorField.setItems(FXCollections.observableArrayList(
                "Công nghệ thông tin",
                "Điện tử viễn thông",
                "Kỹ thuật phần mềm",
                "Khoa học máy tính",
                "Hệ thống thông tin",
                "Tự động hoá",
                "Điện tử hoá"
        ));

        // Liên kết cột TableView với các thuộc tính của User
        idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getId()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFullname()));
        genderColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGender().toString()));
        classColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getClassname()));
        majorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMajor()));
        phoneColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPhonenumber()));
        emailColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEmail()));
        userNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPassword()));

        // Gán dữ liệu cho bảng
        userTable.setItems(userData);

        // Sự kiện click chuột vào bảng
        userTable.setOnMouseClicked(this::onTableClick);

        // Load dữ liệu mẫu
        loadSampleData();
    }

    @FXML
    private void onTableClick(MouseEvent event) {
        selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userIdField.setText(selectedUser.getId());
            userFullNameField.setText(selectedUser.getFullname());
            userGenderField.setValue(selectedUser.getGender().toString());
            userClassField.setText(selectedUser.getClassname());
            userMajorField.setValue(selectedUser.getMajor());
            userPhoneField.setText(selectedUser.getPhonenumber());
            userEmailField.setText(selectedUser.getEmail());

        }
    }

    private void clearForm() {
        userIdField.clear();
        userFullNameField.clear();
        userGenderField.setValue(null);
        userClassField.clear();
        userMajorField.setValue(null);
        userPhoneField.clear();
        userEmailField.clear();
    }

    private void loadSampleData() {
        List<User> users = userDao.getAllUsers();
        userData.addAll(users);
    }

    @FXML
    private void onAdd(ActionEvent event) {
        try {
            // Kiểm tra các trường nhập liệu
            if (inCompleteInfo()) {
                showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
                return;
            }

            String userId = userIdField.getText();
            if (isUserExisted(userId)) {
                showAlert("Lỗi", "User đã tồn tại! Vui lòng nhập User khác.", Alert.AlertType.WARNING);
                return;
            }

            User newUser = new User(
                    userIdField.getText(),
                    userFullNameField.getText(),
                    Gender.valueOf(userGenderField.getValue()),
                    userClassField.getText(),
                    userMajorField.getValue(),
                    userPhoneField.getText(),
                    userEmailField.getText()
            );

            // Thêm vào bảng và database
            userData.add(newUser);
            saveToDatabase(newUser);

            showAlert("Thành công", "Thêm người dùng thành công!", Alert.AlertType.INFORMATION);
            clearForm();
        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi khi thêm người dùng: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Kiểm tra các trường nhập liệu có bị để trống không
    private boolean inCompleteInfo() {
        return userIdField.getText().isBlank() ||
                userFullNameField.getText().isBlank() ||
                userGenderField.getValue() == null ||
                userClassField.getText().isBlank() ||
                userMajorField.getValue() == null ||
                userPhoneField.getText().isBlank() ||
                userEmailField.getText().isBlank();
    }

    // Kiểm tra xem user có tồn tại trong bảng hay chưa thông qua ID
    private boolean isUserExisted(String userId) {
        for (User user : userData) {
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void onEdit(ActionEvent event) throws SQLException {
        if (selectedUser != null) {
            selectedUser.setId(userIdField.getText());
            selectedUser.setFullname(userFullNameField.getText());
            selectedUser.setGender(Gender.valueOf(userGenderField.getValue()));
            selectedUser.setClassname(userClassField.getText());
            selectedUser.setMajor(userMajorField.getValue());
            selectedUser.setPhonenumber(userPhoneField.getText());
            selectedUser.setEmail(userEmailField.getText());

            userTable.refresh();
            updateInDatabase(selectedUser); // Cập nhật vào Database

            clearForm();
            selectedUser = null;
        } else {
            showAlert("Thông báo", "Hãy chọn một sinh viên để sửa!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        if (selectedUser != null) {
            if (userDao.hasUnreturnedBooks(selectedUser.getId())) {
                showAlert("Thông báo", "Không thể xoá người dùng! Người dùng đang mượn sách!", Alert.AlertType.WARNING);
                return;
            }

            userData.remove(selectedUser);
            deleteFromDatabase(selectedUser);

            userTable.setItems(FXCollections.observableArrayList(userData));
            userTable.refresh();

            clearForm();
            selectedUser = null;

            showAlert("Thông báo", "Xóa thành công!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Thông báo", "Hãy chọn một sinh viên để xóa!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onSearch(ActionEvent event) {
        try {
            // Lấy tiêu chí và từ khóa tìm kiếm
            String criteria = searchCriteria.getValue();
            String keyword = searchField.getText().trim();

            if (criteria == null || criteria.isBlank() || keyword.isBlank()) {
                showAlert("Lỗi", "Vui lòng chọn tiêu chí và nhập từ khóa tìm kiếm!", Alert.AlertType.WARNING);
                return;
            }

            ObservableList<User> filteredUsers = FXCollections.observableArrayList();
            for (User user : userData) {
                switch (criteria) {
                    case "ID":
                        if (user.getId().toLowerCase().contains(keyword.toLowerCase())) {
                            filteredUsers.add(user);
                        }
                        break;
                    case "Tên":
                        if (user.getFullname().toLowerCase().contains(keyword.toLowerCase())) {
                            filteredUsers.add(user);
                        }
                        break;
                    case "Lớp":
                        if (user.getClassname().toLowerCase().contains(keyword.toLowerCase())) {
                            filteredUsers.add(user);
                        }
                        break;
                    default:
                        showAlert("Lỗi", "Tiêu chí không hợp lệ!", Alert.AlertType.ERROR);
                        return;
                }
            }

            // Hiển thị kết quả trong bảng
            userTable.setItems(filteredUsers);

            if (filteredUsers.isEmpty()) {
                showAlert("Thông báo", "Không tìm thấy kết quả nào phù hợp!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi khi tìm kiếm: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    //-------------------UPDATE INTO DATABASE-------------------
    private void saveToDatabase(User user) throws SQLException {
        userDao.addUser(user);
        System.out.println("Lưu vào cơ sở dữ liệu: " + user);
    }

    private void updateInDatabase(User user) throws SQLException {
        userDao.updateUser(user);
        System.out.println("Cập nhật cơ sở dữ liệu: " + user);
    }

    private void deleteFromDatabase(User user) {
        userDao.deleteUser(user.getId());
        System.out.println("Xóa khỏi cơ sở dữ liệu: " + user);
    }
    //----------------------------------------------------


    // Hiển thị thông báo Alert
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void onShowDetail(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Thông báo", "Vui lòng chọn người dùng!", Alert.AlertType.WARNING);
            return;
        }
        showUserDetails(selectedUser);
    }

    private void showUserDetails(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Admin/UserDetailsDialog.fxml"));
            DialogPane dialogPane = loader.load();

            UserDetailsDialogController controller = loader.getController();
            controller.setUserDetails(user);

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
        final UserManagementController.Delta dragDelta = new UserManagementController.Delta();

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
