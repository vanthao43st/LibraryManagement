package org.uet.database.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class StatisticDaoTest {

    private StatisticDao statisticDao;

    @BeforeEach
    void setUp() {
        statisticDao = new StatisticDao();
    }

    @Test
    void testGetTotalDocumentsAsync() {
        CompletableFuture<Integer> totalDocumentsFuture = statisticDao.getTotalDocumentsAsync();
        int totalDocuments = totalDocumentsFuture.join();

        assertTrue(totalDocuments >= 0, "Total documents should be greater than or equal to 0");
    }

    @Test
    void testGetBorrowedDocumentsAsync() {
        CompletableFuture<Integer> borrowedDocumentsFuture = statisticDao.getBorrowedDocumentsAsync();
        int borrowedDocuments = borrowedDocumentsFuture.join();

        assertTrue(borrowedDocuments >= 0, "Borrowed documents should be greater than or equal to 0");
    }

    @Test
    void testGetAvailableDocumentsAsync() {
        CompletableFuture<Integer> availableDocumentsFuture = statisticDao.getAvailableDocumentsAsync();
        int availableDocuments = availableDocumentsFuture.join();

        assertTrue(availableDocuments >= 0, "Available documents should be greater than or equal to 0");
    }
}
