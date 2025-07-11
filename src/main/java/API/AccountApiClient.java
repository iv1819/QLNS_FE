/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;
import Model.Account; // Import Model.Account (frontend)
import Model.AccountDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Client để tương tác với các endpoint liên quan đến Account ở Backend.
 * Kế thừa ApiClientBase để sử dụng các phương thức gửi request chung.
 */
public class AccountApiClient extends ApiClientBase {

    private static final String ACCOUNTS_API_PATH = "/accounts"; // Base path cho Accounts Controller

    public AccountApiClient() {
        super(); // Gọi constructor của lớp cha để khởi tạo OkHttpClient và ObjectMapper
    }

    /**
     * Gửi yêu cầu đăng ký tài khoản mới.
     * @param account Đối tượng Account chứa thông tin đăng ký.
     * @return Đối tượng Account đã được đăng ký (có thể có thêm thông tin từ server).
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public Account registerAccount(Account account) throws IOException {
        String jsonRequest = objectMapper.writeValueAsString(account);
        String jsonResponse = sendPostRequest(ACCOUNTS_API_PATH + "/register", jsonRequest);
        return objectMapper.readValue(jsonResponse, Account.class);
    }

    /**
     * Gửi yêu cầu đăng nhập.
     * @param taiKhoan Tên tài khoản.
     * @param matKhau Mật khẩu.
     * @return Map chứa phản hồi từ server (ví dụ: success, role, message).
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public Map<String, Object> login(String taiKhoan, String matKhau) throws IOException {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("taiKhoan", taiKhoan);
        requestBody.put("matKhau", matKhau);
        String jsonRequest = objectMapper.writeValueAsString(requestBody);
        
        // Endpoint cho login là /accounts/login
        String jsonResponse = sendPostRequest(ACCOUNTS_API_PATH + "/login", jsonRequest);
        
        // Phản hồi của login là Map<String, Object> (success, message, role, tennv)
        return objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});
    }
    
    /**
     * Lấy tất cả tài khoản từ API.
     * @return Danh sách các đối tượng Account.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public List<Account> getAllAccounts() throws IOException {
        String jsonResponse = sendGetRequest(ACCOUNTS_API_PATH);
        return objectMapper.readValue(jsonResponse, new TypeReference<List<Account>>() {});
    }

    /**
     * Lấy tài khoản theo tên tài khoản.
     * @param taiKhoan Tên tài khoản.
     * @return Đối tượng Account.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public Account getAccountByTaiKhoan(String taiKhoan) throws IOException {
        String jsonResponse = sendGetRequest(ACCOUNTS_API_PATH + "/" + taiKhoan);
        return objectMapper.readValue(jsonResponse, Account.class);
    }

    /**
     * Cập nhật thông tin tài khoản.
     * @param taiKhoan Tên tài khoản cần cập nhật.
     * @param account Đối tượng Account chứa thông tin cập nhật.
     * @return Đối tượng Account đã được cập nhật.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public Account updateAccount(String taiKhoan, Account account) throws IOException {
        String jsonRequest = objectMapper.writeValueAsString(account);
        String jsonResponse = sendPutRequest(ACCOUNTS_API_PATH + "/" + taiKhoan, jsonRequest);
        return objectMapper.readValue(jsonResponse, Account.class);
    }

    /**
     * Xóa tài khoản.
     * @param taiKhoan Tên tài khoản cần xóa.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public void deleteAccount(String taiKhoan) throws IOException {
        sendDeleteRequest(ACCOUNTS_API_PATH + "/" + taiKhoan);
    }

    public AccountDto addAccount(AccountDto dto) {
        try {
            URL url = new URL("http://localhost:8080/api/accounts/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(dto);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            if (conn.getResponseCode() == 201) {
                InputStream responseStream = conn.getInputStream();
                return mapper.readValue(responseStream, AccountDto.class);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Account> searchAccounts(String keyword) throws IOException {
        String path = "/accounts/search?keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String json = sendGetRequest(path);
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(json, Account[].class));
    }


    public boolean tonTaiTaiKhoan(String taiKhoan) {
        try {
            String urlStr = "http://localhost:8080/api/account/" + URLEncoder.encode(taiKhoan, "UTF-8");
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            // Nếu tìm thấy account, API sẽ trả về 200
            return responseCode == 200;
        } catch (IOException e) {
            // Nếu xảy ra lỗi 404 hoặc không kết nối được -> tài khoản không tồn tại
            return false;
        }
    }
}
