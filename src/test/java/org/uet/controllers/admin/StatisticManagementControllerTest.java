package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.JavaFXInitializer;

import static org.junit.jupiter.api.Assertions.*;

class StatisticManagementControllerTest {

    private StatisticManagementController controller;
    private Label totalBooksLabel, borrowedBooksLabel, availableBooksLabel;

    @BeforeEach
    void setUp() {
        JavaFXInitializer.initialize();

        controller = new StatisticManagementController();
        totalBooksLabel = new Label();
        borrowedBooksLabel = new Label();
        availableBooksLabel = new Label();

        controller.totalBooksLabel = totalBooksLabel;
        controller.borrowedBooksLabel = borrowedBooksLabel;
        controller.availableBooksLabel = availableBooksLabel;
    }

    @Test
    void testSetStatisticDetails() {
        Platform.runLater(() -> {
            // Giả lập dữ liệu từ DAO
            controller.setStatisticDetails();

            // Kiểm tra xem các nhãn được thiết lập đúng không
            assertNotNull(totalBooksLabel.getText());
            assertNotNull(borrowedBooksLabel.getText());
            assertNotNull(availableBooksLabel.getText());
        });
    }

    @Test
    void testOnClose() {
        Platform.runLater(() -> {
            Button closeButton = new Button();
            closeButton.setOnAction(event -> controller.onClose(event));

            // Giả lập việc hiển thị cửa sổ
            Platform.runLater(() -> {
                Stage stage = new Stage();
                stage.setScene(new Scene(new StackPane(closeButton), 100, 100));
                stage.show();

                assertTrue(stage.isShowing());

                closeButton.fireEvent(new ActionEvent());

                assertFalse(stage.isShowing());
            });
        });
    }
}
