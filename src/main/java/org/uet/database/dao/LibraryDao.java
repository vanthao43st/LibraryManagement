package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Library;
import org.uet.entity.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class LibraryDao {
    public CompletableFuture<ArrayList<Library>> getAllLibraryRecordsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<Library> items = new ArrayList<>();
            String query = "SELECT * FROM library";
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    Library item = new Library();
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
        });
    }

    public CompletableFuture<ArrayList<Library>> getAllLibraryRecordsForUserAsync() {
        return CompletableFuture.supplyAsync(() -> {
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

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, SessionManager.getInstance().getCurrentUser().getId());

                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        Library item = new Library();
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
        });
    }

    public CompletableFuture<Boolean> borrowDocumentAsync(String userId, String code, String type, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            boolean isBook = checkIfBookAsync(code).join();
            if (!checkAvailabilityAsync(code, quantity).join()) {
                System.out.println("Không đủ tài liệu để mượn.");
                return false;
            }

            String insertLibraryQuery = "INSERT INTO library (library_user_id, library_document_code, library_document_type, " +
                    "library_quantity, library_borrow_date, library_due_date, library_status) " +
                    "VALUES (?, ?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 90 DAY), ?)";

            String updateQuery = isBook
                    ? "UPDATE book SET book_quantity = book_quantity - ? WHERE book_code = ?"
                    : "UPDATE thesis SET thesis_quantity = thesis_quantity - ? WHERE thesis_code = ?";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement insertLibraryPs = connection.prepareStatement(insertLibraryQuery);
                 PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {
                insertLibraryPs.setString(1, userId);
                insertLibraryPs.setString(2, code);
                insertLibraryPs.setString(3, type);
                insertLibraryPs.setInt(4, quantity);
                insertLibraryPs.setString(5, "Chưa trả");
                insertLibraryPs.executeUpdate();

                updatePs.setInt(1, quantity);
                updatePs.setString(2, code);
                updatePs.executeUpdate();

                System.out.println("Mượn " + (isBook ? "sách" : "luận văn") + " thành công!");
                return true;
            } catch (Exception e) {
                System.out.println("Đã xảy ra lỗi: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> returnDocumentAsync(String userId, String code, int returnQuantity, String returnDate, int libraryId) {
        return CompletableFuture.supplyAsync(() -> {
            boolean isBook = checkIfBookAsync(code).join();

            String checkQuery = "SELECT library_quantity, library_due_date FROM library " +
                    "WHERE library_user_id = ? AND library_document_code = ? AND library_id = ?";

            String updateLibraryQuery = "UPDATE library SET library_quantity = library_quantity - ?, library_return_date = ?, library_status = ?, library_late_days = ?, library_fine = ? " +
                    "WHERE library_id = ?";

            String updateDocumentQuery = isBook
                    ? "UPDATE book SET book_quantity = book_quantity + ? WHERE book_code = ?"
                    : "UPDATE thesis SET thesis_quantity = thesis_quantity + ? WHERE thesis_code = ?";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                 PreparedStatement updateLibraryStmt = connection.prepareStatement(updateLibraryQuery);
                 PreparedStatement updateDocumentStmt = connection.prepareStatement(updateDocumentQuery)) {

                checkStmt.setString(1, userId);
                checkStmt.setString(2, code);
                checkStmt.setInt(3, libraryId);
                ResultSet resultSet = checkStmt.executeQuery();

                if (resultSet.next()) {
                    int borrowedQuantity = resultSet.getInt("library_quantity");
                    Date dueDate = resultSet.getDate("library_due_date");
                    Date returnDateObj = Date.valueOf(returnDate);

                    long lateDays = 0;
                    double fine = 0;
                    String status = "Đã trả";

                    if (returnDateObj.after(dueDate)) {
                        long lateMillis = returnDateObj.getTime() - dueDate.getTime();
                        lateDays = lateMillis / (1000 * 60 * 60 * 24);

                        if (lateDays > 0) {
                            fine = lateDays <= 10 ? lateDays * 1000 : lateDays <= 20 ? lateDays * 2000 : lateDays * 3000;
                            status = "Trả muộn";
                        }
                    }

                    updateLibraryStmt.setInt(1, returnQuantity);
                    updateLibraryStmt.setDate(2, returnDateObj);
                    updateLibraryStmt.setString(3, borrowedQuantity == returnQuantity ? status : "Chưa trả");
                    updateLibraryStmt.setLong(4, lateDays);
                    updateLibraryStmt.setDouble(5, fine);
                    updateLibraryStmt.setInt(6, libraryId);
                    updateLibraryStmt.executeUpdate();

                    updateDocumentStmt.setInt(1, returnQuantity);
                    updateDocumentStmt.setString(2, code);
                    updateDocumentStmt.executeUpdate();

                    System.out.println("Trả " + (isBook ? "sách" : "luận văn") + " thành công! Trạng thái: " + status);
                    return true;
                } else {
                    System.out.println("Không tìm thấy bản ghi hoặc số lượng không hợp lệ.");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi trả tài liệu: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Void> deleteLibraryRecordAsync() {
        return CompletableFuture.runAsync(() -> {
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
        });
    }

    public CompletableFuture<Boolean> checkIfBookAsync(String code) {
        return CompletableFuture.supplyAsync(() -> {
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
        });
    }

    public CompletableFuture<Boolean> checkAvailabilityAsync(String code, int requestedQuantity) {
        return CompletableFuture.supplyAsync(() -> {
            String bookQuery = "SELECT book_quantity FROM book WHERE book_code = ?";
            String thesisQuery = "SELECT thesis_quantity FROM thesis WHERE thesis_code = ?";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement bookPs = connection.prepareStatement(bookQuery);
                 PreparedStatement thesisPs = connection.prepareStatement(thesisQuery)) {

                bookPs.setString(1, code);
                ResultSet bookResultSet = bookPs.executeQuery();
                if (bookResultSet.next()) {
                    int availableQuantity = bookResultSet.getInt("book_quantity");
                    return availableQuantity >= requestedQuantity;
                }

                thesisPs.setString(1, code);
                ResultSet thesisResultSet = thesisPs.executeQuery();
                if (thesisResultSet.next()) {
                    int availableQuantity = thesisResultSet.getInt("thesis_quantity");
                    return availableQuantity >= requestedQuantity;
                }

                System.out.println("Không tìm thấy sách hoặc luận văn với mã: " + code);
                return false;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> isUserExistedAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT 1 FROM user WHERE user_id = ?";
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, userId);
                ResultSet resultSet = ps.executeQuery();
                return resultSet.next();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> isDocumentCodeExistedAsync(String code) {
        return CompletableFuture.supplyAsync(() -> {
            String queryBook = "SELECT 1 FROM book WHERE book_code = ?";
            String queryThesis = "SELECT 1 FROM thesis WHERE thesis_code = ?";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement psBook = connection.prepareStatement(queryBook);
                 PreparedStatement psThesis = connection.prepareStatement(queryThesis)) {

                psBook.setString(1, code);
                ResultSet rsBook = psBook.executeQuery();
                if (rsBook.next()) {
                    return true;
                }

                psThesis.setString(1, code);
                ResultSet rsThesis = psThesis.executeQuery();
                return rsThesis.next();

            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Integer> getLibraryIdAsync(String userId, String documentCode, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT library_id FROM library WHERE library_user_id = ? AND library_document_code = ? AND library_quantity = ?";
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, userId);
                ps.setString(2, documentCode);
                ps.setInt(3, quantity);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("library_id");
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi lấy library_id: " + e.getMessage());
            }
            return null;
        });
    }}