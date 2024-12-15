package org.uet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.Book;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class GoogleBooksAPITest {

    private GoogleBooksAPI googleBooksAPI;

    @BeforeEach
    void setUp() {
        googleBooksAPI = new GoogleBooksAPI();
    }

    @Test
    void testSearchBookByISBNAsync() {
        CompletableFuture<ArrayList<Book>> booksFuture = GoogleBooksAPI.searchBookByISBNAsync("9780134685991");
        ArrayList<Book> books = booksFuture.join();

        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals("Effective Java", books.get(0).getTitle());
    }

    @Test
    void testSearchBookByTitleAsync() {
        CompletableFuture<ArrayList<Book>> booksFuture = GoogleBooksAPI.searchBookByTitleAsync("Clean Code");
        ArrayList<Book> books = booksFuture.join();

        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals("Clean Code", books.get(0).getTitle());
    }

    @Test
    void testFetchBookDataAsyncNoResults() {
        CompletableFuture<ArrayList<Book>> booksFuture = GoogleBooksAPI.fetchBookDataAsync("isbn:0000000000000");
        ArrayList<Book> books = booksFuture.join();

        assertNull(books);
    }

    @Test
    void testFetchBookDataAsyncInvalidQuery() {
        CompletableFuture<ArrayList<Book>> booksFuture = GoogleBooksAPI.fetchBookDataAsync("!@#$%");
        ArrayList<Book> books = booksFuture.join();

        assertNull(books);
    }
}