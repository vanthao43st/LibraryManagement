package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.uet.database.dao.UserDao;
import org.uet.entity.User;

public class UserDetailsDialogController {

    @FXML
    protected Label userIdLabel, userNameLabel, genderLabel, classLabel, majorLabel, phoneLabel, emailLabel, statusLabel;

    protected final UserDao userDao = new UserDao();

    public void setUserDetails(User user) {
        if (user != null) {
            Platform.runLater(() -> {
                userIdLabel.setText(user.getId());
                userNameLabel.setText(user.getFullname());
                genderLabel.setText(user.getGender().toString());
                classLabel.setText(user.getClassname());
                majorLabel.setText(user.getMajor());
                phoneLabel.setText(user.getPhonenumber());
                emailLabel.setText(user.getEmail());
            });

            userDao.hasUnreturnedBooksAsync(user.getId()).thenAccept(hasUnreturnedBooks -> {
                Platform.runLater(() -> {
                    if (hasUnreturnedBooks) {
                        statusLabel.setText("Đang mượn sách.");
                    } else {
                        statusLabel.setText("Đã trả sách.");
                    }
                });
            }).exceptionally(e -> {
                Platform.runLater(() -> statusLabel.setText("Lỗi khi kiểm tra trạng thái."));
                return null;
            });
        }
    }

    @FXML
    protected void onClose() {
        Stage stage = (Stage) userIdLabel.getScene().getWindow();
        stage.close();
    }
}
