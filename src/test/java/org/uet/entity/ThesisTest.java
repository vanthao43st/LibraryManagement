package org.uet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThesisTest {

    private Thesis thesis;

    @BeforeEach
    void setUp() {
        thesis = new Thesis(
                "John Doe",
                "12345",
                "Thesis on Computer Science",
                "Thesis Title",
                "PhD",
                "Computer Science",
                5,
                2023,
                "Dr. Smith",
                "University of Technology"
        );
    }

    @Test
    void testGetDegree() {
        assertEquals("PhD", thesis.getDegree());
    }

    @Test
    void testSetDegree() {
        thesis.setDegree("Master's");
        assertEquals("Master's", thesis.getDegree());
    }

    @Test
    void testGetMajor() {
        assertEquals("Computer Science", thesis.getMajor());
    }

    @Test
    void testSetMajor() {
        thesis.setMajor("Information Technology");
        assertEquals("Information Technology", thesis.getMajor());
    }

    @Test
    void testGetSubmissionYear() {
        assertEquals(2023, thesis.getSubmissionYear());
    }

    @Test
    void testSetSubmissionYear() {
        thesis.setSubmissionYear(2024);
        assertEquals(2024, thesis.getSubmissionYear());
    }

    @Test
    void testGetSupervisor() {
        assertEquals("Dr. Smith", thesis.getSupervisor());
    }

    @Test
    void testSetSupervisor() {
        thesis.setSupervisor("Dr. Johnson");
        assertEquals("Dr. Johnson", thesis.getSupervisor());
    }

    @Test
    void testGetUniversity() {
        assertEquals("University of Technology", thesis.getUniversity());
    }

    @Test
    void testSetUniversity() {
        thesis.setUniversity("Institute of Science");
        assertEquals("Institute of Science", thesis.getUniversity());
    }

    @Test
    void testGetCode() {
        assertEquals("12345", thesis.getCode());
    }

    @Test
    void testSetCode() {
        thesis.setCode("54321");
        assertEquals("54321", thesis.getCode());
    }

    @Test
    void testGetTitle() {
        assertEquals("Thesis Title", thesis.getTitle());
    }

    @Test
    void testSetTitle() {
        thesis.setTitle("New Thesis Title");
        assertEquals("New Thesis Title", thesis.getTitle());
    }

    @Test
    void testGetDescription() {
        assertEquals("Thesis on Computer Science", thesis.getDescription());
    }

    @Test
    void testSetDescription() {
        thesis.setDescription("New Thesis Description");
        assertEquals("New Thesis Description", thesis.getDescription());
    }

    @Test
    void testGetAuthor() {
        assertEquals("John Doe", thesis.getAuthor());
    }

    @Test
    void testSetAuthor() {
        thesis.setAuthor("Jane Doe");
        assertEquals("Jane Doe", thesis.getAuthor());
    }

    @Test
    void testGetQuantity() {
        assertEquals(5, thesis.getQuantity());
    }

    @Test
    void testSetQuantity() {
        thesis.setQuantity(10);
        assertEquals(10, thesis.getQuantity());
    }

    @Test
    void testToString() {
        assertEquals("", thesis.toString());
    }
}
