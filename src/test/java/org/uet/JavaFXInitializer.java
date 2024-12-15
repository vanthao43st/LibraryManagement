package org.uet;

import javafx.application.Platform;

public class JavaFXInitializer {

    private static boolean initialized = false;

    public static void initialize() {
        if (!initialized) {
            try {
                // Khởi chạy toolkit JavaFX
                Platform.startup(() -> {
                });
                initialized = true;
            } catch (IllegalStateException ex) {
                // Nếu toolkit đã khởi chạy, bỏ qua lỗi
                initialized = true;
            }
        }
    }
}
