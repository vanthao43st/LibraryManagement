package org.uet.entity;

public class Document {
    private String code;
    private String title;
    private String description;
    private String category;
    private String author;
    private double price;
    private int quantity;
    private int index;

    public Document() {
        this.code = "0001";
        this.title = "";
        this.description = "";
        this.category = "";
        this.author = "";
        this.price = 0;
        this.quantity = 0;
        this.index = 0;
    }

    public Document(String code, String title, String description, String category,
                    String author, double price, int quantity) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.category = category;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
        this.index = 0;
    }

    // Getter và Setter cho thuộc tính index
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    // Các getter và setter khác
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

    @Override
    public String toString() {
        return "Document{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", index=" + index +
                '}';
    }
}
