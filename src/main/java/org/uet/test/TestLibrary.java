package org.uet.test;

import org.uet.database.dao.LibraryDao;
import org.uet.entity.Library;

import java.util.ArrayList;

public class TestLibrary {
    private static final LibraryDao libraryDao = new LibraryDao();

    private static void getAllLibraryRecord() {
        ArrayList<Library> libraries = libraryDao.getAllLibraryRecords();
        for (Library library : libraries) {
            System.out.println(library.toString());
        }
    }

    private static void borrowDocuments() {
//        if (libraryDao.borrowDocument("14020604", "0001", 19)) {
//            System.out.println("Đã mượn thành công.");
//        } else {
//            System.out.println("Không mượn được.");
//        }
    }

//    private static void returnDocuments() {
//        if (libraryDao.returnDocument("14020604", "0001", 15, "2025-03-02")) {
//            System.out.println("Đã trả thành công.");
//        } else {
//            System.out.println("Không trả được");
//        }
//    }

    private static void deleteLibraryRecord() {
        libraryDao.deleteLibraryRecord();
    }

    public static void main(String[] args) {
//        getAllLibraryRecord();
//        borrowDocuments();
//        returnDocuments();
//        deleteLibraryRecord();
    }
}
