package org.uet.entity;

public class Library {
    private String userId;
    private String documentCode;
    private String documentType;
    private int quantity;
    private String borrowDate;
    private String returnDate;
    private String dueDate;
    private String status;
    private int lateDays;
    private double fine;

    public Library() {
        this.userId = "21010938";
        this.documentCode = "0001";
        this.documentType = "Sách";
        this.quantity = 0;
        this.borrowDate = "";
        this.returnDate = "";
        this.dueDate = "";
        this.status = "";
        this.lateDays = 0;
        this.fine = 0;
    }

    public Library(String userId, String documentCode, String documentType, int quantity, String borrowDate,
                   String returnDate, String dueDate, String status) {
        this.userId = userId;
        this.documentCode = documentCode;
        this.documentType = documentType;
        this.quantity = quantity;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLateDays() {
        return lateDays;
    }

    public void setLateDays(int lateDays) {
        this.lateDays = lateDays;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    private void checkStatus(String status) {
        if ("Trả muộn".equals(status)
                && this.returnDate != null && !this.returnDate.isEmpty()
                && this.dueDate != null && !this.dueDate.isEmpty()) {

            // Chuyển ngày mượn, ngày trả và ngày đến hạn thành kiểu Date
            try {
                long dueDateMillis = new java.text.SimpleDateFormat("yyyy-MM-dd")
                        .parse(this.dueDate).getTime();
                long returnDateMillis = new java.text.SimpleDateFormat("yyyy-MM-dd")
                        .parse(this.returnDate).getTime();

                // Tính số ngày muộn
                long lateMillis = returnDateMillis - dueDateMillis;
                this.lateDays = (int) (lateMillis / (1000 * 60 * 60 * 24));  // Chuyển millis thành day

                // Tính tiền phạt
                if (this.lateDays > 0) {
                    if (this.lateDays <= 10) {
                        this.fine = this.lateDays * 1000;
                    } else if (this.lateDays <= 20) {
                        this.fine = this.lateDays * 2000;
                    } else {
                        this.fine = this.lateDays * 3000;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            this.lateDays = 0;
            this.fine = 0;
        }
    }

    @Override
    public String toString() {
        return "{userId='" + userId + '\'' +
                ", documentCode='" + documentCode + '\'' +
                ", quantity=" + quantity +
                ", borrowDate='" + borrowDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", status='" + status + '\'' +
                ", lateDays=" + lateDays +

                ", fine=" + fine +
                '}';
    }
}
