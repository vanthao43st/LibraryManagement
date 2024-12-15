package org.uet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document("12345", "Test Title", "Test Description", "Test Author", 10) {
            @Override
            public String toString() {
                return "Test Document";
            }
        };
    }

    @Test
    void testGetCode() {
        assertEquals("12345", document.getCode());
    }

    @Test
    void testSetCode() {
        document.setCode("54321");
        assertEquals("54321", document.getCode());
    }

    @Test
    void testGetTitle() {
        assertEquals("Test Title", document.getTitle());
    }

    @Test
    void testSetTitle() {
        document.setTitle("New Title");
        assertEquals("New Title", document.getTitle());
    }

    @Test
    void testGetDescription() {
        assertEquals("Test Description", document.getDescription());
    }

    @Test
    void testSetDescription() {
        document.setDescription("New Description");
        assertEquals("New Description", document.getDescription());
    }

    @Test
    void testGetAuthor() {
        assertEquals("Test Author", document.getAuthor());
    }

    @Test
    void testSetAuthor() {
        document.setAuthor("New Author");
        assertEquals("New Author", document.getAuthor());
    }

    @Test
    void testGetQuantity() {
        assertEquals(10, document.getQuantity());
    }

    @Test
    void testSetQuantity() {
        document.setQuantity(20);
        assertEquals(20, document.getQuantity());
    }

    @Test
    void testToString() {
        assertEquals("Test Document", document.toString());
    }
}
