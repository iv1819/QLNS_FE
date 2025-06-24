/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;

import Model.Author;
import Model.Book;
import Model.OD;
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
public class ODApiClient extends ApiClientBase {

    private static final String ctdh_endPoints = "/ods";

    public ODApiClient() {
        super(); // Gọi constructor của lớp cha để khởi tạo client và objectMapper
    }

    
    public List<OD> getAllODs() throws IOException {
        String jsonResponse = sendGetRequest(ctdh_endPoints); // Gọi sendGetRequest từ ApiClientBase
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, new TypeReference<List<OD>>() {});
            } catch (IOException e) {
                Logger.getLogger(ODApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON cho Ods", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return Collections.emptyList();
    }
    

    public OD getODbyID(int id) throws IOException {
        String jsonResponse = sendGetRequest(ctdh_endPoints + "/" + id); // Gọi sendGetRequest
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, OD.class);
            } catch (IOException e) {
                Logger.getLogger(ODApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON cho ods ID " + id, e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }

    public OD addOD(OD od) throws IOException {
        // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
        String jsonRequest = objectMapper.writeValueAsString(od);
        String jsonResponse = sendPostRequest(ctdh_endPoints, jsonRequest); // Gọi sendPostRequest
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, OD.class);
            } catch (IOException e) {
                Logger.getLogger(ODApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON khi thêm OD", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }
   public void deleteODbyMaDH(String madh) throws IOException {
        sendDeleteRequest(ctdh_endPoints + "/madh/" + madh); // Gọi sendDeleteRequest
    }
    
    public void deleteOD(int id) throws IOException {
        sendDeleteRequest(ctdh_endPoints + "/" + id); // Gọi sendDeleteRequest
    }
     public List<OD> searchOds(String query) throws IOException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        String jsonResponse = sendGetRequest(ctdh_endPoints + "/search?query=" + encodedQuery); // <-- Gọi sendGetRequest từ ApiClientBase
        OD[] odsArray = objectMapper.readValue(jsonResponse, OD[].class);
        return Arrays.asList(odsArray);
    }
}
