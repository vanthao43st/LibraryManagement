package org.uet.database.dao;

import org.uet.database.connection.DBConnection;
import org.uet.entity.Library;

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

//    public void addLibraryRecord(Library library) {
//        String query = "INSERT INTO library (library_user_id, library_document_code, library_quantity, " +
//                "library_borrow_date, library_return_date, library_due_date, library_status) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//        try (Connection connection = DBConnection.getConnection();
//             PreparedStatement ps = connection.prepareStatement(query)) {
//
//            ps.setString(1, library.getUserId());
//            ps.setString(2, library.getDocumentCode());
//            ps.setInt(3, library.getQuantity());
//            ps.setString(4, library.getBorrowDate());
//            ps.setString(5, library.getReturnDate());
//            ps.setString(6, library.getDueDate());
//            ps.setString(7, library.getStatus());
//
//            int rowsAffected = ps.executeUpdate();
//            System.out.println(rowsAffected + " row(s) inserted.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    // Kiểm tra số lượng tài liệu còn lại
    public boolean checkAvailability(String documentCode, int requestedQuantity) {
        String query = "SELECT document_quantity FROM document WHERE document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, documentCode);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                int availableQuantity = resultSet.getInt("document_quantity");
                return availableQuantity >= requestedQuantity; // Đủ số lượng để mượn
            } else {
                System.out.println("Không tìm thấy tài liệu với mã: " + documentCode);
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean borrowDocument(String userId, String documentCode, int quantity) {
        if (!checkAvailability(documentCode, quantity)) {
            System.out.println("Không đủ tài liệu để mượn.");
            return false;
        }

        // Thực hiện mượn tài liệu
        String updateDocumentQuery = "UPDATE document SET document_quantity = document_quantity - ? WHERE document_code = ?";
        String insertLibraryQuery = "INSERT INTO library (library_user_id, library_document_code, library_quantity, " +
                "library_borrow_date, library_due_date, library_status) " +
                "VALUES (?, ?, ?, CURDATE(), CURDATE() + INTERVAL 90 DAY, ?)";
        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement updateDocumentPs = connection.prepareStatement(updateDocumentQuery);
                PreparedStatement insertLibraryPs = connection.prepareStatement(insertLibraryQuery)
        ) {
            // Cập nhật số lượng tài liệu
            updateDocumentPs.setInt(1, quantity);
            updateDocumentPs.setString(2, documentCode);
            updateDocumentPs.executeUpdate();

            // Thêm thông tin mượn tài liệu
            insertLibraryPs.setString(1, userId);
            insertLibraryPs.setString(2, documentCode);
            insertLibraryPs.setInt(3, quantity);
            insertLibraryPs.setString(4, "Chưa trả");
            insertLibraryPs.executeUpdate();

            System.out.println("Mượn tài liệu thành công!");
            return true;
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

    public boolean isDocumentExisted(String documentCode) {
        String query = "SELECT 1 FROM document WHERE document_code = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, documentCode);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next(); // Trả về true nếu tìm thấy document_code
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean returnDocument(String userId, String documentCode, int returnQuantity, String returnDate) {
        String checkQuery = "SELECT library_quantity, library_due_date FROM library WHERE library_user_id = ? AND library_document_code = ?";
        String updateLibraryQuery = "UPDATE library SET library_quantity = library_quantity - ?, library_return_date = ?, library_status = ?, library_late_days = ?, library_fine = ? " +
                "WHERE library_user_id = ? AND library_document_code = ?";
        String updateDocumentQuery = "UPDATE document SET document_quantity = document_quantity + ? WHERE document_code = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                PreparedStatement updateLibraryStmt = connection.prepareStatement(updateLibraryQuery);
                PreparedStatement updateDocumentStmt = connection.prepareStatement(updateDocumentQuery)
        ) {
            // Kiểm tra thông tin mượn tài liệu
            checkStmt.setString(1, userId);
            checkStmt.setString(2, documentCode);
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
                updateLibraryStmt.setString(7, documentCode);
                updateLibraryStmt.executeUpdate();

                updateDocumentStmt.setInt(1, returnQuantity);
                updateDocumentStmt.setString(2, documentCode);
                updateDocumentStmt.executeUpdate();

                System.out.println("Trả tài liệu thành công! Trạng thái: " + status);
                return true;
            } else {
                System.out.println("Không tìm thấy tài liệu cần trả hoặc tài liệu đã được trả.");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void updateLibraryRecord(Library library) {
        String query = "UPDATE library SET library_quantity = ?, library_borrow_date = ?, library_return_date = ?, " +
                "library_due_date = ?, library_status = ? WHERE library_user_id = ? AND library_document_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, library.getQuantity());
            ps.setString(2, library.getBorrowDate());
            ps.setString(3, library.getReturnDate());
            ps.setString(4, library.getDueDate());
            ps.setString(5, library.getStatus());
            ps.setString(6, library.getUserId());
            ps.setString(7, library.getDocumentCode());

            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
