/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;

import Entity.Book;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class BookApiClient extends ApiClientBase {
private static final String BOOKS_API_PATH = "/books"; 
    // Constructor kế thừa từ ApiClientBase
    public BookApiClient() {
        super();
    }

    public List<Book> getAllBooks() throws IOException {
        String jsonResponse = sendGetRequest(BOOKS_API_PATH); // <-- Gọi sendGetRequest từ ApiClientBase
        // Sử dụng objectMapper từ ApiClientBase để đọc JSON thành mảng, sau đó chuyển sang List
        Book[] booksArray = objectMapper.readValue(jsonResponse, Book[].class);
        return Arrays.asList(booksArray);
    }

    /**
     * Lấy sách theo ID từ API.
     *
     * @param id ID của sách cần lấy.
     * @return Đối tượng Book.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public Book getBookById(String id) throws IOException {
        String jsonResponse = sendGetRequest(BOOKS_API_PATH + "/" + id); // <-- Gọi sendGetRequest từ ApiClientBase
        return objectMapper.readValue(jsonResponse, Book.class);
    }

   
    public Book addBook(Book bookDto) throws IOException { // <-- THAY ĐỔI THAM SỐ
        String jsonInputString = objectMapper.writeValueAsString(bookDto); // <-- CHUYỂN BOOKDTO THÀNH JSON
        String jsonResponse = sendPostRequest(BOOKS_API_PATH, jsonInputString);
        return objectMapper.readValue(jsonResponse, Book.class);
    }

     public Book updateBook(String id, Book bookDto) throws IOException { // <-- THAY ĐỔI THAM SỐ
        if (id == null || id.trim().isEmpty()) { // Kiểm tra ID từ tham số
            throw new IllegalArgumentException("ID sách không được để trống khi cập nhật.");
        }
        System.out.println("PUT URL: " + BOOKS_API_PATH + "/" + id + "  (len=" + id.length() + ")");

        String jsonInputString = objectMapper.writeValueAsString(bookDto); // <-- CHUYỂN BOOKDTO THÀNH JSON
        String jsonResponse = sendPutRequest(BOOKS_API_PATH + "/" + id, jsonInputString);
        return objectMapper.readValue(jsonResponse, Book.class);
    }

    /**
     * Xóa một sách khỏi API.
     *
     * @param bookId ID của sách cần xóa.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public void deleteBook(String bookId) throws IOException {
        sendDeleteRequest(BOOKS_API_PATH + "/" + bookId); 
    }

    /**
     * Lấy sách theo mã danh mục.
     *
     * @param categoryId Mã danh mục.
     * @return Danh sách các đối tượng Book.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public List<Book> getBooksByCategoryId(Long categoryId) throws IOException {
        String jsonResponse = sendGetRequest(BOOKS_API_PATH + "/categories/" + categoryId); // <-- Gọi sendGetRequest từ ApiClientBase
        Book[] booksArray = objectMapper.readValue(jsonResponse, Book[].class);
        return Arrays.asList(booksArray);
    }

    /**
     * Tìm kiếm sách theo từ khóa.
     *
     * @param query Từ khóa tìm kiếm.
     * @return Danh sách các đối tượng Book.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public List<Book> searchBooks(String query) throws IOException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        String jsonResponse = sendGetRequest(BOOKS_API_PATH + "/search?query=" + encodedQuery); // <-- Gọi sendGetRequest từ ApiClientBase
        Book[] booksArray = objectMapper.readValue(jsonResponse, Book[].class);
        return Arrays.asList(booksArray);
    }
}
