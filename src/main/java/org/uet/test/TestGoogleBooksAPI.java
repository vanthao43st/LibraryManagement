package org.uet.test;

import org.uet.entity.Book;
import org.uet.service.GoogleBooksAPI;
import org.uet.entity.Document;

import java.util.ArrayList;

public class TestGoogleBooksAPI {
    private static void searchDocumentByISBN() {
        ArrayList<Book> books = GoogleBooksAPI.searchBookByISBN("9780134685991");
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }

    private static void searchDocumentByTitle() {
        ArrayList<Book> books = GoogleBooksAPI.searchBookByTitle("Artificial Intelligence");
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }

    public static void main(String[] args) {
//        searchDocumentByISBN();
        searchDocumentByTitle();
    }
}
