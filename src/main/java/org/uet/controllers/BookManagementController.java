package org.uet.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import org.uet.entity.Document;

public class BookManagementController {
    @FXML
    private TableView<Document> documentTable;

    @FXML
    private TableColumn<Document, Integer> colIndex;

    @FXML
    private TableColumn<Document, String> colCode;

    @FXML
    private TableColumn<Document, String> colTitle;

    @FXML
    private TableColumn<Document, String> colDescription;

    @FXML
    private TableColumn<Document, String> colCategory;

    @FXML
    private TableColumn<Document, String> colAuthor;

    @FXML
    private TableColumn<Document, Double> colPrice;

    @FXML
    private TableColumn<Document, Integer> colQuantity;

    @FXML
    private TextField txtCode, txtTitle, txtCategory, txtAuthor, txtPrice, txtQuantity;
    @FXML
    private TextField searchTitle;

    private ObservableList<Document> documentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colIndex.setCellValueFactory(new PropertyValueFactory<>("index"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        documentTable.setItems(documentList);
        loadDocumentList();
    }

    private void loadDocumentList() {
        try (InputStream is = getClass().getResourceAsStream("/data/document.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            documentList.clear();
            int index = 1;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 7 && !values[0].equals("Code")) {
                    Document doc = new Document(values[0], values[1], values[2],
                            values[3], values[4], Double.parseDouble(values[5]), Integer.parseInt(values[6]));
                    doc.setIndex(index++);
                    documentList.add(doc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void searchBooks() {
        ObservableList<Document> filteredList = FXCollections.observableArrayList();
        String title = searchTitle.getText().toLowerCase();

        for (Document document : documentList) {
            if (title.isEmpty() || document.getTitle().toLowerCase().contains(title)) {
                filteredList.add(document);
            }
        }
        documentTable.setItems(filteredList);
    }

    @FXML
    public void sortList() {
        ObservableList<Document> filteredList = FXCollections.observableArrayList();

        String code = txtCode.getText().toLowerCase();
        String title = txtTitle.getText().toLowerCase();
        String category = txtCategory.getText().toLowerCase();
        String author = txtAuthor.getText().toLowerCase();
        String price = txtPrice.getText().toLowerCase();
        String quantity = txtQuantity.getText().toLowerCase();

        int index = 1;

        for (Document document : documentList) {
            if ((code.isEmpty() || document.getCode().toLowerCase().contains(code))
                    && (title.isEmpty() || document.getTitle().toLowerCase().contains(title))
                    && (category.isEmpty() || document.getCategory().toLowerCase().contains(category))
                    && (author.isEmpty() || document.getAuthor().toLowerCase().contains(author))
                    && (price.isEmpty() || String.valueOf(document.getPrice()).toLowerCase().contains(price))
                    && (quantity.isEmpty() || String.valueOf(document.getQuantity()).toLowerCase().contains(quantity))) {
                document.setIndex(index++);
                filteredList.add(document);
            }
        }
        documentTable.setItems(filteredList);
    }
}
