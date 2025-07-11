/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;

import Model.Author;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * API Client để tương tác với các endpoint liên quan đến Tác giả (Author).
 * Kế thừa từ ApiClientBase để xử lý các yêu cầu HTTP cơ bản.
 */
public class AuthorApiClient extends ApiClientBase {

    private static final String TAC_GIA_ENDPOINT = "/authors"; // Endpoint cho Tác giả

    public AuthorApiClient() {
        super(); // Gọi constructor của lớp cha để khởi tạo client và objectMapper
    }

    /**
     * Lấy tất cả Tác giả từ API.
     * @return Danh sách các đối tượng Author, hoặc danh sách rỗng nếu có lỗi.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendGetRequest.
     */
    public List<Author> getAllAuthors() throws IOException {
        String jsonResponse = sendGetRequest(TAC_GIA_ENDPOINT); // Gọi sendGetRequest từ ApiClientBase
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Author>>() {});
            } catch (IOException e) {
                Logger.getLogger(AuthorApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON cho Authors", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return Collections.emptyList();
    }
    
    /**
     * Lấy tất cả tên Tác giả từ API (được sử dụng cho ComboBox).
     * @return Danh sách các tên Tác giả, hoặc danh sách rỗng nếu có lỗi.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendGetRequest.
     */
    public List<String> getAllTacGiaNames() throws IOException {
        // Giả định có endpoint /tac_gia/names trên backend trả về List<String>
        String jsonResponse = sendGetRequest(TAC_GIA_ENDPOINT + "/names"); 
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, new TypeReference<List<String>>() {});
            } catch (IOException e) {
                Logger.getLogger(AuthorApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON tên Tác giả", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return Collections.emptyList();
    }

    /**
     * Lấy một Tác giả bằng mã Tác giả.
     * @param maTG Mã Tác giả cần lấy (kiểu String).
     * @return Đối tượng Author nếu tìm thấy, hoặc null nếu không tìm thấy/lỗi.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendGetRequest.
     */
    public Author getAuthorById(String maTG) throws IOException { // Đảm bảo maTG là String
        String jsonResponse = sendGetRequest(TAC_GIA_ENDPOINT + "/" + maTG); // Gọi sendGetRequest
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, Author.class);
            } catch (IOException e) {
                Logger.getLogger(AuthorApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON cho Author ID " + maTG, e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }

    /**
     * Thêm một Tác giả mới vào hệ thống.
     * @param author Đối tượng Author cần thêm (maTG có thể null nếu backend tự sinh).
     * @return Đối tượng Author đã được thêm (có thể bao gồm maTG được sinh bởi backend), hoặc null nếu thất bại.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendPostRequest.
     */
    public Author addAuthor(Author author) throws IOException {
        // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
        String jsonRequest = objectMapper.writeValueAsString(author);
        String jsonResponse = sendPostRequest(TAC_GIA_ENDPOINT, jsonRequest); // Gọi sendPostRequest
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, Author.class);
            } catch (IOException e) {
                Logger.getLogger(AuthorApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON khi thêm Author", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }

    /**
     * Cập nhật thông tin một Tác giả.
     * @param maTG Mã Tác giả của đối tượng cần cập nhật (kiểu String).
     * @param author Đối tượng Author chứa thông tin cập nhật.
     * @return Đối tượng Author đã được cập nhật, hoặc null nếu thất bại.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendPutRequest.
     * @throws IllegalArgumentException Nếu mã Tác giả là null.
     */
    public Author updateAuthor(String maTG, Author author) throws IOException {
        if (maTG == null || maTG.trim().isEmpty()) { // Kiểm tra maTG không null/rỗng
            throw new IllegalArgumentException("Mã Tác giả không được để trống khi cập nhật.");
        }
        // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
        String jsonRequest = objectMapper.writeValueAsString(author);
        String jsonResponse = sendPutRequest(TAC_GIA_ENDPOINT + "/" + maTG, jsonRequest); // Gọi sendPutRequest
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, Author.class);
            } catch (IOException e) {
                Logger.getLogger(AuthorApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON khi cập nhật Author", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }

    /**
     * Xóa một Tác giả khỏi hệ thống.
     * @param maTG Mã Tác giả của đối tượng cần xóa (kiểu String).
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendDeleteRequest.
     */
    public void deleteAuthor(String maTG) throws IOException {
        sendDeleteRequest(TAC_GIA_ENDPOINT + "/" + maTG); // Gọi sendDeleteRequest
    }

    public List<Author> searchAuthors(String keyword) throws IOException {
        String path = "/authors/search?keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String json = sendGetRequest(path);
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(json, Author[].class)); // dùng json thay vì response
    }


}