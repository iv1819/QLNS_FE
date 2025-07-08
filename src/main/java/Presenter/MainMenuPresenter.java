/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.BookApiClient;
import API.CategoryApiClient;
import API.ODApiClient;
import API.OrderApiClient;
import Entity.Book;
import Entity.Category;
import Entity.OD;
import Entity.Order;
import View.interfaces.IMainMenu;
import util.BookDataChangeListener; // Vẫn sử dụng listener này
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;


public class MainMenuPresenter implements BookDataChangeListener {

    private IMainMenu view;
    private BookApiClient bookApiClient;
    private CategoryApiClient categoryApiClient;
    private ODApiClient odApiClient;
    private OrderApiClient orderApiClient;
    // Đối tượng sách đang được chọn trên UI để thêm vào hóa đơn
    private Book currentSelectedBookForReceipt;

    public MainMenuPresenter(IMainMenu view) {
        this.view = view;
        this.bookApiClient = new BookApiClient();
        this.categoryApiClient = new CategoryApiClient();
        this.odApiClient = new ODApiClient();
        this.orderApiClient = new OrderApiClient();
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
    /* Giữ trạng thái */
    private String currentCustomerName = null;

    public void onCustomerSelected(Object sel) {
        currentCustomerName = (sel == null) ? null : sel.toString().trim();
        recalcTotal();                         // tính lại tổng ngay
    }
    private void recalcTotal() {
        boolean hasCustomer =
            currentCustomerName != null &&
            !currentCustomerName.isEmpty() &&
            !currentCustomerName.equalsIgnoreCase("--Chọn khách hàng--");

        view.updateReceiptTotal(hasCustomer);  // truyền cờ giảm giá
    }
    public void onBookItemSelected(Book book) {
        this.currentSelectedBookForReceipt = book;
        view.updateSelectedBook(book);
    }
public void onAddReceiptItemClicked(int quantity) {

            // ==== VALIDATE NHƯ CŨ ====
            if (currentSelectedBookForReceipt == null) {
                view.showMessage("Vui lòng chọn một sản phẩm trước.");
                return;
            }
            if (quantity <= 0) {
                view.showMessage("Số lượng phải lớn hơn 0.");
                return;
            }
            Book book = currentSelectedBookForReceipt;
                DefaultTableModel model = view.getReceiptTableModel();

                // ==== TÌM XEM ĐÃ CÓ SÁCH NÀY TRONG BẢNG CHƯA ====
                int existedRow = -1;
                int existedQty = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(book.getTenSach())) {
                        existedRow = i;
                        existedQty = (Integer) model.getValueAt(i, 1);
                        break;
                    }
                }

                int totalQtyAfterAdd = existedQty + quantity;

                // ==== KIỂM TRA TỒN KHO VỚI TỔNG SỐ LƯỢNG ====
                if (totalQtyAfterAdd > book.getSoLuong()) {
                    view.showMessage(
                        "Số lượng yêu cầu vượt quá tồn kho ("
                        + book.getSoLuong() + "). Hiện đang có "
                        + existedQty + " quyển trong hóa đơn."
                    );
                    return;
                }

                double donGia = book.getGiaBan();
                double tongGiaRow = totalQtyAfterAdd * donGia;

                if (existedRow >= 0) {
                    // ---> ĐÃ CÓ: cập nhật số lượng & tổng giá
                    model.setValueAt(totalQtyAfterAdd, existedRow, 1);               // cột 1: Số lượng
                    model.setValueAt(String.format("%.0f $", tongGiaRow), existedRow, 3); // cột 3: Tổng giá
                } else {
                    // ---> CHƯA CÓ: thêm mới
                    Object[] row = {
                        book.getTenSach(),
                        quantity,
                        String.format("%.0f $", donGia),
                        String.format("%.0f $", quantity * donGia)
                    };
                    model.addRow(row);
                }

                // ==== CẬP NHẬT TỔNG TIỀN & TỔNG SỐ LƯỢNG ====
                recalcTotal();

                // ==== RESET CHỌN SÁCH ====
                currentSelectedBookForReceipt = null;
                view.updateSelectedBook(null);
            
        }
            private String generateOrderId() {
            int rand = ThreadLocalRandom.current().nextInt(100000, 999999);
            return "dh_" + rand;
        }
public void onCheckoutClicked(String tenkh) {
    DefaultTableModel model = view.getReceiptTableModel();

    if (model.getRowCount() == 0) {
        view.showMessage("Hoá đơn đang trống.");
        return;
    }

    String maDH = generateOrderId(); // vd: dh_123456789
    LocalDateTime ngayBan = LocalDateTime.now();
    java.sql.Date ngayBanOnlyDate = java.sql.Date.valueOf(ngayBan.toLocalDate());
    double tongTien = view.LayTongTien();

    Order newOrder = new Order(maDH, tenkh, ngayBanOnlyDate, tongTien);

    // Gọi API tạo đơn hàng
    new SwingWorker<Order, Void>() {
        @Override
        protected Order doInBackground() throws Exception {
            return orderApiClient.addOrder(newOrder);
        }

        @Override
        protected void done() {
            try {
                Order addedOrder = get();
                if (addedOrder != null) {
                    view.showMessage("Thêm đơn hàng thành công.");

                    // Sau khi thêm Order thành công => thêm chi tiết đơn hàng (OD)
                    AtomicInteger completedCount = new AtomicInteger(0);
                    int totalRows = model.getRowCount();

                    for (int row = 0; row < totalRows; row++) {
                        OD detail = new OD();
                        detail.setMaDH(maDH);
                        Object tenSPObj = model.getValueAt(row, 0);
                            if (tenSPObj == null || tenSPObj.toString().trim().isEmpty()) {
                                view.showErrorMessage("Tên sản phẩm ở dòng " + (row + 1) + " đang bị trống.");
                                return; // hoặc continue; nếu bạn muốn bỏ qua dòng đó
                            }
                            detail.setTenSach(tenSPObj.toString());

                        detail.setSoLuong((Integer) model.getValueAt(row, 1));
                        detail.setDonGia(parseMoney(model.getValueAt(row, 2).toString()));

                        new SwingWorker<OD, Void>() {
                            @Override
                            protected OD doInBackground() throws Exception {
                                System.out.println("Đang thêm CTDH: tenSP=" + detail.getTenSach()+ ", soLuong=" + detail.getSoLuong() + ", donGia=" + detail.getDonGia());

                                return odApiClient.addOD(detail);
                            }

                            @Override
                            protected void done() {
                                try {
                                    OD addedOD = get();
                                    if (addedOD == null) {
                                        view.showErrorMessage("Thêm CTDH thất bại.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    view.showErrorMessage("Lỗi khi thêm CTDH: " + e.getMessage());
                                } finally {
                                    // Khi tất cả CTDH đã xử lý => Xoá bảng
                                    if (completedCount.incrementAndGet() == totalRows) {
                                        view.clearReceiptTable();
                                    }
                                }
                            }
                        }.execute();
                    }

                } else {
                    view.showErrorMessage("Thêm đơn hàng thất bại.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                view.showErrorMessage("Lỗi khi thêm đơn hàng: " + e.getMessage());
            }
        }
    }.execute();
}

private double parseMoney(String str) {
    return Double.parseDouble(
        str.replace("$", "").replace(",", "").trim()
    );
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