/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package API;

import static API.ApiClientBase.BASE_URL;
import Model.Category;
import com.fasterxml.jackson.core.type.TypeReference;
    import okhttp3.*;
    import java.io.IOException;
    import java.util.Arrays;
import java.util.Collections;
    import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

    public class CategoryApiClient extends ApiClientBase {

        private static final String CATEGORIES = "/categories"; // Chỉ là đường dẫn tương đối

    public CategoryApiClient() {
        super();
    }
 public List<String> getAllDanhMucNames() throws IOException {
        String jsonResponse = sendGetRequest(CATEGORIES + "/names"); 
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Sử dụng objectMapper đã được kế thừa từ ApiClientBase
                return objectMapper.readValue(jsonResponse, new TypeReference<List<String>>() {});
            } catch (IOException e) {
                Logger.getLogger(AuthorApiClient.class.getName()).log(Level.SEVERE, "Lỗi phân tích JSON tên dm", e);
                throw e; // Ném lại lỗi để Presenter xử lý
            }
        }
        return Collections.emptyList();
    }
    /**
     * Lấy tất cả thể loại từ API.
     *
     * @return Danh sách các đối tượng Category.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public List<Category> getAllCategories() throws IOException {
        String jsonResponse = sendGetRequest(CATEGORIES); // <-- Gọi sendGetRequest từ ApiClientBase
        Category[] categoriesArray = objectMapper.readValue(jsonResponse, Category[].class);
        return Arrays.asList(categoriesArray);
    }

        /**
         * Thêm một danh mục mới vào API.
         * Tương ứng với POST /api/categories
         */
        public Category addCategory(Category category) throws IOException {
            String jsonInputString = objectMapper.writeValueAsString(category); // Sử dụng objectMapper từ ApiClientBase
        String jsonResponse = sendPostRequest(CATEGORIES, jsonInputString); // <-- Gọi sendPostRequest từ ApiClientBase
        return objectMapper.readValue(jsonResponse, Category.class);
        }

        /**
         * Cập nhật một danh mục hiện có trong API.
         * Tương ứng với PUT /api/categories/{id}
         */
        public Category updateCategory(Category category) throws IOException {
            if (category.getMaDanhMuc() == null || category.getMaDanhMuc().isEmpty()) {
                throw new IllegalArgumentException("Category ID (maDanhMuc) cannot be null or empty for update.");
            }
            String jsonInputString = objectMapper.writeValueAsString(category);
              String jsonResponse = sendPutRequest(CATEGORIES + "/" + category.getMaDanhMuc(), jsonInputString);
                return objectMapper.readValue(jsonResponse, Category.class);
            
        }

        /**
         * Xóa một danh mục khỏi API.
         * Tương ứng với DELETE /api/categories/{id}
         */
        public void deleteCategory(String maDanhMuc) throws IOException {
           sendDeleteRequest(CATEGORIES + "/" + maDanhMuc);
        }
    }
    
