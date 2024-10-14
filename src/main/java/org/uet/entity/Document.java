package org.uet.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Document {
    private String code;
    private String title;
    private String category;
    private String author;
    private String publisher;
    private double price;
    private int quantity;
}
