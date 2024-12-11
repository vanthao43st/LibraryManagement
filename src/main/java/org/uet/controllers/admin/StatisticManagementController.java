package org.uet.controllers.admin;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.uet.database.dao.StatisticDao;

public class StatisticManagementController {
    @FXML
    public Label totalBooksLabel, borrowedBooksLabel, availableBooksLabel;

    private static final StatisticDao statisticDao = new StatisticDao();

    public void setStatisticDetails() {
        statisticDao.getTotalDocumentsAsync().thenAccept(total -> {
            Platform.runLater(() -> totalBooksLabel.setText(String.valueOf(total)));
        });

        statisticDao.getBorrowedDocumentsAsync().thenAccept(borrowed -> {
            Platform.runLater(() -> borrowedBooksLabel.setText(String.valueOf(borrowed)));
        });

        statisticDao.getAvailableDocumentsAsync().thenAccept(available -> {
            Platform.runLater(() -> availableBooksLabel.setText(String.valueOf(available)));
        });
    }

    @FXML
    public void onClose(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }
}
