package org.uet.controllers.user;

import javafx.application.Platform;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.uet.database.dao.UserDao;
import org.uet.entity.SessionManager;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class UserDetailController {

    @FXML
    private TextField userIdField, userFullNameField, userClassField,
            userPhoneField, userEmailField, usernameField, userPasswordField, userStatusField;

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

    public void loadUserDetails() {
        CompletableFuture.runAsync(() -> {
            try {
                // Lấy thông tin người dùng từ SessionManager
                currentUser = SessionManager.getInstance().getCurrentUser();
                if (currentUser != null) {
                    Platform.runLater(() -> {
                        // Gán giá trị cho các trường trong giao diện
                        userIdField.setText(currentUser.getId());
                        userFullNameField.setText(currentUser.getFullname());
                        userGenderField.setValue(currentUser.getGender().toString());
                        userClassField.setText(currentUser.getClassname());
                        userMajorField.setValue(currentUser.getMajor());
                        userPhoneField.setText(currentUser.getPhonenumber());
                        userEmailField.setText(currentUser.getEmail());
                        usernameField.setText(currentUser.getUsername());
                        userPasswordField.setText(currentUser.getPassword());
                    });

                    userDao.hasUnreturnedBooksAsync(currentUser.getId()).thenAccept(hasUnreturnedBooks -> {
                        Platform.runLater(() -> {
                            if (hasUnreturnedBooks) {
                                userStatusField.setText("Chưa trả sách.");
                            } else {
                                userStatusField.setText("Đã trả sách.");
                            }
                        });
                    }).exceptionally(e -> {
                        Platform.runLater(() -> userStatusField.setText("Lỗi khi kiểm tra trạng thái."));
                        return null;
                    });
                } else {
                    Platform.runLater(() -> showAlert("Lỗi", "Không thể tải thông tin người dùng!", Alert.AlertType.ERROR));
                }
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Lỗi", "Đã xảy ra lỗi khi tải thông tin người dùng: " + e.getMessage(), Alert.AlertType.ERROR));
            }
        });
    }

    @FXML
    protected void onEdit() {
        if (currentUser != null) {
            // Cập nhật thông tin người dùng từ form
            currentUser.setFullname(userFullNameField.getText());
            currentUser.setGender(Gender.valueOf(userGenderField.getValue()));
            currentUser.setClassname(userClassField.getText());
            currentUser.setMajor(userMajorField.getValue());
            currentUser.setPhonenumber(userPhoneField.getText());
            currentUser.setEmail(userEmailField.getText());
            currentUser.setPassword(userPasswordField.getText());

            // Gọi hàm cập nhật trong cơ sở dữ liệu
            updateInDatabase(currentUser);

            showAlert("Thành công", "Thông tin đã được cập nhật!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Cảnh báo", "Không có người dùng để chỉnh sửa!", Alert.AlertType.WARNING);
        }
    }

    //-------------------UPDATE INTO DATABASE-------------------
    protected void updateInDatabase(User user) {
        userDao.updateUserAsync(user).thenRun(() -> {
            Platform.runLater(() -> System.out.println("Cập nhật cơ sở dữ liệu: " + user));
        }).exceptionally(e -> {
            Platform.runLater(() -> System.out.println("Lỗi khi cập nhật cơ sở dữ liệu: " + e.getMessage()));
            return null;
        });
    }
    //---------------------------------------------------------

    // Hiển thị thông báo Alert
    protected void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
