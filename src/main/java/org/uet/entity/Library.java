package org.uet.entity;

public class Library {
    protected String userId;
    protected String documentCode;
    protected String documentType;
    protected int quantity;
    protected String borrowDate;
    protected String returnDate;
    protected String dueDate;
    protected String status;
    protected int lateDays;
    protected double fine;
    protected String title;
    protected String description;

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
        this.title = "";
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

    public Library(String documentCode, String documentType, String title, String description, int quantity, String borrowDate,
                   String dueDate, String status) {
        this.documentCode = documentCode;
        this.documentType = documentType;
        this.title = title;
        this.description = description;
        this.quantity = quantity;
        this.borrowDate = borrowDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Library{" +
                "borrowDate='" + borrowDate + '\'' +
                ", userId='" + userId + '\'' +
                ", documentCode='" + documentCode + '\'' +
                ", documentType='" + documentType + '\'' +
                ", quantity=" + quantity +
                ", returnDate='" + returnDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", status='" + status + '\'' +
                ", lateDays=" + lateDays +
                ", fine=" + fine +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
