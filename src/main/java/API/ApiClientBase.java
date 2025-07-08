package API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp cơ sở cho tất cả các API client, cung cấp OkHttpClient, ObjectMapper
 * và các phương thức chung để gửi các yêu cầu HTTP.
 */
public abstract class ApiClientBase { // Đã chuyển thành abstract vì nó không thể được khởi tạo trực tiếp

    // Thay đổi BASE_URL này thành URL của API backend thực tế của bạn
    // Ví dụ: "http://localhost:8080/api" nếu backend chạy trên localhost cổng 8080 và có prefix /api
    protected static final String BASE_URL = "http://localhost:8080/api";

    protected OkHttpClient client;
    protected ObjectMapper objectMapper; // Dùng để chuyển đổi giữa Java Objects và JSON

    public ApiClientBase() {
        // Cấu hình OkHttpClient với các timeout hợp lý
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // Thời gian chờ kết nối
                .readTimeout(30, TimeUnit.SECONDS)    // Thời gian chờ đọc dữ liệu
                .writeTimeout(30, TimeUnit.SECONDS)   // Thời gian chờ ghi dữ liệu
                .build();

        // Khởi tạo ObjectMapper của Jackson
        this.objectMapper = new ObjectMapper();
        // Đăng ký module để hỗ trợ Java 8 Date/Time API (LocalDate, etc.)
        this.objectMapper.registerModule(new JavaTimeModule());
        // Có thể thêm các cấu hình Jackson khác nếu cần, ví dụ:
        // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Gửi một yêu cầu GET đến một đường dẫn API cụ thể.
     *
     * @param path Đường dẫn cụ thể của API (ví dụ: "/books", "/categories/names").
     * @return Chuỗi JSON trả về từ API.
     * @throws IOException Nếu yêu cầu không thành công (lỗi mạng, HTTP error code).
     */
    protected String sendGetRequest(String path) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                Logger.getLogger(ApiClientBase.class.getName()).log(Level.SEVERE,
                        "GET request to " + path + " failed: " + response.code() + " - " + response.message() + " - " + errorBody);
                throw new IOException("GET request to " + BASE_URL + path + " failed: " + response.code() + " " + response.message());
            }
            return response.body().string();
        }
    }

    /**
     * Gửi một yêu cầu POST đến một đường dẫn API cụ thể với dữ liệu JSON.
     *
     * @param path Đường dẫn cụ thể của API (ví dụ: "/books", "/categories").
     * @param jsonBody Chuỗi JSON của dữ liệu sẽ gửi đi.
     * @return Chuỗi JSON trả về từ API.
     * @throws IOException Nếu yêu cầu không thành công (lỗi mạng, HTTP error code).
     */
    protected String sendPostRequest(String path, String jsonBody) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                Logger.getLogger(ApiClientBase.class.getName()).log(Level.SEVERE,
                        "POST request to " + path + " failed: " + response.code() + " - " + response.message() + " - " + errorBody);
                throw new IOException("POST request to " + BASE_URL + path + " failed: " + response.code() + " " + response.message() + " - " + errorBody);

            }
            return response.body().string();
        }
    }

    /**
     * Gửi một yêu cầu PUT đến một đường dẫn API cụ thể với dữ liệu JSON.
     *
     * @param path Đường dẫn cụ thể của API (ví dụ: "/books/{id}", "/categories/{id}").
     * @param jsonBody Chuỗi JSON của dữ liệu sẽ gửi đi.
     * @return Chuỗi JSON trả về từ API.
     * @throws IOException Nếu yêu cầu không thành công (lỗi mạng, HTTP error code).
     */
    protected String sendPutRequest(String path, String jsonBody) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                Logger.getLogger(ApiClientBase.class.getName()).log(Level.SEVERE,
                        "PUT request to " + path + " failed: " + response.code() + " - " + response.message() + " - " + errorBody);
                throw new IOException("PUT request to " + BASE_URL + path + " failed: " + response.code() + " " + response.message() + " - " + errorBody);

            }
            return response.body().string();
        }
    }

    /**
     * Gửi một yêu cầu DELETE đến một đường dẫn API cụ thể.
     *
     * @param path Đường dẫn cụ thể của API (ví dụ: "/books/{id}", "/categories/{id}").
     * @throws IOException Nếu yêu cầu không thành công (lỗi mạng, HTTP error code).
     */
    protected void sendDeleteRequest(String path) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                Logger.getLogger(ApiClientBase.class.getName()).log(Level.SEVERE,
                        "DELETE request to " + path + " failed: " + response.code() + " - " + response.message() + " - " + errorBody);
                throw new IOException("DELETE request to " + BASE_URL + path + " failed: " + response.code() + " " + response.message() + " - " + errorBody);
            }
            // Đối với DELETE, thường không có body trả về, chỉ cần kiểm tra success
        }
    }
}
