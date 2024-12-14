package org.uet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book(
                "12345",
                "Test Book",
                "This is a test book description",
                "Test Author",
                "Test Category",
                9.99,
                10
        );
    }

    @Test
    void testGetCategory() {
        assertEquals("Test Category", book.getCategory());
    }

    @Test
    void testSetCategory() {
        book.setCategory("New Category");
        assertEquals("New Category", book.getCategory());
    }

    @Test
    void testGetPrice() {
        assertEquals(9.99, book.getPrice());
    }

    @Test
    void testSetPrice() {
        book.setPrice(19.99);
        assertEquals(19.99, book.getPrice());
    }

    @Test
    void testGetCode() {
        assertEquals("12345", book.getCode());
    }

    @Test
    void testSetCode() {
        book.setCode("54321");
        assertEquals("54321", book.getCode());
    }

    @Test
    void testGetTitle() {
        assertEquals("Test Book", book.getTitle());
    }

    @Test
    void testSetTitle() {
        book.setTitle("New Book Title");
        assertEquals("New Book Title", book.getTitle());
    }

    @Test
    void testGetDescription() {
        assertEquals("This is a test book description", book.getDescription());
    }

    @Test
    void testSetDescription() {
        book.setDescription("New Book Description");
        assertEquals("New Book Description", book.getDescription());
    }

    @Test
    void testGetAuthor() {
        assertEquals("Test Author", book.getAuthor());
    }

    @Test
    void testSetAuthor() {
        book.setAuthor("New Author");
        assertEquals("New Author", book.getAuthor());
    }

    @Test
    void testGetQuantity() {
        assertEquals(10, book.getQuantity());
    }

    @Test
    void testSetQuantity() {
        book.setQuantity(20);
        assertEquals(20, book.getQuantity());
    }

    @Test
    void testToString() {
        assertEquals("", book.toString());
    }
}
