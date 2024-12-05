package org.uet.test;

import org.uet.database.dao.BookDao;
import org.uet.entity.Book;
import org.uet.entity.Document;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestDocument {
    private static final BookDao BOOK_DAO = new BookDao();

    private static void getAllDocument() {
        ArrayList<Book> books = BOOK_DAO.getAllBook();
        for (Book book : books) {
            System.out.println(book.toString());
        }
    }

    private static void addDocument() throws SQLException {
//        Document document = new Document("0013", "Bài tập Vật lý đại cương tập 1", 25000, 150);
//        documentDao.addDocument(document);
    }

    private static void updateDocument() throws SQLException {
        Book book = new Book("0001", "Bài tập đại số tuyến tính",
                "Ôn tập lý thuyết và thực hành thông qua các bài tập về đại số",
                "Toán học", "Dennis Ritchie", 50000, 20);
        BOOK_DAO.updateBook(book);
    }

    private static void deleteDocument() {
        BOOK_DAO.deleteBook("0005");
    }

    private static void searchDocuments() {
        ArrayList<Book> books = BOOK_DAO.searchBooks("j", null, null);
        for (Book book : books) {
            System.out.println(books.toString());
        }
    }

    public static void main(String[] args) throws SQLException {
//        getAllDocument();
//        addDocument();
        updateDocument();
//        deleteDocument();
//        searchDocuments();
    }
}
