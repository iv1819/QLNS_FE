/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.BookApiClient;
import API.CategoryApiClient;
import API.CustomerApiClient;
import API.ODApiClient;
import API.OrderApiClient;
import Model.Book;
import Model.Category;
import Model.Customer;
import Model.OD;
import Model.Order;
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
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;

public class MainMenuPresenter implements BookDataChangeListener {

    private IMainMenu view;
    private BookApiClient bookApiClient;
    private CategoryApiClient categoryApiClient;
    private ODApiClient odApiClient;
    private OrderApiClient orderApiClient;
    private CustomerApiClient cusApiClient;
    // Đối tượng sách đang được chọn trên UI để thêm vào hóa đơn
    private Book currentSelectedBookForReceipt;
private String currentCustomerName = null;
    public MainMenuPresenter(IMainMenu view) {
        this.view = view;
        this.bookApiClient = new BookApiClient();
        this.categoryApiClient = new CategoryApiClient();
        this.odApiClient = new ODApiClient();
        this.orderApiClient = new OrderApiClient();
                this.cusApiClient = new CustomerApiClient();

    }

   public void loadAndDisplayBooksByCategories() {
        new SwingWorker<LinkedHashMap<String, ArrayList<Book>>, Void>() {
            @Override
            protected LinkedHashMap<String, ArrayList<Book>> doInBackground() throws Exception {
                LinkedHashMap<String, ArrayList<Book>> categorizedBooks = new LinkedHashMap<>();

                List<Category> categories = categoryApiClient.getAllCategories();
                List<Book> allBooks = bookApiClient.getAllBooks();

                // Add "Tất cả" category first
                categorizedBooks.put("Tất cả", new ArrayList<>(allBooks));

                // Populate other categories
                for (Category category : categories) {
                    ArrayList<Book> booksInThisCategory = new ArrayList<>();
                    for (Book book : allBooks) {
                         if (book.getMaDanhMuc() != null && book.getMaDanhMuc().equals(category.getMaDanhMuc())) {
                                 booksInThisCategory.add(book);
                        }
                    }
                    categorizedBooks.put(category.getTenDanhMuc(), booksInThisCategory);
                }
                return categorizedBooks;
            }

            @Override
            protected void done() {
                try {
                    LinkedHashMap<String, ArrayList<Book>> categorizedBooks = get(); // Get result from doInBackground
                    // Check if categories or books are empty and show message
                    if (categorizedBooks.isEmpty() || (categorizedBooks.size() == 1 && categorizedBooks.containsKey("Tất cả") && categorizedBooks.get("Tất cả").isEmpty())) {
                        view.showMessage("Không tìm thấy danh mục hoặc sách nào từ API.");
                    }
                    view.populateMaterialCategoryTabs(categorizedBooks);
                } catch (Exception ex) {
                    Logger.getLogger(MainMenuPresenter.class.getName()).log(Level.SEVERE, null, ex);
                    view.showErrorMessage("Lỗi khi tải dữ liệu từ API: " + ex.getMessage() + "\nĐảm bảo backend đang chạy và BASE_URL đúng.");
                }
            }
        }.execute(); // Start the SwingWorker
    }

    // Changed return type to void as it's now asynchronous
    public void onCustomerSelected(String sel) {
        // Clear current customer name immediately to show a 'loading' or 'no selection' state
        currentCustomerName = null;
        view.updateCustomerNameDisplay(null); // Update UI to reflect no customer selected yet

        if (sel == null || sel.trim().isEmpty()) {
            System.out.println("DEBUG (onCustomerSelected): Input selection is empty or null.");
            recalcTotal(); // Recalculate total as no customer is selected
            return;
        }

        String trimmedSel = sel.trim();

        new SwingWorker<String, Void>() { // SwingWorker to perform search in background
            @Override
            protected String doInBackground() throws Exception {
                List<Customer> cus = cusApiClient.search(trimmedSel); // Perform the API call
                if (cus != null && !cus.isEmpty()) {
                    return cus.getFirst().getTenKh(); // Return the found customer name
                }
                return null; // No customer found
            }

            @Override
            protected void done() { // This method runs on the EDT
                try {
                    String foundCustomerName = get(); // Get the result from doInBackground
                    currentCustomerName = foundCustomerName; // Update the presenter's state
                    view.updateCustomerNameDisplay(currentCustomerName); // Update the UI
                    System.out.println("DEBUG (onCustomerSelected.done): Customer search result: " + (currentCustomerName != null ? currentCustomerName : "No customer found"));
                } catch (Exception ex) {
                    Logger.getLogger(MainMenuPresenter.class.getName()).log(Level.SEVERE, null, ex);
                    view.showErrorMessage("Lỗi khi tìm khách hàng: " + ex.getMessage());
                    currentCustomerName = null; // Ensure null on error
                    view.updateCustomerNameDisplay(null); // Clear display on error
                } finally {
                    // Always recalculate total after customer selection process, regardless of success or failure.
                    recalcTotal();
                }
            }
        }.execute(); // Start the SwingWorker
    }
          public void searchSP(String tenSP) {
        if (tenSP == null || tenSP.trim().isEmpty()) {
            loadAndDisplayBooksByCategories(); 
            return;
        }
        new SwingWorker<LinkedHashMap<String, ArrayList<Book>>, Void>() {
            private boolean noResultsFound = false;

            @Override
            protected LinkedHashMap<String, ArrayList<Book>> doInBackground() throws Exception {
                LinkedHashMap<String, ArrayList<Book>> booksByCat = new LinkedHashMap<>();

                List<Category> categories = categoryApiClient.getAllCategories();
                for (Category dm : categories) {
                    List<Book> booksInThisCategoryFromApi = bookApiClient.getBooksByCategoryId(dm.getMaDanhMuc());

                    ArrayList<Book> filteredBooksInThisCategory = new ArrayList<>();
                    for (Book book : booksInThisCategoryFromApi) {
                        if (book.getTenSach() != null && book.getTenSach().toLowerCase().contains(tenSP.toLowerCase())) {
                            filteredBooksInThisCategory.add(book);
                        }
                    }
                    if (!filteredBooksInThisCategory.isEmpty()) {
                        booksByCat.put(dm.getTenDanhMuc(), filteredBooksInThisCategory);
                    }
                }

                noResultsFound = booksByCat.isEmpty() || booksByCat.values().stream().allMatch(List::isEmpty);

                return booksByCat;
            }

            @Override
            protected void done() {
               
                try {
                    LinkedHashMap<String, ArrayList<Book>> categorizedBooks = get();
                    view.populateMaterialCategoryTabs(categorizedBooks); 
                    if (noResultsFound) {
                        view.showMessage("Không tìm thấy sản phẩm nào phù hợp với từ khóa '" + tenSP + "'.");
                    }
                } catch (Exception ex) {
                    // Xử lý lỗi nếu có trong quá trình tải dữ liệu
                    Logger.getLogger(MainMenuPresenter.class.getName()).log(Level.SEVERE, null, ex);
                    view.showErrorMessage("Lỗi khi tìm kiếm sách: " + ex.getMessage() + "\nĐảm bảo backend đang chạy và BASE_URL đúng.");
                }
            }
        }.execute(); // Bắt đầu chạy SwingWorker
    }

    /* Bất cứ khi nào bảng hóa đơn thay đổi, gọi hàm này */
    private void recalcTotal() {

        view.updateReceiptTotal(currentCustomerName != null);  // truyền cờ giảm giá
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
public void onCheckoutClicked(String tenkh, String tennv) {
    DefaultTableModel model = view.getReceiptTableModel();

    if (model.getRowCount() == 0) {
        view.showMessage("Hoá đơn đang trống.");
        return;
    }

    double tongTien = view.LayTongTien();

    Order newOrder = new Order(tenkh, tongTien, tennv);

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
                List<OD> ctdhs = new ArrayList<>();
                
                if (addedOrder != null) {
                    view.showMessage("Thêm đơn hàng thành công.");

                    // Sau khi thêm Order thành công => thêm chi tiết đơn hàng (OD)
                    AtomicInteger completedCount = new AtomicInteger(0);
                    int totalRows = model.getRowCount();

                    for (int row = 0; row < totalRows; row++) {
                        OD detail = new OD();
                        detail.setMaDH(addedOrder.getMaDH());
                        Object tenSPObj = model.getValueAt(row, 0);
                            if (tenSPObj == null || tenSPObj.toString().trim().isEmpty()) {
                                view.showErrorMessage("Tên sản phẩm ở dòng " + (row + 1) + " đang bị trống.");
                                return; // hoặc continue; nếu bạn muốn bỏ qua dòng đó
                            }
                            detail.setTenSach(tenSPObj.toString());

                        detail.setSoLuong((Integer) model.getValueAt(row, 1));
                        detail.setDonGia(parseMoney(model.getValueAt(row, 2).toString()));
                        detail.setTongTien(parseMoney(model.getValueAt(row, 3).toString()));
                        

                        new SwingWorker<OD, Void>() {
                            @Override
                            protected OD doInBackground() throws Exception {
                                System.out.println("Đang thêm CTDH: tenSP=" + detail.getTenSach()+ ", soLuong=" + detail.getSoLuong() + ", donGia=" + detail.getDonGia()+ "tongtien= "+ detail.getTongTien());

                                return odApiClient.addOD(detail);
                            }

                            @Override
                            protected void done() {
                                try {
                                    OD addedOD = get();
                                    if (addedOD == null) {
                                        view.showErrorMessage("Thêm CTDH thất bại.");
                                    }
                                    else{
                                         String tenSP = detail.getTenSach();
                                        int soLuongMua = detail.getSoLuong();
                                        // --- START: Added code for book quantity update ---
                                        new SwingWorker<Void, Void>() {
                                            @Override
                                            protected Void doInBackground() throws Exception {
                                               
                                                List<Book> books = bookApiClient.searchBooks(tenSP);
                                                if (books != null && !books.isEmpty()) {
                                                    Book bookToUpdate = books.get(0); // Assuming the first result is the correct book
                                                    int currentSoLuong = bookToUpdate.getSoLuong();
                                                    bookToUpdate.setSoLuong(currentSoLuong - soLuongMua);
                                                    bookApiClient.updateBook(bookToUpdate.getMaSach(),bookToUpdate); // Update the book
                                                } else {
                                                    throw new Exception("Không tìm thấy sách với tên: " + tenSP);
                                                }
                                                return null;
                                            }

                                            @Override
                                            protected void done() {
                                                try {
                                                    get(); // Check for exceptions
                                                    System.out.println("Cập nhật số lượng sách " + tenSP + " thành công.");
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                    view.showErrorMessage("Lỗi khi cập nhật số lượng sách " + tenSP + ": " + ex.getMessage());
                                                }
                                            }
                                        }.execute();
                                        ctdhs.add(detail);
                                        // --- END: Added code for book quantity update ---
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    view.showErrorMessage("Lỗi khi thêm CTDH: " + e.getMessage());
                                } finally {
                                    // Khi tất cả CTDH đã xử lý => Xoá bảng
                                    if (completedCount.incrementAndGet() == totalRows) {
                                        String filePath = "C:/Users/Admin/hoadon.pdf";
                                        exportInvoiceToPDF(filePath, addedOrder, ctdhs);
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
public static void exportInvoiceToPDF(String filePath, Order dh, List<OD> ctdhs) {
        Rectangle pageSize = new Rectangle(298, 420); // A6 in points (1 point = 1/72 inch)
        Document document = new Document(pageSize, 20, 20, 20, 20); // A6 paper

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // ============ TIÊU ĐỀ ============
            BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 14, Font.BOLD);

            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            Font fonthg = new Font(bf, 12);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Mã ĐH: " + dh.getMaDH(),fonthg));
            document.add(new Paragraph("Ngày: " + dh.getNgayBan(),fonthg));
            document.add(new Paragraph("Khách: " + (dh.getTenKH() != null ? dh.getTenKH() : "Khách lẻ"),fonthg));
            document.add(new Paragraph("Nhân viên: " + dh.getTennv(),fonthg));

            document.add(new Paragraph(" "));

            // ============ BẢNG SẢN PHẨM ============
            PdfPTable table = new PdfPTable(4);
            table.setWidths(new float[]{3, 1, 2, 2});
            table.setWidthPercentage(100);
            table.setSpacingBefore(5f);
            table.setSpacingAfter(5f);

            table.addCell("SP");
            table.addCell("SL");
            table.addCell(new Phrase("Đ. Giá", fonthg));
            table.addCell(new Phrase("Th. Tiền", fonthg));
            for (OD ct : ctdhs) {
                table.addCell(new Phrase(ct.getTenSach(), fonthg));
                table.addCell(String.valueOf(ct.getSoLuong()));
                table.addCell(String.format("%,.0f", ct.getDonGia()));
                table.addCell(String.format("%,.0f", ct.getSoLuong() * ct.getDonGia()));
            }

            document.add(table);

            // ============ TỔNG TIỀN ============
            Paragraph total = new Paragraph("Tổng: " + String.format("%,.0f", dh.getTongTien()) + " $",
                    new Font(bf, 12, Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // ============ CẢM ƠN ============
            document.add(new Paragraph(" "));
            Paragraph thanks = new Paragraph("Cảm ơn quý khách!", new Font(bf, 12));
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(filePath)); // Mở file bằng phần mềm mặc định trên máy
                } else {
                    System.out.println("Desktop không được hỗ trợ trên hệ điều hành này.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Không thể mở hóa đơn: " + e.getMessage());
            }
        }
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