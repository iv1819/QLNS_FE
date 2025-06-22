/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.BookApiClient;
import API.CategoryApiClient;
import Model.Book;
import Model.Category;
import View.interfaces.IMainMenu;
import util.BookDataChangeListener; // Vẫn sử dụng listener này
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainMenuPresenter implements BookDataChangeListener {

    private IMainMenu view;
    private BookApiClient bookApiClient;
    private CategoryApiClient categoryApiClient;

    // Đối tượng sách đang được chọn trên UI để thêm vào hóa đơn
    private Book currentSelectedBookForReceipt;

    public MainMenuPresenter(IMainMenu view) {
        this.view = view;
        this.bookApiClient = new BookApiClient();
        this.categoryApiClient = new CategoryApiClient();
    }

    public void loadAndDisplayBooksByCategories() {
        LinkedHashMap<String, ArrayList<Book>> categorizedBooks = new LinkedHashMap<>();
        try {
            List<Category> categories = categoryApiClient.getAllCategories();
            List<Book> allBooks = bookApiClient.getAllBooks();

            if (categories.isEmpty()) {
                view.showMessage("Không tìm thấy danh mục nào từ API.");
            }
            categorizedBooks.put("Tất cả", new ArrayList<>(allBooks));
            for (Category category : categories) {
                ArrayList<Book> booksInThisCategory = new ArrayList<>();
                for (Book book : allBooks) {
                    if (book.getMaDanhMuc() != null && book.getMaDanhMuc().equals(category.getTenDanhMuc())) {
                        booksInThisCategory.add(book);
                    }
                }
                categorizedBooks.put(category.getTenDanhMuc(), booksInThisCategory);
            }
            view.populateMaterialCategoryTabs(categorizedBooks);

        } catch (IOException ex) {
            Logger.getLogger(MainMenuPresenter.class.getName()).log(Level.SEVERE, null, ex);
            view.showErrorMessage("Lỗi khi tải dữ liệu từ API: " + ex.getMessage() + "\nĐảm bảo backend đang chạy và BASE_URL đúng.");
        }
    }

    public void onBookItemSelected(Book book) {
        this.currentSelectedBookForReceipt = book;
        view.updateSelectedBook(book);
    }

    public void onAddReceiptItemClicked(int quantity) {
        if (currentSelectedBookForReceipt == null) {
            view.showMessage("Vui lòng chọn một cuốn sách trước.");
            return;
        }

        if (quantity <= 0) {
            view.showMessage("Số lượng phải lớn hơn 0.");
            return;
        }

        if (quantity > currentSelectedBookForReceipt.getSoLuong()) {
            view.showMessage("Số lượng yêu cầu vượt quá số lượng tồn kho (" + currentSelectedBookForReceipt.getSoLuong() + ").");
            return;
        }

        String tenSach = currentSelectedBookForReceipt.getTenSach();
        double donGia = currentSelectedBookForReceipt.getGiaBan();
        double tongGia = quantity * donGia;

        Object[] rowData = {tenSach, quantity, String.format("%.0f VNĐ", donGia), String.format("%.0f VNĐ", tongGia)};
        view.addReceiptItem(rowData);

        // Cập nhật tổng tiền và tổng số lượng
        updateReceiptTotals();
        
        // Reset lựa chọn sách sau khi thêm vào hóa đơn
        currentSelectedBookForReceipt = null;
        view.updateSelectedBook(null); // Xóa thông tin sách đang chọn trên UI
    }


    private void updateReceiptTotals() {
        view.updateReceiptTotalAmount(calculateTotalAmountFromView());
        view.updateReceiptTotalItems(calculateTotalItemsFromView());
    }

    private double calculateTotalAmountFromView() {
        return 0;
    }

    private int calculateTotalItemsFromView() {
        return 0;
    }

    @Override
    public void onBookDataChanged() {
        System.out.println("DEBUG (MainMenuPresenter): Nhận được thông báo dữ liệu sách đã thay đổi. Đang tải lại tab.");
        loadAndDisplayBooksByCategories(); // Tải lại dữ liệu khi có thay đổi
    }

    // Các phương thức khác của Presenter (ví dụ: tìm kiếm sách)
    public List<Book> searchBooks(String query) throws IOException {
        return bookApiClient.searchBooks(query);
    }
}