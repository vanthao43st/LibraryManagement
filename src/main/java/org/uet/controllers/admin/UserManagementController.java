package org.uet.controllers.admin;

import javafx.application.Platform;
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
import java.util.Optional;

public class UserManagementController {

    @FXML
    protected ComboBox<String> searchCriteria, userGenderField, userMajorField;

    @FXML
    protected TextField searchField, userIdField, userFullNameField, usernameField,
            userClassField, userPhoneField, userEmailField, userPasswordField;

    @FXML
    protected TableView<User> userTable;

    @FXML
    protected TableColumn<User, String> idColumn, nameColumn, genderColumn,
            classColumn, majorColumn, phoneColumn, emailColumn,
            usernameColumn, passwordColumn;

    protected static final UserDao userDao = new UserDao();

    // Dữ liệu mẫu
    protected final ObservableList<User> userData = FXCollections.observableArrayList();

    // Theo dõi người dùng được chọn
    protected User selectedUser;

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
        usernameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPassword()));

        // Gán dữ liệu cho bảng
        userTable.setItems(userData);

        // Sự kiện click chuột vào bảng
        userTable.setOnMouseClicked(this::onTableClick);

        // Load dữ liệu mẫu
        loadSampleData();
    }

    @FXML
    protected void onTableClick(MouseEvent event) {
        selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userIdField.setEditable(false);
            userIdField.setText(selectedUser.getId());
            userFullNameField.setText(selectedUser.getFullname());
            userGenderField.setValue(selectedUser.getGender().toString());
            userClassField.setText(selectedUser.getClassname());
            userMajorField.setValue(selectedUser.getMajor());
            userPhoneField.setText(selectedUser.getPhonenumber());
            userEmailField.setText(selectedUser.getEmail());
            usernameField.setText(selectedUser.getUsername());
            userPasswordField.setText(selectedUser.getPassword());
        }
    }

    protected void clearForm() {
        userIdField.clear();
        userFullNameField.clear();
        userGenderField.setValue(null);
        userClassField.clear();
        userMajorField.setValue(null);
        userPhoneField.clear();
        userEmailField.clear();
        userPasswordField.clear();
        usernameField.clear();
    }

    protected void loadSampleData() {
        userDao.getAllUsersAsync().thenAccept(users -> {
            Platform.runLater(() -> userData.addAll(users));
        }).exceptionally(e -> {
            Platform.runLater(() -> showAlert("Lỗi", "Không thể tải dữ liệu người dùng: " + e.getMessage(), Alert.AlertType.ERROR));
            return null;
        });
    }

    @FXML
    protected void onAdd(ActionEvent event) {
        try {
            // Kiểm tra các trường nhập liệu
            if (inCompleteInfo()) {
                showAlert("Cảnh báo", "Vui lòng nhập đầy đủ thông tin vào tất cả các trường!", Alert.AlertType.WARNING);
                return;
            }

            String userId = userIdField.getText();
            if (isUserExisted(userId)) {
                showAlert("Lỗi", "User ID đã tồn tại! Vui lòng nhập User ID khác.", Alert.AlertType.ERROR);
                return;
            }

            User newUser = new User(
                    userIdField.getText(),
                    userFullNameField.getText(),
                    Gender.valueOf(userGenderField.getValue()),
                    userClassField.getText(),
                    userMajorField.getValue(),
                    userPhoneField.getText(),
                    userEmailField.getText(),
                    usernameField.getText(),
                    userPasswordField.getText()
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
    protected boolean inCompleteInfo() {
        return userIdField.getText().isBlank() ||
                userFullNameField.getText().isBlank() ||
                userGenderField.getValue() == null ||
                userClassField.getText().isBlank() ||
                userMajorField.getValue() == null ||
                userPhoneField.getText().isBlank() ||
                userEmailField.getText().isBlank() ||
                usernameField.getText().isBlank() ||
                userPasswordField.getText().isBlank();
    }

    // Kiểm tra xem user có tồn tại trong bảng hay chưa thông qua ID
    protected boolean isUserExisted(String userId) {
        for (User user : userData) {
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    protected void onEdit(ActionEvent event) throws SQLException {
        if (selectedUser != null) {
            selectedUser.setId(userIdField.getText());
            selectedUser.setFullname(userFullNameField.getText());
            selectedUser.setGender(Gender.valueOf(userGenderField.getValue()));
            selectedUser.setClassname(userClassField.getText());
            selectedUser.setMajor(userMajorField.getValue());
            selectedUser.setPhonenumber(userPhoneField.getText());
            selectedUser.setEmail(userEmailField.getText());
            selectedUser.setUsername(usernameField.getText());
            selectedUser.setPassword(userPasswordField.getText());

            userTable.refresh();
            updateInDatabase(selectedUser); // Cập nhật vào Database

            clearForm();
            selectedUser = null;
            showAlert("Thông báo", "Cập nhật người dùng thành công.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Cảnh báo", "Hãy chọn một sinh viên để sửa!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    protected void onDelete(ActionEvent event) {
        if (selectedUser != null) {
            // Hiển thị hộp thoại xác nhận
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Xác nhận xoá");
            confirmationAlert.setHeaderText("Bạn có chắc chắn muốn xoá người dùng này?");
            confirmationAlert.setContentText("Hành động này không thể hoàn tác.");

            // Chờ người dùng xác nhận
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Kiểm tra xem người dùng có sách chưa trả hay không
                userDao.hasUnreturnedBooksAsync(selectedUser.getId()).thenAccept(hasUnreturnedBooks -> {
                    if (hasUnreturnedBooks) {
                        Platform.runLater(() -> showAlert("Thông báo", "Không thể xoá người dùng! Người dùng đang mượn sách!", Alert.AlertType.WARNING));
                        return;
                    }

                    Platform.runLater(() -> {
                        userData.remove(selectedUser);
                        deleteFromDatabase(selectedUser);
                        userIdField.setEditable(true);

                        userTable.setItems(FXCollections.observableArrayList(userData));
                        userTable.refresh();

                        clearForm();
                        selectedUser = null;

                        showAlert("Thông báo", "Xóa thành công!", Alert.AlertType.INFORMATION);
                    });
                }).exceptionally(e -> {
                    Platform.runLater(() -> showAlert("Lỗi", "Đã xảy ra lỗi khi kiểm tra sách chưa trả: " + e.getMessage(), Alert.AlertType.ERROR));
                    return null;
                });
            }
        } else {
            showAlert("Thông báo", "Hãy chọn một sinh viên để xóa!", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    protected void onSearch(ActionEvent event) {
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
    protected void saveToDatabase(User user) {
        userDao.addUserAsync(user).thenRun(() -> {
            Platform.runLater(() -> {
                System.out.println("Lưu vào cơ sở dữ liệu: " + user);
            });
        }).exceptionally(e -> {
            Platform.runLater(() -> System.out.println("Lỗi khi lưu vào cơ sở dữ liệu: " + e.getMessage()));
            return null;
        });
    }

    protected void updateInDatabase(User user) {
        userDao.updateUserAsync(user).thenRun(() -> {
            Platform.runLater(() -> System.out.println("Cập nhật cơ sở dữ liệu: " + user));
        }).exceptionally(e -> {
            Platform.runLater(() -> System.out.println("Lỗi khi cập nhật cơ sở dữ liệu: " + e.getMessage()));
            return null;
        });
    }

    protected void deleteFromDatabase(User user) {
        userDao.deleteUserAsync(user.getId()).thenRun(() -> {
            Platform.runLater(() -> System.out.println("Xóa khỏi cơ sở dữ liệu: " + user));
        }).exceptionally(e -> {
            Platform.runLater(() -> System.out.println("Lỗi khi xóa khỏi cơ sở dữ liệu: " + e.getMessage()));
            return null;
        });
    }    //----------------------------------------------------


    // Hiển thị thông báo Alert
    protected void showAlert(String title, String message, Alert.AlertType type) {
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

    protected void showUserDetails(User user) {
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

    protected void enableDragging(Stage stage, DialogPane dialogPane) {
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
