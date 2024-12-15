package org.uet.controllers.user;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.uet.JavaFXInitializer;
import org.uet.enums.Gender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.SessionManager;
import org.uet.entity.User;
import org.uet.database.dao.UserDao;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailControllerTest {

    private UserDetailController controller;
    private TextField userIdField, userFullNameField, userClassField, userPhoneField, userEmailField, usernameField, userPasswordField, userStatusField;
    private ComboBox<String> userGenderField, userMajorField;
    private UserDao userDao;
    private User currentUser;

    @BeforeEach
    void setUp() {
        JavaFXInitializer.initialize();

        controller = new UserDetailController();
        userIdField = new TextField();
        userFullNameField = new TextField();
        userClassField = new TextField();
        userPhoneField = new TextField();
        userEmailField = new TextField();
        usernameField = new TextField();
        userPasswordField = new TextField();
        userStatusField = new TextField();
        userGenderField = new ComboBox<>();
        userMajorField = new ComboBox<>();
        userDao = new UserDao();
        currentUser = new User("123", "John Doe", Gender.MALE, "Class A", "Computer Science", "1234567890", "john@example.com", "johndoe", "password");

        controller.userIdField = userIdField;
        controller.userFullNameField = userFullNameField;
        controller.userClassField = userClassField;
        controller.userPhoneField = userPhoneField;
        controller.userEmailField = userEmailField;
        controller.usernameField = usernameField;
        controller.userPasswordField = userPasswordField;
        controller.userStatusField = userStatusField;
        controller.userGenderField = userGenderField;
        controller.userMajorField = userMajorField;
        UserDetailController.userDao = userDao;

        controller.initialize();

        SessionManager.getInstance().setCurrentUser(currentUser);
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            controller.initialize();

            ObservableList<String> genders = FXCollections.observableArrayList("MALE", "FEMALE");
            ObservableList<String> majors = FXCollections.observableArrayList(
                    "Công nghệ thông tin",
                    "Điện tử viễn thông",
                    "Kỹ thuật phần mềm",
                    "Khoa học máy tính",
                    "Hệ thống thông tin",
                    "Tự động hóa"
            );

            assertEquals(genders, userGenderField.getItems());
            assertEquals(majors, userMajorField.getItems());
        });
    }

    @Test
    void testLoadUserDetails() {
        Platform.runLater(() -> {
            controller.loadUserDetails();

            assertEquals("123", userIdField.getText());
            assertEquals("John Doe", userFullNameField.getText());
            assertEquals("MALE", userGenderField.getValue());
            assertEquals("Class A", userClassField.getText());
            assertEquals("Computer Science", userMajorField.getValue());
            assertEquals("1234567890", userPhoneField.getText());
            assertEquals("john@example.com", userEmailField.getText());
            assertEquals("johndoe", usernameField.getText());
            assertEquals("password", userPasswordField.getText());
        });
    }

    @Test
    void testOnEdit() {
        Platform.runLater(() -> {
            userFullNameField.setText("Jane Doe");
            userGenderField.setValue("FEMALE");
            userClassField.setText("Class B");
            userMajorField.setValue("Software Engineering");
            userPhoneField.setText("0987654321");
            userEmailField.setText("jane@example.com");
            usernameField.setText("janedoe");
            userPasswordField.setText("newpassword");

            controller.onEdit();

            assertEquals("Jane Doe", currentUser.getFullname());
            assertEquals(Gender.FEMALE, currentUser.getGender());
            assertEquals("Class B", currentUser.getClassname());
            assertEquals("Software Engineering", currentUser.getMajor());
            assertEquals("0987654321", currentUser.getPhonenumber());
            assertEquals("jane@example.com", currentUser.getEmail());
            assertEquals("newpassword", currentUser.getPassword());
        });
    }

    @Test
    void testShowAlert() {
        Platform.runLater(() -> {
            controller.showAlert("Test Alert", "This is a test alert message", AlertType.INFORMATION);

            Alert alert = new Alert(AlertType.INFORMATION);
            assertEquals("Test Alert", alert.getTitle());
            assertEquals("This is a test alert message", alert.getContentText());
        });
    }
}
