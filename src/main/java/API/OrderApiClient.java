/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;

import Entity.Author;
import Entity.OD;
import Entity.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class OrderApiClient extends ApiClientBase {

    private static final String order_endpoints = "/orders";

    public OrderApiClient() {
        super(); 
    }

   
    public List<Order> getAllOrders() throws IOException {
        String jsonResponse = sendGetRequest(order_endpoints); // Gọi sendGetRequest từ ApiClientBase
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Order>>() {});
            } catch (IOException e) {
                Logger.getLogger(OrderApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON cho Order", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return Collections.emptyList();
    }
    
    public Order getOrderbyMaDH(String maDH) throws IOException { // Đảm bảo maTG là String
        String jsonResponse = sendGetRequest(order_endpoints + "/" + maDH); // Gọi sendGetRequest
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, Order.class);
            } catch (IOException e) {
                Logger.getLogger(OrderApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON cho Order ID " + maDH, e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }

    public Order addOrder(Order order) throws IOException {
        // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
        String jsonRequest = objectMapper.writeValueAsString(order);
        String jsonResponse = sendPostRequest(order_endpoints, jsonRequest); // Gọi sendPostRequest
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, Order.class);
            } catch (IOException e) {
                Logger.getLogger(OrderApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON khi thêm Order", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }

    public void deleteOrder(String maDH) throws IOException {
        sendDeleteRequest(order_endpoints + "/" + maDH); // Gọi sendDeleteRequest
    }
    public List<Order> searchOrder(String query) throws IOException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        String jsonResponse = sendGetRequest(order_endpoints + "/search?query=" + encodedQuery); // <-- Gọi sendGetRequest từ ApiClientBase
        Order[] ordersArray = objectMapper.readValue(jsonResponse, Order[].class);
        return Arrays.asList(ordersArray);
    }
}
