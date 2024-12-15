package org.uet.database.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.database.connection.DBConnection;
import org.uet.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class BookDaoTest {

    private BookDao bookDao;

    @BeforeEach
    void setUp() {
        bookDao = new BookDao();
    }

    @Test
    void testGetAllBooksAsync() {
        CompletableFuture<ArrayList<Book>> booksFuture = bookDao.getAllBooksAsync();
        ArrayList<Book> books = booksFuture.join();

        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    void testAddBookAsync() {
        Book newBook = new Book(
                "99999999",
                "Test Book",
                "This is a test book description",
                "Test Category",
                "Test Author",
                9.99,
                10
        );

        CompletableFuture<Void> addBookFuture = bookDao.addBookAsync(newBook);
        addBookFuture.join();

        CompletableFuture<ArrayList<Book>> booksFuture = bookDao.getAllBooksAsync();
        ArrayList<Book> books = booksFuture.join();

        boolean bookExists = books.stream().anyMatch(book -> book.getCode().equals("99999999"));
        assertTrue(bookExists);
    }

    @Test
    void testUpdateBookAsync() {
        Book updatedBook = new Book(
                "99999999",
                "Updated Book Title",
                "Updated book description",
                "Updated Category",
                "Updated Author",
                19.99,
                20
        );

        CompletableFuture<Void> updateBookFuture = bookDao.updateBookAsync(updatedBook);
        updateBookFuture.join();

        CompletableFuture<ArrayList<Book>> booksFuture = bookDao.getAllBooksAsync();
        ArrayList<Book> books = booksFuture.join();

        Book book = books.stream().filter(b -> b.getCode().equals("99999999")).findFirst().orElse(null);
        assertNotNull(book);
        assertEquals("Updated Book Title", book.getTitle());
        assertEquals("Updated book description", book.getDescription());
        assertEquals("Updated Category", book.getCategory());
        assertEquals("Updated Author", book.getAuthor());
        assertEquals(19.99, book.getPrice());
        assertEquals(20, book.getQuantity());
    }

    @Test
    void testDeleteBookAsync() {
        CompletableFuture<Void> deleteBookFuture = bookDao.deleteBookAsync("99999999");
        deleteBookFuture.join();

        CompletableFuture<ArrayList<Book>> booksFuture = bookDao.getAllBooksAsync();
        ArrayList<Book> books = booksFuture.join();

        boolean bookExists = books.stream().anyMatch(book -> book.getCode().equals("99999999"));
        assertFalse(bookExists);
    }

    @Test
    void testIsBorrowedBookAsync() {
        CompletableFuture<Boolean> isBorrowedBookFuture = bookDao.isBorrowedBookAsync("123");
        boolean isBorrowed = isBorrowedBookFuture.join();

        assertNotNull(isBorrowed);
    }
}
