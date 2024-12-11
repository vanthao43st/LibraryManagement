package org.uet.entity;

public abstract class Document {
    private String code;
    private String title;
    private String description;
    private String author;
    private int quantity;

    public Document() {
        this.code = "";
        this.title = "";
        this.description = "";
        this.author = "";
        this.quantity = 0;
    }

    public Document(String code, String title, String description, String author, int quantity) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.author = author;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public abstract String toString();
}
