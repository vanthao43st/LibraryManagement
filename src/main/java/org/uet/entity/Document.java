package org.uet.entity;

public class Document {
    private String code;
    private String title;
    private String category;
    private String author;
    private String publisher;
    private double price;
    private int quantity;

    public Document() {
    }

    public Document(String code, String title, String category, String author, String publisher, double price, int quantity) {
        this.code = code;
        this.title = title;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
