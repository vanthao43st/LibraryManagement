package org.uet.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.uet.controllers.admin.AdminHomeController;
import org.uet.controllers.user.UserHomeController;
import org.uet.database.connection.DBConnection;
import org.uet.database.dao.UserDao;
import org.uet.entity.SessionManager;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button closeButton, registerButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private static final UserDao userDao = new UserDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeButton.setOnMouseClicked(e -> System.exit(-1));

        // Hoãn thiết lập kéo thả đến khi scene sẵn sàng
        usernameField.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                addDraggableFeature();
            }
        });

        registerButton.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Register.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) registerButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene); // Đổi scene sang màn hình Register

                // Hiển thị lại cửa sổ mới
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML
    private void handleLoginAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            try (Connection connection = DBConnection.getConnection()) {
                // Kiểm tra nếu tài khoản là admin
                if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                    // Chuyển sang giao diện Admin
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Admin/AdminHome.fxml"));
                    Parent root = loader.load();

                    // Lấy controller của Home Admin và thiết lập dữ liệu nếu cần
                    AdminHomeController adminController = loader.getController();
                    adminController.setWelcomeMessage("Hi, Admin!");

                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    // Kiểm tra thông tin user trong cơ sở dữ liệu
                    String query = "SELECT * FROM user WHERE user_username = ? AND user_password = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        // Lấy thông tin người dùng từ kết quả truy vấn
                        User user = new User(
                                resultSet.getString("user_id"),
                                resultSet.getString("user_fullname"),
                                Gender.valueOf(resultSet.getString("user_gender")),
                                resultSet.getString("user_class"),
                                resultSet.getString("user_major"),
                                resultSet.getString("user_phone"),
                                resultSet.getString("user_email"),
                                resultSet.getString("user_username"),
                                resultSet.getString("user_password")
                        );

                        // Lưu thông tin người dùng vào SessionManager
                        SessionManager.getInstance().setCurrentUser(user);

                        // Chuyển sang giao diện User
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/User/UserHome.fxml"));
                        Parent root = loader.load();

                        // Lấy controller của Home User và truyền fullname
                        UserHomeController userController = loader.getController();
                        System.out.println(user.getFullname());
                        userController.setWelcomeMessage("Hi, " + user.getFullname());

                        Stage stage = (Stage) usernameField.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } else {
                        // Tài khoản không hợp lệ
                        showErrorAlert("Tên đăng nhập hoặc mật khẩu không chính xác!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorAlert("Đã xảy ra lỗi khi kết nối cơ sở dữ liệu!");
            }
        } else {
            showErrorAlert("Hãy nhập đúng tài khoản!");
        }
    }


    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Đăng nhập thất bại");
        alert.setHeaderText(message);
        alert.setContentText("Hãy thử lại!");
        alert.showAndWait();
    }

    private void addDraggableFeature() {
        usernameField.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((windowObservable, oldWindow, newWindow) -> {
                    if (newWindow instanceof Stage stage) {
                        // Ghi lại tọa độ khi nhấn chuột
                        newScene.setOnMousePressed(event -> {
                            xOffset = stage.getX() - event.getScreenX();
                            yOffset = stage.getY() - event.getScreenY();
                        });

                        // Cập nhật vị trí cửa sổ khi kéo chuột
                        newScene.setOnMouseDragged(event -> {
                            stage.setX(event.getScreenX() + xOffset);
                            stage.setY(event.getScreenY() + yOffset);
                        });
                    }
                });
            }
        });
    }

    public void handleRegisterAction(ActionEvent event) {
    }
}
