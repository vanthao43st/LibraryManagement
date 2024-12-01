package org.uet.test;

import org.uet.database.dao.DocumentDao;
import org.uet.entity.Document;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestDocument {
    private static final DocumentDao documentDao = new DocumentDao();

    private static void getAllDocument() {
        ArrayList<Document> documents = documentDao.getAllDocument();
        for (Document document : documents) {
            System.out.println(document.toString());
        }
    }

    private static void addDocument() throws SQLException {
//        Document document = new Document("0013", "Bài tập Vật lý đại cương tập 1", 25000, 150);
//        documentDao.addDocument(document);
    }

    private static void updateDocument() throws SQLException {
        Document document = new Document("0001", "Bài tập đại số tuyến tính",
                "Ôn tập lý thuyết và thực hành thông qua các bài tập về đại số",
                "Toán học", "Dennis Ritchie", 50000, 20);
        documentDao.updateDocument(document);
    }

    private static void deleteDocument() {
        documentDao.deleteDocument("0005");
    }

    private static void searchDocuments() {
        ArrayList<Document> documents = documentDao.searchDocuments("j", null, null);
        for (Document document : documents) {
            System.out.println(document.toString());
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
