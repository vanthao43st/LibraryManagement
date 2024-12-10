package org.uet.controllers.user;

import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.uet.database.dao.UserDao;
import org.uet.entity.SessionManager;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.sql.SQLException;

public class UserDetailController {

    @FXML
    private TextField userIdField, userNameField, userClassField,
            userPhoneField, userEmailField, userPasswordField, userStatusField;

    @FXML
    private ComboBox<String> userGenderField, userMajorField;

    private User currentUser;

    private static UserDao userDao = new UserDao();

    @FXML
    public void initialize() {
        // Cấu hình các ComboBox
        userGenderField.setItems(FXCollections.observableArrayList("MALE", "FEMALE"));
        userMajorField.setItems(FXCollections.observableArrayList(
                "Công nghệ thông tin",
                "Điện tử viễn thông",
                "Kỹ thuật phần mềm",
                "Khoa học máy tính",
                "Hệ thống thông tin",
                "Tự động hóa"
        ));

        // Hiển thị thông tin người dùng hiện tại
        loadUserDetails();

    }

    private void loadUserDetails() {
        try {
            // Lấy thông tin người dùng từ SessionManager
            currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                // Gán giá trị cho các trường trong giao diện
                userIdField.setText(currentUser.getId());
                userNameField.setText(currentUser.getFullname());
                userGenderField.setValue(currentUser.getGender().toString());
                userClassField.setText(currentUser.getClassname());
                userMajorField.setValue(currentUser.getMajor());
                userPhoneField.setText(currentUser.getPhonenumber());
                userEmailField.setText(currentUser.getEmail());
                userPasswordField.setText(currentUser.getPassword());
//                userStatusField.setText(currentUser.getStatus()); // Trường Status chỉ hiển thị
            } else {
                showAlert("Lỗi", "Không thể tải thông tin người dùng!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Lỗi", "Đã xảy ra lỗi khi tải thông tin người dùng: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onEdit() {
        if (currentUser != null) {
            try {
                // Cập nhật thông tin người dùng từ form
                currentUser.setFullname(userNameField.getText());
                currentUser.setGender(Gender.valueOf(userGenderField.getValue()));
                currentUser.setClassname(userClassField.getText());
                currentUser.setMajor(userMajorField.getValue());
                currentUser.setPhonenumber(userPhoneField.getText());
                currentUser.setEmail(userEmailField.getText());
                currentUser.setPassword(userPasswordField.getText());

                // Gọi hàm cập nhật trong cơ sở dữ liệu
                updateInDatabase(currentUser);

                showAlert("Thành công", "Thông tin người dùng đã được cập nhật!", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Lỗi", "Không thể cập nhật thông tin người dùng: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Cảnh báo", "Không có người dùng để chỉnh sửa!", Alert.AlertType.WARNING);
        }
    }

    //-------------------UPDATE INTO DATABASE-------------------
    private void updateInDatabase(User user) throws SQLException {
        userDao.updateUser(user);
        System.out.println("Cập nhật cơ sở dữ liệu: " + user);
    }
    //---------------------------------------------------------

    // Hiển thị thông báo Alert
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
