package org.uet.entity;

public class Book extends Document {
    protected String category;
    protected double price;

    public Book() {
        super();
        this.category = "";
        this.price = 0;
    }

    public Book(String code, String title, String description, String author, String category, double price, int quantity) {
        super(code, title, description, author, quantity);
        this.category = category;
        this.price = price;
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

    @Override
    public String toString() {
        return "";
    }
}
