package org.uet.entity;

public class Book extends Document{
    private String category;
    private double price;
    private int quantity;

    public Book() {
        super();
        this.category = "";
        this.price = 0;
        this.quantity = 0;
    }

    public Book(String code, String title, String description, String author, String category, double price, int quantity) {
        super(code, title, description, author);
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        return "";
    }
}
