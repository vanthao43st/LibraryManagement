package org.uet.database.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.database.connection.DBConnection;
import org.uet.entity.Library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class LibraryDaoTest {

    private LibraryDao libraryDao;

    @BeforeEach
    void setUp() {
        libraryDao = new LibraryDao();
    }

    @Test
    void testGetAllLibraryRecordsAsync() {
        CompletableFuture<ArrayList<Library>> libraryRecordsFuture = libraryDao.getAllLibraryRecordsAsync();
        ArrayList<Library> libraryRecords = libraryRecordsFuture.join();

        assertNotNull(libraryRecords);
        assertFalse(libraryRecords.isEmpty());
    }

    @Test
    void testDeleteLibraryRecordAsync() {
        CompletableFuture<Void> deleteLibraryRecordFuture = libraryDao.deleteLibraryRecordAsync();
        deleteLibraryRecordFuture.join();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM library WHERE library_quantity = 0")) {

            ResultSet resultSet = ps.executeQuery();

            assertFalse(resultSet.next());
        } catch (SQLException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testCheckIfBookAsync() {
        CompletableFuture<Boolean> checkIfBookFuture = libraryDao.checkIfBookAsync("12345");
        boolean isBook = checkIfBookFuture.join();

        assertTrue(isBook);
    }

    @Test
    void testCheckAvailabilityAsync() {
        CompletableFuture<Boolean> checkAvailabilityFuture = libraryDao.checkAvailabilityAsync("12345", 1);
        boolean isAvailable = checkAvailabilityFuture.join();

        assertTrue(isAvailable);
    }

    @Test
    void testIsUserExistedAsync() {
        CompletableFuture<Boolean> isUserExistedFuture = libraryDao.isUserExistedAsync("1");
        boolean isUserExisted = isUserExistedFuture.join();

        assertTrue(isUserExisted);
    }

    @Test
    void testIsDocumentCodeExistedAsync() {
        CompletableFuture<Boolean> isDocumentCodeExistedFuture = libraryDao.isDocumentCodeExistedAsync("12345");
        boolean isDocumentCodeExisted = isDocumentCodeExistedFuture.join();

        assertTrue(isDocumentCodeExisted);
    }

    @Test
    void testGetLibraryIdAsync() {
        CompletableFuture<Integer> getLibraryIdFuture = libraryDao.getLibraryIdAsync("1", "12345", 1);
        Integer libraryId = getLibraryIdFuture.join();

        assertNotNull(libraryId);
    }

    @Test
    void testBorrowDocumentAsync() {
        String userId = "1";
        String documentCode = "12345";
        String documentType = "Sách";
        int quantity = 1;

        CompletableFuture<Boolean> borrowDocumentFuture = libraryDao.borrowDocumentAsync(userId, documentCode, documentType, quantity);
        boolean isBorrowed = borrowDocumentFuture.join();

        assertTrue(isBorrowed);

        // Kiểm tra xem tài liệu có được ghi lại trong cơ sở dữ liệu không
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM library WHERE library_user_id = ? AND library_document_code = ?")) {

            ps.setString(1, userId);
            ps.setString(2, documentCode);
            ResultSet resultSet = ps.executeQuery();

            assertTrue(resultSet.next());
        } catch (SQLException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void testReturnDocumentAsync() {
        String userId = "1";
        String documentCode = "12345";
        int returnQuantity = 1;
        String returnDate = "2023-12-15";
        int libraryId = 1;

        CompletableFuture<Boolean> returnDocumentFuture = libraryDao.returnDocumentAsync(userId, documentCode, returnQuantity, returnDate, libraryId);
        boolean isReturned = returnDocumentFuture.join();

        assertTrue(isReturned);

        // Kiểm tra xem tài liệu có được cập nhật trong cơ sở dữ liệu không
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM library WHERE library_user_id = ? AND library_document_code = ? AND library_id = ?")) {

            ps.setString(1, userId);
            ps.setString(2, documentCode);
            ps.setInt(3, libraryId);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                assertEquals(returnDate, resultSet.getString("library_return_date"));
            }
        } catch (SQLException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}
