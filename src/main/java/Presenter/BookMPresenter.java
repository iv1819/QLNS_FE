/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.AuthorApiClient;
import API.BookApiClient;
import API.CategoryApiClient; // Cần thiết để lấy tên danh mục
import API.PublisherApiClient;
import Model.Book;
import View.BookM;
import View.interfaces.IBookM;
import com.fasterxml.jackson.databind.JsonNode;
import util.BookDataChangeListener;
import okhttp3.OkHttpClient;
import javax.swing.SwingWorker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * Presenter cho BookM View.
 * Xử lý logic nghiệp vụ liên quan đến quản lý sách (CRUD).
 */
public class BookMPresenter {

    private IBookM view;
    private BookApiClient bookApiClient;
    private CategoryApiClient categoryApiClient;
    private AuthorApiClient authorApi;
    private PublisherApiClient pubApi;
    private String selectedLocalImagePath; // Đường dẫn file cục bộ được chọn (tạm thời cho preview)
    private String uploadedImageUrl; // URL của ảnh sau khi tải lên backend

    // OkHttpClient và ObjectMapper cho việc gọi API upload
    private final OkHttpClient okHttpClient;
    private static final String API_BASE_URL = "http://localhost:8080/api"; // Phải khớp với backend
private String currentUploadedImageUrl;
    // Danh sách các Listener muốn nhận thông báo về thay đổi dữ liệu sách
    private List<BookDataChangeListener> listeners = new ArrayList<>();
 private final ObjectMapper objectMapper; 
    public BookMPresenter(IBookM view) {
        this.view = view;
        this.bookApiClient = new BookApiClient();
        this.categoryApiClient = new CategoryApiClient();
                this.authorApi = new AuthorApiClient();
        this.pubApi = new PublisherApiClient();
        this.objectMapper = new ObjectMapper();
BookM.setApiBaseUrlForImages("http://localhost:8080"); 
 this.okHttpClient = new OkHttpClient(); 
        this.selectedLocalImagePath = "";
        this.uploadedImageUrl = "";
        initializeData();
    }

    // Phương thức để thêm Listener (MainMenuPresenter sẽ đăng ký ở đây)
    public void addListener(BookDataChangeListener listener) {
        listeners.add(listener);
        System.out.println("DEBUG (BookMPresenter): Đã thêm listener: " + listener.getClass().getSimpleName());
    }

    // Phương thức để xóa Listener (khi BookM đóng)
    public void removeListener(BookDataChangeListener listener) {
        listeners.remove(listener);
        System.out.println("DEBUG (BookMPresenter): Đã xóa listener: " + listener.getClass().getSimpleName());
    }

    // Phương thức thông báo cho tất cả các Listener
    private void notifyListeners() {
        System.out.println("DEBUG (BookMPresenter): Đang thông báo cho các listener về thay đổi dữ liệu sách.");
        for (BookDataChangeListener listener : listeners) {
            listener.onBookDataChanged();
        }
    }
    public void clearForm() {
            view.clearForm();
            currentUploadedImageUrl = null; // Xóa URL ảnh đã upload
        }
      public void onBookSelected() {
        String maSach = view.getSelectedMaSach();
        if (maSach == null || maSach.trim().isEmpty()) {
            view.clearForm();
            return;
        }

        new SwingWorker<Book, Void>() {
            @Override
            protected Book doInBackground() throws Exception {
                return bookApiClient.getBookById(maSach); // Gọi trực tiếp ApiClient
            }

            @Override
            protected void done() {
                try {
                    Book book = get();
                    if (book != null) {
                        view.populateBookDetails(book);
                        // Cập nhật currentUploadedImageUrl với đường dẫn của sách hiện tại
                        currentUploadedImageUrl = book.getDuongDanAnh(); 
                    } else {
                        view.showErrorMessage("Không tìm thấy chi tiết sách cho ID: " + maSach);
                        view.clearForm();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi tải chi tiết sách: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    public void onImageSelectionRequested(File selectedFile) {
    if (selectedFile == null) return;

    /* 1. Tạm thời hiển thị ảnh cục bộ cho người dùng xem trước */
    view.updateImagePreview(selectedFile.getAbsolutePath());

    new SwingWorker<String, Void>() {

        @Override
        protected String doInBackground() throws Exception {

            /* 2. Endpoint upload mới */
            String uploadUrl = API_BASE_URL + "/uploads";        // API_BASE_URL = "http://localhost:8080/api"

            /* 3. Tạo multipart request */
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", selectedFile.getName(),
                            RequestBody.create(
                                    selectedFile,
                                    MediaType.parse(Files.probeContentType(selectedFile.toPath()))))
                    .build();

            Request request = new Request.Builder().url(uploadUrl).post(body).build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    String err = response.body() != null ? response.body().string() : "<no body>";
                    throw new IOException("Upload thất bại – HTTP " + response.code() + ": " + err);
                }

                /* 4. Đọc JSON */
                JsonNode json = objectMapper.readTree(response.body().string());

                if (json.has("url")) {
                    return json.get("url").asText();             // "/api/uploads/<uuid>.jpg"
                }
                throw new IOException("JSON không chứa 'url': " + json.toPrettyString());
            }
        }

        @Override
        protected void done() {
            try {
                String uploadedPath = get();                     // "/api/uploads/..."
                if (uploadedPath != null && !uploadedPath.isBlank()) {
                    currentUploadedImageUrl = uploadedPath;      // Lưu vào biến
                    view.updateImagePreview(uploadedPath);       // View sẽ tự ghép domain nếu cần
                    view.showMessage("Ảnh đã tải lên: " + uploadedPath);
                } else {
                    currentUploadedImageUrl = null;
                    view.updateImagePreview("");
                    view.showErrorMessage("Không nhận được URL ảnh từ server.");
                }
            } catch (InterruptedException | ExecutionException ex) {
                Throwable cause = ex.getCause();
                currentUploadedImageUrl = null;
                view.updateImagePreview("");
                view.showErrorMessage("Lỗi upload: " +
                        (cause != null ? cause.getMessage() : ex.getMessage()));
                ex.printStackTrace();
            }
        }
    }.execute();
}

    public String getSelectedLocalImagePath() {
        return selectedLocalImagePath;
    }

    /**
     * Lấy URL ảnh đã được tải lên backend.
     */
    public String getUploadedImageUrl() {
        return uploadedImageUrl;
    }
    
    /**
     * Tải tất cả sách từ API và yêu cầu View hiển thị.
     */
    public void loadAllBooks() {
        new SwingWorker<List<Book>, Void>() {
            @Override
            protected List<Book> doInBackground() throws Exception {
                return bookApiClient.getAllBooks();
            }

            @Override
            protected void done() {
                try {
                    List<Book> books = get();
                    view.displayBooks(new ArrayList<>(books));
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi tải sách từ API: " + e.getMessage());
                }
            }
        }.execute();
    }

    /**
     * Xử lý logic thêm sách.
     * @param book Đối tượng Book cần thêm.
     */
    public void addBook(Book book) {
        // Đảm bảo book.getDuongDanAnh() chứa uploadedImageUrl
        book.setDuongDanAnh(uploadedImageUrl);

        new SwingWorker<Book, Void>() {
            @Override
            protected Book doInBackground() throws Exception {
                return bookApiClient.addBook(book);
            }

            @Override
            protected void done() {
                try {
                    Book addedBook = get();
                    if (addedBook != null) {
                        view.showMessage("Thêm sách thành công!");
                        loadAllBooks(); // Tải lại danh sách sách trong BookM
                        notifyListeners(); // Thông báo cho MainMenuPresenter
                        view.clearForm(); // Xóa trắng input
                    } else {
                        view.showErrorMessage("Thêm sách thất bại.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi thêm sách qua API: " + e.getMessage());
                }
            }
        }.execute();
    }

    /**
     * Xử lý logic cập nhật sách.
     * @param book Đối tượng Book cần cập nhật.
     */
    public void updateBook(Book book) {
        // Nếu có ảnh mới được upload, cập nhật đường dẫn ảnh
        if (!uploadedImageUrl.isEmpty()) {
            book.setDuongDanAnh(uploadedImageUrl);
        }

        new SwingWorker<Book, Void>() {
            @Override
            protected Book doInBackground() throws Exception {
                return bookApiClient.updateBook(book);
            }

            @Override
            protected void done() {
                try {
                    Book updatedBook = get();
                    if (updatedBook != null) {
                        view.showMessage("Cập nhật sách thành công!");
                        loadAllBooks();
                        notifyListeners();
                        view.clearForm();
                    } else {
                        view.showErrorMessage("Cập nhật sách thất bại.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi cập nhật sách qua API: " + e.getMessage());
                }
            }
        }.execute();
    }

    /**
     * Xử lý logic xóa sách.
     * @param maSach Mã sách cần xóa.
     */
    public void deleteBook(String maSach) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                bookApiClient.deleteBook(maSach);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Kiểm tra ngoại lệ
                    view.showMessage("Xóa sách thành công!");
                    loadAllBooks();
                    notifyListeners();
                    view.clearForm();
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi xóa sách qua API: " + e.getMessage());
                }
            }
        }.execute();
    }

    /**
     * Xử lý logic tìm kiếm sách.
     * @param tenSach Tên sách để tìm kiếm.
     * @param tenTacGia Tên tác giả để tìm kiếm.
     */
    public void searchBooks(String tenSach, String tenTacGia) {
        new SwingWorker<List<Book>, Void>() {
            @Override
            protected List<Book> doInBackground() throws Exception {
                String query = "";
                if (tenSach != null && !tenSach.isEmpty()) {
                    query += tenSach;
                }
                if (tenTacGia != null && !tenTacGia.isEmpty()) {
                    if (!query.isEmpty()) query += " ";
                    query += tenTacGia;
                }
                if (query.isEmpty()) {
                    return bookApiClient.getAllBooks();
                }
                return bookApiClient.searchBooks(query);
            }

            @Override
            protected void done() {
                try {
                    List<Book> searchResults = get();
                    view.displayBooks(new ArrayList<>(searchResults));
                    if (searchResults.isEmpty()) {
                        view.showMessage("Không tìm thấy sách nào phù hợp.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi tìm kiếm sách qua API: " + e.getMessage());
                }
            }
        }.execute();
    }

    /**
     * Lấy tất cả tên nhà xuất bản từ API (hoặc từ nguồn dữ liệu khác).
     * @return Danh sách tên nhà xuất bản.
     */
    public List<String> loadAllPublishersAndPopulateNames(){
         new SwingWorker<List<String>, Void>() {
        @Override
        protected List<String> doInBackground() throws Exception {
            // Đây chính là đoạn code bạn cung cấp, thực hiện gọi API Client
            return pubApi.getAllNhaXBNames(); 
        }

        @Override
        protected void done() {
            try {
                List<String> names = get(); // Lấy kết quả từ doInBackground
                view.populateNhaXBNames(names); // Cập nhật ComboBox trên View
            } catch (InterruptedException | ExecutionException e) {
               view.showErrorMessage("Lỗi tải tên Nhà Xuất Bản: " + e.getMessage());
                    e.printStackTrace();
                    view.populateNhaXBNames(Collections.emptyList());
            }
        }
    }.execute(); 
        return null;
         
    }
    
    /**
     * Lấy tất cả tên tác giả từ API (hoặc từ nguồn dữ liệu khác).
     * @return Danh sách tên tác giả.
     */
    public List<String> loadAllAuthorsAndPopulateNames(){
 new SwingWorker<List<String>, Void>() {
        @Override
        protected List<String> doInBackground() throws Exception {
            // Đây chính là đoạn code bạn cung cấp, thực hiện gọi API Client
            return authorApi.getAllTacGiaNames(); 
        }

        @Override
        protected void done() {
             try {
                    List<String> names = get();
                    view.populateTacGiaNames(names);
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi tải tên Tác giả: " + e.getMessage());
                    e.printStackTrace();
                    view.populateTacGiaNames(Collections.emptyList());
                }
        }
    }.execute(); 
        return null;    }

    /**
     * Lấy tất cả tên danh mục từ API và yêu cầu View cập nhật ComboBox.
     * @return Danh sách tên danh mục.
     */
    public List<String> loadAllCategoriesAndPopulateNames(){
        new SwingWorker<List<String>, Void>() {
        @Override
        protected List<String> doInBackground() throws Exception {
            // Đây chính là đoạn code bạn cung cấp, thực hiện gọi API Client
            return categoryApiClient.getAllDanhMucNames(); 
        }

        @Override
        protected void done() {
             try {
                    List<String> names = get();
                    view.populateDanhMucNames(names);
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi tải tên Danh muc: " + e.getMessage());
                    e.printStackTrace();
                    view.populateTacGiaNames(Collections.emptyList());
                }
        }
    }.execute(); 
        return null;  
    }
 private void initializeData() {
        loadAllBooks(); // Tải danh sách sách
        loadAllAuthorsAndPopulateNames(); // Tải tên tác giả
        loadAllPublishersAndPopulateNames(); // Tải tên nhà xuất bản
        loadAllCategoriesAndPopulateNames(); // Tải tên danh mục
    }
}

