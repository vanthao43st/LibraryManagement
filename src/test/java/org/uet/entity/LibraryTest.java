package org.uet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library(
                "21010938",
                "0001",
                "Sách",
                1,
                "2023-01-01",
                "2023-03-01",
                "2023-02-01",
                "Chưa trả"
        );
    }

    @Test
    void testGetUserId() {
        assertEquals("21010938", library.getUserId());
    }

    @Test
    void testSetUserId() {
        library.setUserId("21010939");
        assertEquals("21010939", library.getUserId());
    }

    @Test
    void testGetDocumentCode() {
        assertEquals("0001", library.getDocumentCode());
    }

    @Test
    void testSetDocumentCode() {
        library.setDocumentCode("0002");
        assertEquals("0002", library.getDocumentCode());
    }

    @Test
    void testGetDocumentType() {
        assertEquals("Sách", library.getDocumentType());
    }

    @Test
    void testSetDocumentType() {
        library.setDocumentType("Luận văn");
        assertEquals("Luận văn", library.getDocumentType());
    }

    @Test
    void testGetQuantity() {
        assertEquals(1, library.getQuantity());
    }

    @Test
    void testSetQuantity() {
        library.setQuantity(2);
        assertEquals(2, library.getQuantity());
    }

    @Test
    void testGetBorrowDate() {
        assertEquals("2023-01-01", library.getBorrowDate());
    }

    @Test
    void testSetBorrowDate() {
        library.setBorrowDate("2023-01-02");
        assertEquals("2023-01-02", library.getBorrowDate());
    }

    @Test
    void testGetReturnDate() {
        assertEquals("2023-03-01", library.getReturnDate());
    }

    @Test
    void testSetReturnDate() {
        library.setReturnDate("2023-03-02");
        assertEquals("2023-03-02", library.getReturnDate());
    }

    @Test
    void testGetDueDate() {
        assertEquals("2023-02-01", library.getDueDate());
    }

    @Test
    void testSetDueDate() {
        library.setDueDate("2023-02-02");
        assertEquals("2023-02-02", library.getDueDate());
    }

    @Test
    void testGetStatus() {
        assertEquals("Chưa trả", library.getStatus());
    }

    @Test
    void testSetStatus() {
        library.setStatus("Đã trả");
        assertEquals("Đã trả", library.getStatus());
    }

    @Test
    void testGetLateDays() {
        assertEquals(0, library.getLateDays());
    }

    @Test
    void testSetLateDays() {
        library.setLateDays(5);
        assertEquals(5, library.getLateDays());
    }

    @Test
    void testGetFine() {
        assertEquals(0, library.getFine());
    }

    @Test
    void testSetFine() {
        library.setFine(10000);
        assertEquals(10000, library.getFine());
    }

    @Test
    void testGetTitle() {
        library.setTitle("Test Title");
        assertEquals("Test Title", library.getTitle());
    }

    @Test
    void testSetTitle() {
        library.setTitle("New Title");
        assertEquals("New Title", library.getTitle());
    }

    @Test
    void testGetDescription() {
        library.setDescription("Test Description");
        assertEquals("Test Description", library.getDescription());
    }

    @Test
    void testSetDescription() {
        library.setDescription("New Description");
        assertEquals("New Description", library.getDescription());
    }

    @Test
    void testToString() {
        String expected = "Library{borrowDate='2023-01-01', userId='21010938', documentCode='0001', documentType='Sách', quantity=1, returnDate='2023-03-01', dueDate='2023-02-01', status='Chưa trả', lateDays=0, fine=0.0, title='', description=''}";
        assertEquals(expected, library.toString());
    }
}
