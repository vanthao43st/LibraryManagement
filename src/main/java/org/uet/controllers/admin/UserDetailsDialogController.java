package org.uet.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.uet.database.dao.UserDao;
import org.uet.entity.User;

public class UserDetailsDialogController {

    @FXML
    private Label userIdLabel, userNameLabel, genderLabel, classLabel, majorLabel, phoneLabel, emailLabel, statusLabel;

    private final UserDao userDao = new UserDao();

    public void setUserDetails(User user) {
        if (user != null) {
            userIdLabel.setText(user.getId());
            userNameLabel.setText(user.getFullname());
            genderLabel.setText(user.getGender().toString());
            classLabel.setText(user.getClassname());
            majorLabel.setText(user.getMajor());
            phoneLabel.setText(user.getPhonenumber());
            emailLabel.setText(user.getEmail());

            // Check status in library
            if (userDao.hasUnreturnedBooks(user.getId())) {
                statusLabel.setText("Đang mượn sách.");
            } else {
                statusLabel.setText("Đã trả sách.");
            }
        }
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) userIdLabel.getScene().getWindow();
        stage.close();
    }
}
