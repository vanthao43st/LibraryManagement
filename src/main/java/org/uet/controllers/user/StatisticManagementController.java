package org.uet.controllers.user;

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
        totalBooksLabel.setText(String.valueOf(statisticDao.getTotalBooks()));
        borrowedBooksLabel.setText(String.valueOf(statisticDao.getBorrowedBooks()));
        availableBooksLabel.setText(String.valueOf(statisticDao.getAvailableBooks()));
    }

    @FXML
    public void onClose(ActionEvent event) {
        ((Button) event.getSource()).getScene().getWindow().hide();
    }
}
