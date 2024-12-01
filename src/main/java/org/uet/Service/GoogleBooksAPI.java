package org.uet.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.uet.entity.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class GoogleBooksAPI {
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    // Tìm kiếm tài liệu bằng ISBN
    public static ArrayList<Document> searchBookByISBN(String isbn) {
        return fetchBookData("isbn:" + isbn);
    }

    // Tìm kiếm tài liệu bằng tiêu đề
    public static ArrayList<Document> searchBookByTitle(String title) {
        return fetchBookData("intitle:" + title);
    }

    private static ArrayList<Document> fetchBookData(String query) {
        ArrayList<Document> documents = new ArrayList<>();

        try {
            // Mã hóa URL
            String queryEncoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String urlStr = API_URL + queryEncoded;
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Kiểm tra mã phản hồi HTTP
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Lỗi từ API: HTTP " + responseCode);
                return null;
            }

            // Đọc kết quả
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

//            System.out.println(response);

            // Xử lý JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.toString());

//            System.out.println(jsonNode);

            // Kiểm tra kết quả trả về
            if (!jsonNode.has("items") || jsonNode.get("items").isEmpty()) {
                System.out.println("Không tìm thấy kết quả cho query: " + query);
                return null;
            }

            for (JsonNode item : jsonNode.get("items")) {
                JsonNode volumeInfo = item.get("volumeInfo");

                String id = item.has("id") ? item.get("id").asText() : UUID.randomUUID().toString();
                String title = volumeInfo.has("title") ? volumeInfo.get("title").asText() : "Unknown";

                String author = "Unknown";
                if (volumeInfo.has("authors")) {
                    author = volumeInfo.get("authors").get(0).asText();
                }

                String category = "Unknown";
                if (volumeInfo.has("categories")) {
                    category = volumeInfo.get("categories").get(0).asText();
                }

                String description = "Unknown";
                if (volumeInfo.has("description")) {
                    description = volumeInfo.get("description").asText();
                }

                documents.add(new Document(id, title, description, category, author, 50000, 10));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        return documents;
    }
}

