/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.AuthorApiClient;
import Model.Author;
import View.AuthorM;
import View.interfaces.IAuthorM;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 *
 * @author trang
 */
public class AuthorMPresenter {

    private IAuthorM view;
    private AuthorApiClient authorApiClient;
 
    public AuthorMPresenter(IAuthorM view) {
        this.view = view;
        this.authorApiClient = new AuthorApiClient();
        initializeData();
    }

    public AuthorMPresenter(AuthorM aThis, MainMenuPresenter mainMenuPresenter) {
        this.view = aThis;
        this.authorApiClient = new AuthorApiClient();

        // Khởi tạo dữ liệu ngay
        initializeData();

        // Nếu bạn muốn dùng MainMenuPresenter để làm gì đó sau này thì có thể lưu lại:
        // this.mainMenuPresenter = mainMenuPresenter; // nếu có biến để giữ
    }


    private void initializeData() {
        loadAllAuthors();
    }

    /**
     * Tải tất cả tác giả từ API và yêu cầu View hiển thị.
     */
    public void loadAllAuthors() {
        new SwingWorker<List<Author>, Void>() {
            @Override
            protected List<Author> doInBackground() throws Exception {
                return authorApiClient.getAllAuthors();
            }

            @Override
            protected void done() {
                try {
                    List<Author> authors = get();
                    view.displayAuthors(new ArrayList<>(authors));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi tải tác giả từ API: " + e.getMessage());
                }
            }
        }.execute();
    }

    /**
     * Xử lý logic thêm tác giả.
     */
    public void addAuthor(Author newAuthor) {
        Author author = new Author();
        author.setMaTG(view.getMaTG());
        author.setTenTG(view.getTenTG());

        // Validate input
        if (author.getTenTG() == null || author.getTenTG().isEmpty()) {
            view.showErrorMessage("Vui lòng nhập tên tác giả.");
            return;
        }
        if (author.getMaTG() != null && !author.getMaTG().startsWith("tg_")) {
            view.showErrorMessage("Mã tác giả phải bắt đầu bằng tg_");
            return;
        }
        if (!author.getTenTG().matches("^[\\p{L}\\s]+$")) {
            view.showErrorMessage("Tên tác giả không được chứa số hoặc ký tự đặc biệt.");
            return;
        }

        new SwingWorker<Author, Void>() {
            @Override
            protected Author doInBackground() throws Exception {
                return authorApiClient.addAuthor(author);
            }

            @Override
            protected void done() {
                try {
                    Author addedAuthor = get();
                    if (addedAuthor != null) {
                        view.showMessage("Thêm tác giả thành công!");
                        System.out.println("maTG gửi lên: " + view.getMaTG());
                        loadAllAuthors();
                        view.clearForm();
                    } else {
                        view.showErrorMessage("Thêm tác giả thất bại.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Throwable cause = e.getCause();
                    String message = cause != null ? cause.getMessage() : e.getMessage();

                    if (message != null && message.contains("409")) {
                        view.showErrorMessage("Mã tác giả đã tồn tại. Vui lòng nhập mã khác.");
                    } else {
                        view.showErrorMessage("Lỗi khi thêm tác giả: " + message);
                    }
                }
            }
        }.execute();
    }

    /**
     * Xử lý logic cập nhật tác giả.
     */
    public void updateAuthor(Author updateAuthor) {
        String maTG = view.getMaTG();
        if (maTG == null || maTG.isEmpty()) {
            view.showErrorMessage("Vui lòng chọn tác giả cần cập nhật.");
            return;
        }

        Author author = new Author();
        author.setMaTG(maTG);
        author.setTenTG(view.getTenTG());

        if (author.getTenTG() == null || author.getTenTG().isEmpty()) {
            view.showErrorMessage("Vui lòng nhập tên tác giả.");
            return;
        }

        new SwingWorker<Author, Void>() {
            @Override
            protected Author doInBackground() throws Exception {
                return authorApiClient.updateAuthor(maTG, author);
            }

            @Override
            protected void done() {
                try {
                    Author updatedAuthor = get();
                    if (updatedAuthor != null) {
                        view.showMessage("Cập nhật tác giả thành công!");
                        loadAllAuthors();
                        view.clearForm();
                    } else {
                        view.showErrorMessage("Cập nhật tác giả thất bại.");
                    }


                } catch (InterruptedException | ExecutionException e) {
                    Throwable cause = e.getCause();
                    String message = cause != null ? cause.getMessage() : e.getMessage();
                    view.showErrorMessage("Lỗi khi cập nhật tác giả qua API: " + message);
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Xử lý logic xóa tác giả.
     */
    public void deleteAuthor(String maTG) {
        if (maTG == null || maTG.isEmpty()) {
            view.showErrorMessage("Vui lòng chọn tác giả cần xóa.");
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                authorApiClient.deleteAuthor(maTG);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Kiểm tra ngoại lệ
                    view.showMessage("Xóa tác giả thành công!");
                    loadAllAuthors();
                    view.clearForm();
                } catch (InterruptedException | ExecutionException e) {
                    Throwable cause = e.getCause();
                    String message = cause != null ? cause.getMessage() : e.getMessage();
                    if (message.contains("Không thể xóa vì tác giả vẫn có sách liên kết")) {
                        view.showErrorMessage("Tác giả vẫn còn sách, không thể xóa.");
                    } else {
                        view.showErrorMessage("Lỗi khi xóa tác giả qua API: " + message);
                    }
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Xử lý logic tìm kiếm tác giả.
     */
    public void searchAuthors(String tenTG) {
        new SwingWorker<List<Author>, Void>() {
            @Override
            protected List<Author> doInBackground() throws Exception {
                if (tenTG == null || tenTG.isEmpty()) {
                    return authorApiClient.getAllAuthors();
                }
                return authorApiClient.searchAuthors(tenTG);
            }

            @Override
            protected void done() {
                try {
                    List<Author> searchResults = get();
                    view.displayAuthors(new ArrayList<>(searchResults));
                    if (searchResults.isEmpty()) {
                        view.showMessage("Không tìm thấy tác giả nào phù hợp.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Throwable cause = e.getCause();
                    String message = cause != null ? cause.getMessage() : e.getMessage();
                    view.showErrorMessage("Lỗi khi tìm kiếm tác giả qua API: " + message);
                    e.printStackTrace();
                }
            }
        }.execute();
    }

//    public void addDataChangeListener(MainMenuPresenter mainMenuPresenter) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
}