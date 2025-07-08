/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;
import Model.Publisher;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Admin
 */
public class PublisherApiClient extends ApiClientBase {

    private static final String NXB_ENDPOINT = "/publishers";

    public PublisherApiClient() {
        super(); // Gọi constructor của lớp cha để khởi tạo client và objectMapper
    }

    /**
     * Lấy tất cả Nhà xuất bản từ API.
     * @return Danh sách các đối tượng Publisher, hoặc danh sách rỗng nếu có lỗi.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendGetRequest.
     */
    public List<Publisher> getAllPublishers() throws IOException {
        String jsonResponse = sendGetRequest(NXB_ENDPOINT);
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, new TypeReference<List<Publisher>>() {});
            } catch (IOException e) {
                Logger.getLogger(PublisherApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON cho Publishers", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return Collections.emptyList(); // Trả về danh sách rỗng nếu không có dữ liệu
    }
    
    /**
     * Lấy tất cả tên Nhà xuất bản từ API (được sử dụng cho ComboBox).
     * @return Danh sách các tên NXB, hoặc danh sách rỗng nếu có lỗi.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendGetRequest.
     */
    public List<String> getAllNhaXBNames() throws IOException {
        String jsonResponse = sendGetRequest(NXB_ENDPOINT + "/names"); // Giả định có endpoint /nha_xb/names
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, new TypeReference<List<String>>() {});
            } catch (IOException e) {
                Logger.getLogger(PublisherApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON tên NXB", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return Collections.emptyList();
    }

    /**
     * Lấy một Nhà xuất bản bằng mã NXB.
     * @param maNXB Mã NXB cần lấy (kiểu String).
     * @return Đối tượng Publisher nếu tìm thấy, hoặc null nếu không tìm thấy/lỗi.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendGetRequest.
     */
    public Publisher getPublisherById(String maNXB) throws IOException {
        String jsonResponse = sendGetRequest(NXB_ENDPOINT + "/" + maNXB);
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, Publisher.class);
            } catch (IOException e) {
                Logger.getLogger(PublisherApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON cho Publisher ID " + maNXB, e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }

    /**
     * Thêm một Nhà xuất bản mới vào hệ thống.
     * @param publisher Đối tượng Publisher cần thêm (maNXB có thể null nếu backend tự sinh).
     * @return Đối tượng Publisher đã được thêm (có thể bao gồm maNXB được sinh bởi backend), hoặc null nếu thất bại.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendPostRequest.
     */
    public Publisher addPublisher(Publisher publisher) throws IOException {
        // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
        String jsonRequest = objectMapper.writeValueAsString(publisher);
        String jsonResponse = sendPostRequest(NXB_ENDPOINT, jsonRequest);
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, Publisher.class);
            } catch (IOException e) {
                Logger.getLogger(PublisherApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON khi thêm Publisher", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }

    /**
     * Cập nhật thông tin một Nhà xuất bản.
     * @param maNXB Mã NXB của đối tượng cần cập nhật (kiểu String).
     * @param publisher Đối tượng Publisher chứa thông tin cập nhật.
     * @return Đối tượng Publisher đã được cập nhật, hoặc null nếu thất bại.
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendPutRequest.
     * @throws IllegalArgumentException Nếu mã NXB là null hoặc rỗng.
     */
    public Publisher updatePublisher(String maNXB, Publisher publisher) throws IOException {
        if (maNXB == null || maNXB.trim().isEmpty()) { // Thêm kiểm tra null/rỗng cho maNXB
            throw new IllegalArgumentException("Mã Nhà xuất bản không được để trống khi cập nhật.");
        }
        String jsonRequest = objectMapper.writeValueAsString(publisher);
        String jsonResponse = sendPutRequest(NXB_ENDPOINT + "/" + maNXB, jsonRequest);
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, Publisher.class);
            } catch (IOException e) {
                Logger.getLogger(PublisherApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON khi cập nhật Publisher", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return null;
    }

    /**
     * Xóa một Nhà xuất bản khỏi hệ thống.
     * @param maNXB Mã NXB của đối tượng cần xóa (kiểu String).
     * @throws IOException Nếu có lỗi mạng hoặc IO từ sendDeleteRequest.
     */
    public void deletePublisher(String maNXB) throws IOException {
        sendDeleteRequest(NXB_ENDPOINT + "/" + maNXB);
    }
}
