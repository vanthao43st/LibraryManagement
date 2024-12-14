package org.uet;

import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testStart() throws Exception {
        Application.launch(TestApp.class);
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) throws Exception {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Login.fxml")));
            stage.setTitle("Library Management");
            stage.initStyle(StageStyle.TRANSPARENT);

            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            assertEquals("Library Management", stage.getTitle());

            assertEquals(StageStyle.TRANSPARENT, stage.getStyle());

            assertNotNull(stage.getScene());

            assertTrue(stage.isShowing());
        }

        private double xOffset = 0;
        private double yOffset = 0;
    }
}
