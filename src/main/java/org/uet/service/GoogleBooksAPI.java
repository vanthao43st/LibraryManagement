package org.uet.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.uet.entity.Book;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GoogleBooksAPI {
    protected static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    public static CompletableFuture<ArrayList<Book>> searchBookByISBNAsync(String isbn) {
        return fetchBookDataAsync("isbn:" + isbn);
    }

    public static CompletableFuture<ArrayList<Book>> searchBookByTitleAsync(String title) {
        return fetchBookDataAsync("intitle:" + title);
    }

    protected static CompletableFuture<ArrayList<Book>> fetchBookDataAsync(String query) {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<Book> books = new ArrayList<>();
            try {
                String queryEncoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
                String urlStr = API_URL + queryEncoded;
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    System.out.println("API Error: HTTP " + responseCode);
                    return null;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());

                if (!jsonNode.has("items") || jsonNode.get("items").isEmpty()) {
                    System.out.println("No results found for query: " + query);
                    return null;
                }

                for (JsonNode item : jsonNode.get("items")) {
                    JsonNode volumeInfo = item.get("volumeInfo");
                    JsonNode saleInfo = item.get("saleInfo");

                    String id = item.has("id") ? item.get("id").asText() : UUID.randomUUID().toString();
                    String title = volumeInfo.has("title") ? volumeInfo.get("title").asText() : "Unknown";
                    String author = volumeInfo.has("authors") ? volumeInfo.get("authors").get(0).asText() : "Unknown";
                    String category = volumeInfo.has("categories") ? volumeInfo.get("categories").get(0).asText() : "Unknown";
                    String description = volumeInfo.has("description") ? volumeInfo.get("description").asText() : "Unknown";

                    long price = 50000; // Giá mặc định nếu không có thông tin
                    if (saleInfo != null && saleInfo.has("retailPrice")) {
                        JsonNode retailPriceNode = saleInfo.get("retailPrice");
                        if (retailPriceNode.has("amount")) {
                            price = retailPriceNode.get("amount").asLong();
                        }
                    }

                    books.add(new Book(id, title, description, author, category, price, 10));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
            return books;
        });
    }
}
