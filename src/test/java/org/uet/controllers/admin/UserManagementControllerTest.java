package org.uet.controllers.admin;

import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserManagementControllerTest {

    private UserManagementController controller;
    private ComboBox<String> searchCriteria, userGenderField, userMajorField;
    private TextField searchField, userIdField, userFullNameField, usernameField,
            userClassField, userPhoneField, userEmailField, userPasswordField;
    private TableView<User> userTable;
    private TableColumn<User, String> idColumn, nameColumn, genderColumn,
            classColumn, majorColumn, phoneColumn, emailColumn,
            usernameColumn, passwordColumn;
    private ObservableList<User> userData;

    @BeforeEach
    void setUp() {
        controller = new UserManagementController();
        searchField = new TextField();
        userIdField = new TextField();
        userFullNameField = new TextField();
        usernameField = new TextField();
        userClassField = new TextField();
        userPhoneField = new TextField();
        userEmailField = new TextField();
        userPasswordField = new TextField();
        searchCriteria = new ComboBox<>();
        userGenderField = new ComboBox<>();
        userMajorField = new ComboBox<>();
        userTable = new TableView<>();
        idColumn = new TableColumn<>();
        nameColumn = new TableColumn<>();
        genderColumn = new TableColumn<>();
        classColumn = new TableColumn<>();
        majorColumn = new TableColumn<>();
        phoneColumn = new TableColumn<>();
        emailColumn = new TableColumn<>();
        usernameColumn = new TableColumn<>();
        passwordColumn = new TableColumn<>();
        userData = FXCollections.observableArrayList();

        controller.searchField = searchField;
        controller.userIdField = userIdField;
        controller.userFullNameField = userFullNameField;
        controller.usernameField = usernameField;
        controller.userClassField = userClassField;
        controller.userPhoneField = userPhoneField;
        controller.userEmailField = userEmailField;
        controller.userPasswordField = userPasswordField;
        controller.searchCriteria = searchCriteria;
        controller.userGenderField = userGenderField;
        controller.userMajorField = userMajorField;
        controller.userTable = userTable;
        controller.idColumn = idColumn;
        controller.nameColumn = nameColumn;
        controller.genderColumn = genderColumn;
        controller.classColumn = classColumn;
        controller.majorColumn = majorColumn;
        controller.phoneColumn = phoneColumn;
        controller.emailColumn = emailColumn;
        controller.usernameColumn = usernameColumn;
        controller.passwordColumn = passwordColumn;
        controller.userData.addAll(userData);
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            controller.initialize();

            assertNotNull(idColumn.getCellValueFactory());
            assertNotNull(nameColumn.getCellValueFactory());
            assertNotNull(genderColumn.getCellValueFactory());
            assertNotNull(classColumn.getCellValueFactory());
            assertNotNull(majorColumn.getCellValueFactory());
            assertNotNull(phoneColumn.getCellValueFactory());
            assertNotNull(emailColumn.getCellValueFactory());
            assertNotNull(usernameColumn.getCellValueFactory());
            assertNotNull(passwordColumn.getCellValueFactory());
        });
    }

    @Test
    void testOnTableClick() {
        User user = new User("123", "John Doe", Gender.MALE, "Class A", "Computer Science", "1234567890", "john@example.com", "johndoe", "password");
        userData.add(user);
        userTable.setItems(userData);

        userTable.getSelectionModel().select(user);
        controller.onTableClick(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null));

        assertEquals("123", userIdField.getText());
        assertEquals("John Doe", userFullNameField.getText());
        assertEquals("MALE", userGenderField.getValue());
        assertEquals("Class A", userClassField.getText());
        assertEquals("Computer Science", userMajorField.getValue());
        assertEquals("1234567890", userPhoneField.getText());
        assertEquals("john@example.com", userEmailField.getText());
        assertEquals("johndoe", usernameField.getText());
        assertEquals("password", userPasswordField.getText());
    }

    @Test
    void testInCompleteInfo() {
        userIdField.setText("");
        userFullNameField.setText("Test Name");
        assertTrue(controller.inCompleteInfo());

        userIdField.setText("124");
        userGenderField.setValue("MALE");
        userMajorField.setValue("Công nghệ thông tin");
        assertFalse(controller.inCompleteInfo());
    }

    @Test
    void testIsUserExisted() {
        User user = new User("123", "John Doe", Gender.MALE, "Class A", "Computer Science", "1234567890", "john@example.com", "johndoe", "password");
        userData.add(user);
        userTable.setItems(userData);

        assertTrue(controller.isUserExisted("123"));
        assertFalse(controller.isUserExisted("124"));
    }

    @Test
    void testOnAdd() {
        userIdField.setText("124");
        userFullNameField.setText("Jane Doe");
        userGenderField.setValue("FEMALE");
        userClassField.setText("Class B");
        userMajorField.setValue("Kỹ thuật phần mềm");
        userPhoneField.setText("0987654321");
        userEmailField.setText("jane@example.com");
        usernameField.setText("janedoe");
        userPasswordField.setText("password123");

        controller.onAdd(new ActionEvent());

        assertEquals(1, controller.userData.size());
        assertEquals("124", controller.userData.get(0).getId());
        assertEquals("Jane Doe", controller.userData.get(0).getFullname());
        assertEquals(Gender.FEMALE, controller.userData.get(0).getGender());
        assertEquals("Class B", controller.userData.get(0).getClassname());
        assertEquals("Kỹ thuật phần mềm", controller.userData.get(0).getMajor());
        assertEquals("0987654321", controller.userData.get(0).getPhonenumber());
        assertEquals("jane@example.com", controller.userData.get(0).getEmail());
        assertEquals("janedoe", controller.userData.get(0).getUsername());
        assertEquals("password123", controller.userData.get(0).getPassword());
    }

    @Test
    void testOnDelete() {
        User user = new User("123", "John Doe", Gender.MALE, "Class A", "Computer Science", "1234567890", "john@example.com", "johndoe", "password");
        userData.add(user);
        userTable.setItems(userData);
        userTable.getSelectionModel().select(user);

        controller.selectedUser = user;

        controller.onDelete(new ActionEvent());

        assertFalse(controller.userData.contains(user));
    }

    @Test
    void testOnSearch() {
        User user1 = new User("123", "John Doe", Gender.MALE, "Class A", "Computer Science", "1234567890", "john@example.com", "johndoe", "password");
        User user2 = new User("124", "Jane Doe", Gender.FEMALE, "Class B", "Software Engineering", "0987654321", "jane@example.com", "janedoe", "password123");
        userData.addAll(user1, user2);
        userTable.setItems(userData);

        searchCriteria.setValue("Tên");
        searchField.setText("John");

        controller.onSearch(new ActionEvent());

        assertEquals(1, userTable.getItems().size());
        assertEquals("John Doe", userTable.getItems().get(0).getFullname());
    }

    @Test
    void testShowAlert() {
        Platform.runLater(() -> {
            controller.showAlert("Test Alert", "This is a test alert message", Alert.AlertType.INFORMATION);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            assertEquals("Test Alert", alert.getTitle());
            assertEquals("This is a test alert message", alert.getContentText());
        });
    }

    @Test
    void testOnShowDetail() {
        User user = new User("123", "John Doe", Gender.MALE, "Class A", "Computer Science", "1234567890", "john@example.com", "johndoe", "password");
        userData.add(user);
        userTable.setItems(userData);
        userTable.getSelectionModel().select(user);

        Platform.runLater(() -> {
            controller.onShowDetail(new ActionEvent());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Admin/UserDetailsDialog.fxml"));
            try {
                loader.load();
                UserDetailsDialogController dialogController = loader.getController();
                dialogController.setUserDetails(user);

                assertEquals("123", dialogController.userIdLabel.getText());
                assertEquals("John Doe", dialogController.userNameLabel.getText());
            } catch (IOException e) {
                fail("Failed to load UserDetailsDialog.fxml");
            }
        });
    }

    @Test
    void testEnableDragging() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            DialogPane dialogPane = new DialogPane();

            controller.enableDragging(stage, dialogPane);

            dialogPane.fireEvent(new javafx.scene.input.MouseEvent(javafx.scene.input.MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null));
            stage.setX(100);
            stage.setY(100);
            dialogPane.fireEvent(new javafx.scene.input.MouseEvent(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, 200, 200, 200, 200, null, 0, false, false, false, false, false, false, false, false, false, false, null));

            assertEquals(300, stage.getX());
            assertEquals(300, stage.getY());
        });
    }

}