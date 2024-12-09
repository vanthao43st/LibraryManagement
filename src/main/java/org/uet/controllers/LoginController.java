package org.uet.controllers;

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
import org.uet.database.connection.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button closeButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closeButton.setOnMouseClicked(e -> System.exit(-1));

        // Hoãn thiết lập kéo thả đến khi scene sẵn sàng
        usernameField.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                addDraggableFeature();
            }
        });
    }

    @FXML
    private void handleLoginAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            // Tài khoản admin, giữ nguyên giao diện Home.fxml
            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root = FXMLLoader.load(
                        Objects.requireNonNull(
                                getClass().getResource("/Views/Admin/Home.fxml")
                        )
                );
                Scene scene = new Scene(root, 900, 600);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            // Kiểm tra tài khoản user trong cơ sở dữ liệu
            String query = "SELECT * FROM user WHERE user_username = ? AND user_password = ?";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query);) {

                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    // Đăng nhập thành công, chuyển sang giao diện User.fxml
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    Parent root = FXMLLoader.load(
                            Objects.requireNonNull(
                                    getClass().getResource("/Views/User/Home.fxml")
                            )
                    );
                    Scene scene = new Scene(root, 900, 600);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    // Đăng nhập thất bại
                    showErrorAlert("username hoặc password không hợp lệ!");
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
            }
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
}
