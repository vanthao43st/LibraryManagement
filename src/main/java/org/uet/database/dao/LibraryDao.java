package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Library;
import org.uet.entity.SessionManager;

import java.sql.*;
import java.util.ArrayList;

public class LibraryDao {
    public ArrayList<Library> getAllLibraryRecords() {
        ArrayList<Library> items = new ArrayList<>();
        String query = "SELECT * FROM library";
        Library item;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                item = new Library();
                item.setUserId(resultSet.getString("library_user_id"));
                item.setDocumentCode(resultSet.getString("library_document_code"));
                item.setDocumentType(resultSet.getString("library_document_type"));
                item.setQuantity(resultSet.getInt("library_quantity"));
                item.setBorrowDate(resultSet.getString("library_borrow_date"));
                item.setReturnDate(resultSet.getString("library_return_date"));
                item.setDueDate(resultSet.getString("library_due_date"));
                item.setStatus(resultSet.getString("library_status"));
                item.setLateDays(resultSet.getInt("library_late_days"));
                item.setFine(resultSet.getDouble("library_fine"));

                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return items;
    }

    // Lấy hết các tài liệu đã mượn của user hiện tại
    public ArrayList<Library> getAllLibraryRecordsForUser() {
        ArrayList<Library> items = new ArrayList<>();
        String query = "SELECT l.*, " +
                "CASE " +
                "WHEN l.library_document_type = 'Sách' THEN b.book_title " +
                "WHEN l.library_document_type = 'Luận văn' THEN t.thesis_title " +
                "END AS title, " +
                "CASE " +
                "WHEN l.library_document_type = 'Sách' THEN b.book_description " +
                "WHEN l.library_document_type = 'Luận văn' THEN t.thesis_description " +
                "END AS description " +
                "FROM library l " +
                "LEFT JOIN book b ON l.library_document_code = b.book_code " +
                "LEFT JOIN thesis t ON l.library_document_code = t.thesis_code " +
                "WHERE l.library_user_id = ?";
        Library item;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            //User hiện tại
            ps.setString(1, SessionManager.getInstance().getCurrentUser().getId());

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    item = new Library();
                    item.setDocumentCode(resultSet.getString("library_document_code"));
                    item.setDocumentType(resultSet.getString("library_document_type"));
                    item.setTitle(resultSet.getString("title"));
                    item.setDescription(resultSet.getString("description"));
                    item.setQuantity(resultSet.getInt("library_quantity"));
                    item.setBorrowDate(resultSet.getString("library_borrow_date"));
                    item.setReturnDate(resultSet.getString("library_return_date"));
                    item.setDueDate(resultSet.getString("library_due_date"));
                    item.setStatus(resultSet.getString("library_status"));
                    item.setLateDays(resultSet.getInt("library_late_days"));
                    item.setFine(resultSet.getDouble("library_fine"));

                    items.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi truy vấn database: " + e.getMessage());
        }

        return items;
    }

    public boolean borrowDocument(String userId, String code, String type, int quantity) {
        // Kiểm tra xem tài liệu có phải là sách hoặc luận văn không
        boolean isBook = checkIfBook(code);
//        boolean isThesis = !isBook && checkIfThesis(code);

//        if (!isBook && !isThesis) {
//            System.out.println("Mã không hợp lệ. Không tìm thấy sách hoặc luận văn tương ứng.");
//            return false;
//        }

        // Kiểm tra sự khả dụng của tài liệu trước khi thực hiện mượn
        if (!checkAvailability(code, quantity)) {
            System.out.println("Không đủ tài liệu để mượn.");
            return false;
        }

        // Thêm thông tin mượn vào bảng library
        String insertLibraryQuery = "INSERT INTO library (library_user_id, library_document_code, library_document_type, " +
                "library_quantity, " +
                "library_borrow_date, library_due_date, library_status) " +
                "VALUES (?, ?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 90 DAY), ?)";

        // Cập nhật số lượng tài liệu trong bảng book hoặc thesis
        String updateQuery = isBook
                ? "UPDATE book SET book_quantity = book_quantity - ? WHERE book_code = ?"
                : "UPDATE thesis SET thesis_quantity = thesis_quantity - ? WHERE thesis_code = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement insertLibraryPs = connection.prepareStatement(insertLibraryQuery);
                PreparedStatement updatePs = connection.prepareStatement(updateQuery);
        ) {
            // Thêm thông tin mượn vào bảng library
            insertLibraryPs.setString(1, userId);
            insertLibraryPs.setString(2, code);
            insertLibraryPs.setString(3, type);
            insertLibraryPs.setInt(4, quantity);
            insertLibraryPs.setString(5, "Chưa trả");
            insertLibraryPs.executeUpdate();

            // Cập nhật số lượng tài liệu (sách hoặc luận văn)
            updatePs.setInt(1, quantity);
            updatePs.setString(2, code);
            updatePs.executeUpdate();

            System.out.println("Mượn " + (isBook ? "sách" : "luận văn") + " thành công!");
            return true;
        } catch (Exception e) {
            System.out.println("Đã xảy ra lỗi: " + e.getMessage());
            return false;
        }
    }

    // Kiểm tra số lượng tài liệu hoặc luận văn còn lại
    public boolean checkAvailability(String code, int requestedQuantity) {
        String bookQuery = "SELECT book_quantity FROM book WHERE book_code = ?";
        String thesisQuery = "SELECT thesis_quantity FROM thesis WHERE thesis_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement bookPs = connection.prepareStatement(bookQuery);
             PreparedStatement thesisPs = connection.prepareStatement(thesisQuery)) {

            // Kiểm tra trong bảng tài liệu
            bookPs.setString(1, code);
            ResultSet bookResultSet = bookPs.executeQuery();
            if (bookResultSet.next()) {
                int availableQuantity = bookResultSet.getInt("book_quantity");
                return availableQuantity >= requestedQuantity; // Đủ số lượng để mượn
            }

            // Kiểm tra trong bảng luận văn
            thesisPs.setString(1, code);
            ResultSet thesisResultSet = thesisPs.executeQuery();
            if (thesisResultSet.next()) {
                int availableQuantity = thesisResultSet.getInt("thesis_quantity");
                return availableQuantity >= requestedQuantity; // Đủ số lượng để mượn
            }

            System.out.println("Không tìm thấy sách hoặc luận văn với mã: " + code);
            return false;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String getDocumentType(String documentCode) {
        String sqlBookCheck = "SELECT COUNT(*) FROM book WHERE book_code = ?";
        String sqlThesisCheck = "SELECT COUNT(*) FROM thesis WHERE thesis_code = ?";

        try (Connection connection = DBConnection.getConnection()) {
            // Kiểm tra tài liệu là sách
            try (PreparedStatement ps = connection.prepareStatement(sqlBookCheck)) {
                ps.setString(1, documentCode);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return "Sách";  // Nếu tài liệu tồn tại trong bảng book
                }
            }

            // Kiểm tra tài liệu là luận văn
            try (PreparedStatement ps = connection.prepareStatement(sqlThesisCheck)) {
                ps.setString(1, documentCode);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return "Luận văn";  // Nếu tài liệu tồn tại trong bảng thesis
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean checkIfBook(String code) {
        String query = "SELECT 1 FROM book WHERE book_code = ? LIMIT 1";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean checkIfThesis(String code) {
        String query = "SELECT 1 FROM thesis WHERE thesis_code = ? LIMIT 1";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public boolean isUserExisted(String userId) {
        String query = "SELECT 1 FROM user WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userId);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next(); // Trả về true nếu tìm thấy user_id
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean isCodeExisted(String code) {
        String queryDocument = "SELECT 1 FROM book WHERE book_code = ?";
        String queryThesis = "SELECT 1 FROM thesis WHERE thesis_code = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement psDocument = connection.prepareStatement(queryDocument);
             PreparedStatement psThesis = connection.prepareStatement(queryThesis)) {

            // Check if the code exists in book
            psDocument.setString(1, code);
            ResultSet rsDocument = psDocument.executeQuery();
            if (rsDocument.next()) {
                return true;
            }

            // Check if the code exists in theses
            psThesis.setString(1, code);
            ResultSet rsThesis = psThesis.executeQuery();
            return rsThesis.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean returnDocument(String userId, String code, int returnQuantity, String returnDate) {
        boolean isBook = checkIfBook(code);
        boolean isThesis = !isBook && checkIfThesis(code);

        if (!isBook && !isThesis) {
            System.out.println("Mã không hợp lệ. Không tìm thấy sách hoặc luận văn tương ứng.");
            return false;
        }

        String checkQuery = "SELECT library_quantity, library_due_date FROM library WHERE library_user_id = ? AND library_document_code = ?";

        String updateLibraryQuery = "UPDATE library SET library_quantity = library_quantity - ?, library_return_date = ?, library_status = ?, library_late_days = ?, library_fine = ? " +
                "WHERE library_user_id = ? AND library_document_code = ?";

        String updateQuery = isBook
                ? "UPDATE book SET book_quantity = book_quantity + ? WHERE book_code = ?"
                : "UPDATE thesis SET thesis_quantity = thesis_quantity + ? WHERE thesis_code = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                PreparedStatement updateLibraryStmt = connection.prepareStatement(updateLibraryQuery);
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery)
        ) {
            // Kiểm tra thông tin mượn tài liệu hoặc luận văn
            checkStmt.setString(1, userId);
            checkStmt.setString(2, code);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                int borrowedQuantity = resultSet.getInt("library_quantity");
                Date dueDate = resultSet.getDate("library_due_date");
                Date returnDateObj = Date.valueOf(returnDate);

                // Kiểm tra số lượng trả hợp lệ
                if (returnQuantity > borrowedQuantity) {
                    System.out.println("Số lượng trả vượt quá số lượng mượn.");
                    return false;
                }

                // Tính số ngày muộn và tiền phạt (nếu có)
                long lateDays = 0;
                double fine = 0;
                String status = "Chưa trả";
                if (returnDateObj.after(dueDate)) {
                    long lateMillis = returnDateObj.getTime() - dueDate.getTime();
                    lateDays = lateMillis / (1000 * 60 * 60 * 24); // Tính số ngày muộn

                    // Tính tiền phạt
                    if (lateDays > 0) {
                        if (lateDays <= 10) {
                            fine = lateDays * 1000;
                        } else if (lateDays <= 20) {
                            fine = lateDays * 2000;
                        } else {
                            fine = lateDays * 3000;
                        }
                    }
                    status = "Trả muộn";
                } else if (returnQuantity == borrowedQuantity) {
                    status = "Đã trả";
                }

                updateLibraryStmt.setInt(1, returnQuantity);
                updateLibraryStmt.setDate(2, returnDateObj);
                updateLibraryStmt.setString(3, borrowedQuantity == returnQuantity ? status : "Chưa trả");
                updateLibraryStmt.setLong(4, lateDays);
                updateLibraryStmt.setDouble(5, fine);
                updateLibraryStmt.setString(6, userId);
                updateLibraryStmt.setString(7, code);
                updateLibraryStmt.executeUpdate();

                updateStmt.setInt(1, returnQuantity);
                updateStmt.setString(2, code);
                updateStmt.executeUpdate();

                System.out.println("Trả " + (isBook ? "sách" : "luận văn") + " thành công! Trạng thái: " + status);
                return true;
            } else {
                System.out.println("Không tìm thấy sách hoặc luận văn cần trả hoặc đã được trả.");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void deleteLibraryRecord() {
        String deleteQuery = "DELETE FROM library WHERE library_quantity = 0";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Đã xóa " + rowsAffected + " bản ghi đã trả hết tài liệu.");
            } else {
                System.out.println("Không có bản ghi nào cần xóa.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
