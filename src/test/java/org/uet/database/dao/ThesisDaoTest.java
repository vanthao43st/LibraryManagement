package org.uet.database.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.Thesis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class ThesisDaoTest {

    private ThesisDao thesisDao;

    @BeforeEach
    void setUp() {
        thesisDao = new ThesisDao();
    }

    @Test
    void testGetAllThesisAsync() {
        CompletableFuture<ArrayList<Thesis>> thesesFuture = thesisDao.getAllThesisAsync();
        ArrayList<Thesis> theses = thesesFuture.join();

        assertNotNull(theses);
        assertFalse(theses.isEmpty());
    }

    @Test
    void testAddThesisAsync() {
        Thesis newThesis = new Thesis(
                "John Doe",
                "99999999",
                "Thesis on Computer Science",
                "Thesis Title",
                "PhD",
                "Computer Science",
                5,
                2023,
                "Dr. Smith",
                "University of Technology"
        );

        CompletableFuture<Void> addThesisFuture = thesisDao.addThesisAsync(newThesis);
        addThesisFuture.join();

        CompletableFuture<ArrayList<Thesis>> thesesFuture = thesisDao.getAllThesisAsync();
        ArrayList<Thesis> theses = thesesFuture.join();

        boolean thesisExists = theses.stream().anyMatch(thesis -> thesis.getCode().equals("99999999"));
        assertTrue(thesisExists);
    }

    @Test
    void testUpdateThesisAsync() {
        Thesis updatedThesis = new Thesis(
                "John Doe",
                "99999999",
                "Updated thesis description",
                "Updated Thesis Title",
                "PhD",
                "Computer Science",
                10,
                2024,
                "Dr. Smith",
                "University of Technology"
        );

        CompletableFuture<Void> updateThesisFuture = thesisDao.updateThesisAsync(updatedThesis);
        updateThesisFuture.join();

        CompletableFuture<ArrayList<Thesis>> thesesFuture = thesisDao.getAllThesisAsync();
        ArrayList<Thesis> theses = thesesFuture.join();

        Thesis thesis = theses.stream().filter(t -> t.getCode().equals("99999999")).findFirst().orElse(null);
        assertNotNull(thesis);
        assertEquals("Updated Thesis Title", thesis.getTitle());
        assertEquals("Updated thesis description", thesis.getDescription());
        assertEquals(10, thesis.getQuantity());
        assertEquals(2024, thesis.getSubmissionYear());
    }

    @Test
    void testDeleteThesisAsync() {
        CompletableFuture<Void> deleteThesisFuture = thesisDao.deleteThesisAsync("99999999");
        deleteThesisFuture.join();

        CompletableFuture<ArrayList<Thesis>> thesesFuture = thesisDao.getAllThesisAsync();
        ArrayList<Thesis> theses = thesesFuture.join();

        boolean thesisExists = theses.stream().anyMatch(thesis -> thesis.getCode().equals("99999999"));
        assertFalse(thesisExists);
    }

    @Test
    void testIsBorrowedThesisAsync() {
        CompletableFuture<Boolean> isBorrowedThesisFuture = thesisDao.isBorrowedThesisAsync("12345");
        boolean isBorrowed = isBorrowedThesisFuture.join();

        assertNotNull(isBorrowed);
    }
}
