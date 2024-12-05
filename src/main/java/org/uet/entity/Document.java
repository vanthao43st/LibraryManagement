package org.uet.entity;

public abstract class Document {
    private String code;
    private String title;
    private String description;
    private String author;

    public Document() {
        this.code = "";
        this.title = "";
        this.description = "";
        this.author = "";
    }

    public Document(String author, String code, String description, String title) {
        this.author = author;
        this.code = code;
        this.description = description;
        this.title = title;
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

    public abstract String toString();
}
