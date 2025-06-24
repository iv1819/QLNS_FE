/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
/**
 * Model chịu trách nhiệm gọi API /login.
 * Kế thừa ApiClientBase để dùng chung OkHttpClient + Jackson.
 */
public class LoginApiClient extends ApiClientBase {

    private static final String LOGIN_PATH = "/login";   // BASE_URL + /login

    /**
     * Thực hiện đăng nhập.
     * @return LoginResult (success, message, isManager)
     */
    public Map<String, Object> login(String username, String password) {
     Map<String, String> bodyMap = Map.of("taiKhoan", username, "matKhau", password);

     try {
         String jsonBody = objectMapper.writeValueAsString(bodyMap);
         String respJson = sendPostRequest(LOGIN_PATH, jsonBody);

         JsonNode node = objectMapper.readTree(respJson);
         boolean success = node.path("success").asBoolean(false);

         Map<String, Object> result = new HashMap<>();
         result.put("success", success);

         if (success) {
             String role = node.path("role").asText("Nhân viên");
                result.put("role", role);
         } else {
             result.put("message", node.path("message").asText("Đăng nhập thất bại"));
         }

         return result;

     } catch (IOException ex) {
         ex.printStackTrace();
         return Map.of(
             "success", false,
             "message", "Không kết nối được máy chủ"
         );
     }
 }

}

