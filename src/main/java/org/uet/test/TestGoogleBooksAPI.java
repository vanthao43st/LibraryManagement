package org.uet.test;

import org.uet.service.GoogleBooksAPI;
import org.uet.entity.Document;

import java.util.ArrayList;

public class TestGoogleBooksAPI {
//    private static final GoogleBooksAPI googleBooksAPI = new GoogleBooksAPI();

    private static void searchDocumentByISBN() {
        ArrayList<Document> documents = GoogleBooksAPI.searchBookByISBN("9780134685991");
        for (Document document : documents) {
            System.out.println(document.toString());
        }
    }

    private static void searchDocumentByTitle() {
        ArrayList<Document> documents = GoogleBooksAPI.searchBookByTitle("Artificial Intelligence");
        for (Document document : documents) {
            System.out.println(document.toString());
        }
    }

    public static void main(String[] args) {
//        searchDocumentByISBN();
        searchDocumentByTitle();
    }
}
