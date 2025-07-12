/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Presenter.MainMenuPresenter;
import View.interfaces.IMainMenu;
import Model.Book;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL; // Import URL
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO; // Import ImageIO
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Admin
 */
public final class MainMenu extends javax.swing.JFrame implements IMainMenu{
    private MainMenuPresenter presenter;
private Book currentSelectedBook;
private DefaultTableModel tblModelHD;
/** Lưu lại dữ liệu để tái vẽ khi đổi trang */
private LinkedHashMap<String, ArrayList<Book>> cachedCategories = new LinkedHashMap<>();

private int currentCategoryIndex = 0;   // Tab danh mục đang xem
private int currentPage          = 0;   // Trang (0‑based) trong danh mục đó
private final int BOOKS_PER_PAGE = 5;
private boolean isManager;
    /**
     * Creates new form MainMenu
     */
 private static final String API_BASE_URL = "http://localhost:8080/api"; 

    public MainMenu() {
    }
 
    public MainMenu(boolean isManager, String tenNV) {
         this.isManager = isManager;

        initComponents(); 
        setLocationRelativeTo(null);
        tblModelHD = (DefaultTableModel) jtblHD.getModel();
        tblModelHD.setColumnCount(0);
        tblModelHD.addColumn("Tên sách");
        tblModelHD.addColumn("Số lượng");
        tblModelHD.addColumn("Đơn giá");
        tblModelHD.addColumn("Tổng giá");

        presenter = new MainMenuPresenter(this); // Khởi tạo Presenter và truyền View vào
        jtxtTenNV.setText(tenNV);

        jbtnThemHD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Giao tiếp với Presenter khi có sự kiện
                presenter.onAddReceiptItemClicked((Integer) jspnSL.getValue());
            }
        });
        jbtnTT.addActionListener(e -> {
            // 1) Kiểm tra bảng hóa đơn
            if (tblModelHD.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                    MainMenu.this,
                    "Hoá đơn đang trống – không thể thanh toán.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }
            if(jtxtTenKH.getText().matches("\\d+")){
                showErrorMessage("Tên khách hàng đang là số");
                return;
            }
            String sel = jtxtTenKH.getText();
            String tenKH = (sel == null ||
                            sel.trim().isEmpty())
                          ? null   // <‑‑ truyền null
                          : sel.trim();

            // 3) Gọi Controller
            presenter.onCheckoutClicked(tenKH, jtxtTenNV.getText().trim());
        });
        jbtnDX.addActionListener(e -> {
            Login lg = new Login();
                lg.setVisible(true);
                this.dispose();
        });
        jbtnPreviousPage.addActionListener(e -> {
               currentPage--;
            renderCurrentPage();
        });
        jbtnNextPage.addActionListener(e -> {
               currentPage++;
            renderCurrentPage();
        });
        jbtnTim.addActionListener(e ->{
         presenter.searchSP(jtxtTenSachTK.getText().trim());
    });
         jmTabBooks.addChangeListener(e -> {
        currentCategoryIndex = jmTabBooks.getSelectedIndex();
        currentPage = 0;
        renderCurrentPage();
    });
        jTotalPd.setText("0"); // Giá trị ban đầu

        presenter.loadAndDisplayBooksByCategories(); // Yêu cầu Presenter tải dữ liệu
        jbtnTimKH.addActionListener(e -> {
            presenter.onCustomerSelected(jtxtTenKH.getText());
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // presenter.closeDatabaseConnection(); // Presenter không còn quản lý kết nối DB trực tiếp
            }
        });
        clearReceiptTable();
    }
    @Override
    public DefaultTableModel getReceiptTableModel() {
    return tblModelHD;
}
     @Override
    public void updateCustomerNameDisplay(String customerName) {
        // Assuming jtxtTenKH is the JTextField where the customer name is displayed
        jtxtTenKH.setText(customerName != null ? customerName : "");
    }
    @Override
    public double LayTongTien(){
        return Double.parseDouble(jtxtTongTienHD.getText().replace("$", "").replace(",", "").trim());
    }
    @Override
public void populateMaterialCategoryTabs(LinkedHashMap<String, ArrayList<Book>> categorizedBooks) {

// 1. Lưu cache & reset vị trí
    cachedCategories.clear();
    cachedCategories.putAll(categorizedBooks);
    currentCategoryIndex = 0;
    currentPage = 0;
    // 2. Tạo tab cho mỗi danh mục (chỉ chứa JScrollPane rỗng ban đầu)
    jmTabBooks.removeAll();
    jmTabBooks.setPreferredSize(new Dimension(536,461));
    /* ==== 1. Tab cho sách (như cũ) ==== */
            for (Map.Entry<String, ArrayList<Book>> e : categorizedBooks.entrySet()) {
                addBookTab(e.getKey(), e.getValue());
            }
            renderCurrentPage();
           
}
private void addBookTab(String title, ArrayList<Book> books) {
    // This panel will hold the BookItemPanels in a grid
    JPanel gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
    gridPanel.setBorder(new EmptyBorder(5, 20, 5, 20));

    if (books.isEmpty()) {
        gridPanel.add(new JLabel("Không có sách trong danh mục này."));
    } else {
        for (Book b : books) {
            if(b.getSoLuong()<=0){
                continue;
            }
            BookItemPanel p = new BookItemPanel(b, presenter);
            p.setBookData(b);
            gridPanel.add(p);
        }
    }

    // Create a wrapper panel to contain the gridPanel.
    // This helps the JScrollPane understand the preferred width.
    JPanel wrapperPanel = new JPanel(new BorderLayout());
    wrapperPanel.add(gridPanel, BorderLayout.NORTH); // Add to NORTH to prevent stretching vertically

    // Use the wrapperPanel in the JScrollPane
    jmTabBooks.addTab(title, new JScrollPane(wrapperPanel));
}
private void renderCurrentPage() {
    int tabCount = jmTabBooks.getTabCount();

    // Ngăn lỗi nếu index không hợp lệ
    if (currentCategoryIndex < 0 || currentCategoryIndex >= tabCount) return;

    String categoryName = (String) jmTabBooks.getTitleAt(currentCategoryIndex);
    ArrayList<Book> books = cachedCategories.get(categoryName);
    if (books == null) books = new ArrayList<>(); // tránh null pointer

    // Tính totalPages
    int totalPages = (int) Math.ceil(books.size() / (double) BOOKS_PER_PAGE);
    currentPage = Math.max(0, Math.min(currentPage, totalPages - 1));

    // Lấy sub-list sách cho trang hiện tại
    int start = currentPage * BOOKS_PER_PAGE;
    int end   = Math.min(start + BOOKS_PER_PAGE, books.size());
    List<Book> pageBooks = books.subList(start, end);

    // Tạo panel chứa các sách trên trang hiện tại với GridLayout
    JPanel gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
    gridPanel.setBorder(new EmptyBorder(5, 20, 5, 20)); // Apply border consistently

    if (pageBooks.isEmpty()) {
        gridPanel.add(new JLabel("Không có sách trong danh mục này."));
    } else {
        for (Book book : pageBooks) {
            if(book.getSoLuong() <= 0) { // Check quantity consistently
                continue;
            }
            BookItemPanel p = new BookItemPanel(book, presenter);
            p.setBookData(book);
            gridPanel.add(p);
        }
    }

    // *** Thêm một wrapper panel với BorderLayout.NORTH để ngăn giãn dọc ***
    JPanel wrapperPanelForPage = new JPanel(new BorderLayout());
    wrapperPanelForPage.add(gridPanel, BorderLayout.NORTH);

    // Cập nhật JScrollPane của tab
    Component comp = jmTabBooks.getComponentAt(currentCategoryIndex);
    if (comp instanceof JScrollPane scroll) {
        scroll.setViewportView(wrapperPanelForPage); // Set the wrapper panel as the new view
    }

    // Cập nhật label/nút trang
    jtxtPage.setText((currentPage + 1) + " / " + (totalPages == 0 ? 1 : totalPages));
    jbtnPreviousPage.setEnabled(currentPage > 0);
    jbtnNextPage.setEnabled(currentPage < totalPages - 1);
}

       @Override
    public void addReceiptItem(Object[] rowData) {
        tblModelHD.addRow(rowData);
    }
    @Override
     public void updateSelectedBook(Book book) {
        this.currentSelectedBook = book;
        if (book != null) {
            jtxtTenSachHD.setText(book.getTenSach());
            jspnSL.setValue(1); 
            jbtnThemHD.setEnabled(true);
        } else {
            jtxtTenSachHD.setText("Chọn một cuốn sách...");
            jspnSL.setValue(1);
            jbtnThemHD.setEnabled(false); 
        }
    }
      

    @Override
    public void updateReceiptTotal(boolean hasDiscount) {
        double totalAmount = 0;
        int    totalItems  = 0;

        for (int i = 0; i < tblModelHD.getRowCount(); i++) {
            String moneyStr = tblModelHD.getValueAt(i, 3).toString()
                                       .replace("$", "");
            totalAmount += Double.parseDouble(moneyStr);
            totalItems  += (Integer) tblModelHD.getValueAt(i, 1);
        }

        if (hasDiscount) {
            jtxtGG.setText("10%");
            totalAmount *= 0.9;                           // ‑10 %
        }
        else{
           jtxtGG.setText("0%");

        }

        jtxtTongTienHD.setText(String.format("%.0f $", totalAmount));
        jTotalPd.setText(String.valueOf(totalItems));
    }
    
    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    @Override
    public void clearReceiptTable() {
    // Xoá tất cả các dòng
    tblModelHD.setRowCount(0);

    // Cập nhật lại tổng số lượng & tổng tiền về 0
    jTotalPd.setText("0");
    jtxtTongTienHD.setText("0 $");

    // Nếu muốn vô hiệu hoá nút “Thêm” sau khi xoá
    updateSelectedBook(null);
}
    
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMain = new javax.swing.JPanel();
        jLeft = new javax.swing.JPanel();
        jBanner = new javax.swing.JPanel();
        jbtnQli = new javax.swing.JButton();
        jbtnPreviousPage = new javax.swing.JButton();
        jbtnNextPage = new javax.swing.JButton();
        jtxtPage = new javax.swing.JLabel();
        jMiddle = new javax.swing.JPanel();
        jmTabBooks = new View.MaterialTabbed();
        jMiddle3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jtxtTenSachHD = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jspnSL = new javax.swing.JSpinner();
        jbtnThemHD = new javax.swing.JButton();
        jBottom = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtxtTenSachTK = new javax.swing.JTextField();
        jbtnTim = new javax.swing.JButton();
        jRight = new javax.swing.JPanel();
        jBanner2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jbtnDX = new javax.swing.JButton();
        jList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblHD = new javax.swing.JTable();
        jUnder = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTotalPd = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtxtTongTienHD = new javax.swing.JLabel();
        jbtnTT = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtxtGG = new javax.swing.JLabel();
        jtxtTenKH = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtxtTenNV = new javax.swing.JTextField();
        jbtnTimKH = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jBanner.setBackground(new java.awt.Color(0, 0, 102));

        jbtnQli.setBackground(new java.awt.Color(0, 0, 102));
        jbtnQli.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jbtnQli.setForeground(new java.awt.Color(255, 255, 255));
        jbtnQli.setText("Quản lí");
        jbtnQli.setBorder(null);
        jbtnQli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnQliActionPerformed(evt);
            }
        });

        jbtnPreviousPage.setBackground(new java.awt.Color(254, 255, 255));
        jbtnPreviousPage.setText("<");
        jbtnPreviousPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPreviousPageActionPerformed(evt);
            }
        });

        jbtnNextPage.setBackground(new java.awt.Color(254, 255, 255));
        jbtnNextPage.setText(">");
        jbtnNextPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNextPageActionPerformed(evt);
            }
        });

        jtxtPage.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jtxtPage.setForeground(new java.awt.Color(255, 255, 255));
        jtxtPage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtPage.setText("0/0");

        javax.swing.GroupLayout jBannerLayout = new javax.swing.GroupLayout(jBanner);
        jBanner.setLayout(jBannerLayout);
        jBannerLayout.setHorizontalGroup(
            jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBannerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnQli)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnPreviousPage, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtPage, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtnNextPage, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jBannerLayout.setVerticalGroup(
            jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBannerLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbtnQli)
                    .addGroup(jBannerLayout.createSequentialGroup()
                        .addGroup(jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jbtnPreviousPage)
                                .addComponent(jtxtPage))
                            .addComponent(jbtnNextPage))
                        .addGap(5, 5, 5)))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jMiddleLayout = new javax.swing.GroupLayout(jMiddle);
        jMiddle.setLayout(jMiddleLayout);
        jMiddleLayout.setHorizontalGroup(
            jMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jmTabBooks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jMiddleLayout.setVerticalGroup(
            jMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jmTabBooks, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
        );

        jMiddle3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("Tên sách:");

        jLabel7.setText("Số lượng:");

        jbtnThemHD.setText("Thêm vào hóa đơn");
        jbtnThemHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnThemHDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jMiddle3Layout = new javax.swing.GroupLayout(jMiddle3);
        jMiddle3.setLayout(jMiddle3Layout);
        jMiddle3Layout.setHorizontalGroup(
            jMiddle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMiddle3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtTenSachHD, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspnSL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(jbtnThemHD)
                .addContainerGap())
        );
        jMiddle3Layout.setVerticalGroup(
            jMiddle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMiddle3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jMiddle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtxtTenSachHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jspnSL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnThemHD))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jBottom.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Tên sách:");

        jLabel9.setText("Tìm theo tên sách");

        jbtnTim.setBackground(new java.awt.Color(204, 204, 204));
        jbtnTim.setText("Tìm");

        javax.swing.GroupLayout jBottomLayout = new javax.swing.GroupLayout(jBottom);
        jBottom.setLayout(jBottomLayout);
        jBottomLayout.setHorizontalGroup(
            jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBottomLayout.createSequentialGroup()
                .addContainerGap(193, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(191, 191, 191))
            .addGroup(jBottomLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel8)
                .addGap(37, 37, 37)
                .addComponent(jtxtTenSachTK, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addGap(94, 94, 94)
                .addComponent(jbtnTim)
                .addContainerGap())
        );
        jBottomLayout.setVerticalGroup(
            jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jBottomLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtxtTenSachTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnTim))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jLeftLayout = new javax.swing.GroupLayout(jLeft);
        jLeft.setLayout(jLeftLayout);
        jLeftLayout.setHorizontalGroup(
            jLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jMiddle3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLeftLayout.setVerticalGroup(
            jLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLeftLayout.createSequentialGroup()
                .addComponent(jBanner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jMiddle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jMiddle3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jBanner2.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Hóa đơn");

        jbtnDX.setBackground(new java.awt.Color(254, 255, 255));
        jbtnDX.setText("Đăng xuất");

        javax.swing.GroupLayout jBanner2Layout = new javax.swing.GroupLayout(jBanner2);
        jBanner2.setLayout(jBanner2Layout);
        jBanner2Layout.setHorizontalGroup(
            jBanner2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBanner2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnDX)
                .addContainerGap())
        );
        jBanner2Layout.setVerticalGroup(
            jBanner2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBanner2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jBanner2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnDX))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jList.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        jtblHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên sách", "Số lượng", "Đơn giá", "Tổng giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtblHD);

        javax.swing.GroupLayout jListLayout = new javax.swing.GroupLayout(jList);
        jList.setLayout(jListLayout);
        jListLayout.setHorizontalGroup(
            jListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jListLayout.setVerticalGroup(
            jListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
        );

        jUnder.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Tổng sản phẩm:");

        jTotalPd.setText("0");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Tổng tiền:");

        jtxtTongTienHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jtxtTongTienHD.setText("$1000");

        jbtnTT.setBackground(new java.awt.Color(102, 153, 255));
        jbtnTT.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jbtnTT.setForeground(new java.awt.Color(255, 255, 255));
        jbtnTT.setText("Thanh toán");

        jLabel4.setText("Khách hàng:");

        jLabel5.setText("Giảm giá:");

        jtxtGG.setText("0%");

        jLabel11.setText("Tên nhân viên:");

        jtxtTenNV.setEditable(false);

        jbtnTimKH.setBackground(new java.awt.Color(204, 204, 204));
        jbtnTimKH.setText("Tìm");

        javax.swing.GroupLayout jUnderLayout = new javax.swing.GroupLayout(jUnder);
        jUnder.setLayout(jUnderLayout);
        jUnderLayout.setHorizontalGroup(
            jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUnderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jUnderLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtTongTienHD, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnTT, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jUnderLayout.createSequentialGroup()
                        .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jUnderLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jTotalPd, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtGG, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jUnderLayout.createSequentialGroup()
                                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jtxtTenKH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                    .addComponent(jtxtTenNV, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbtnTimKH)))))
                .addContainerGap())
        );
        jUnderLayout.setVerticalGroup(
            jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUnderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTotalPd))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jtxtGG)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnTimKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtxtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jtxtTongTienHD))
                    .addComponent(jbtnTT, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jRightLayout = new javax.swing.GroupLayout(jRight);
        jRight.setLayout(jRightLayout);
        jRightLayout.setHorizontalGroup(
            jRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBanner2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jUnder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jRightLayout.setVerticalGroup(
            jRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jRightLayout.createSequentialGroup()
                .addComponent(jBanner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jUnder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jMainLayout = new javax.swing.GroupLayout(jMain);
        jMain.setLayout(jMainLayout);
        jMainLayout.setHorizontalGroup(
            jMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMainLayout.createSequentialGroup()
                .addComponent(jLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jMainLayout.setVerticalGroup(
            jMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jRight, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnQliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnQliActionPerformed

        // TODO add your handling code here:
         new MainMenu_Manager2(this, presenter, isManager).setVisible(true);
    }//GEN-LAST:event_jbtnQliActionPerformed

    private void jbtnQuanLyNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnQuanLyNhanVienActionPerformed
        // TODO add your handling code here:
        EmployeeM employeeManagementFrame = new EmployeeM(presenter);
        employeeManagementFrame.setVisible(true);
    }//GEN-LAST:event_jbtnQuanLyNhanVienActionPerformed

    private void jbtnThemHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnThemHDActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jbtnThemHDActionPerformed

    private void jbtnPreviousPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPreviousPageActionPerformed

    }//GEN-LAST:event_jbtnPreviousPageActionPerformed

    private void jbtnNextPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNextPageActionPerformed

    }//GEN-LAST:event_jbtnNextPageActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jBanner;
    private javax.swing.JPanel jBanner2;
    private javax.swing.JPanel jBottom;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jLeft;
    private javax.swing.JPanel jList;
    private javax.swing.JPanel jMain;
    private javax.swing.JPanel jMiddle;
    private javax.swing.JPanel jMiddle3;
    private javax.swing.JPanel jRight;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jTotalPd;
    private javax.swing.JPanel jUnder;
    private javax.swing.JButton jbtnDX;
    private javax.swing.JButton jbtnNextPage;
    private javax.swing.JButton jbtnPreviousPage;
    private javax.swing.JButton jbtnQli;
    private javax.swing.JButton jbtnTT;
    private javax.swing.JButton jbtnThemHD;
    private javax.swing.JButton jbtnTim;
    private javax.swing.JButton jbtnTimKH;
    private View.MaterialTabbed jmTabBooks;
    private javax.swing.JSpinner jspnSL;
    private javax.swing.JTable jtblHD;
    private javax.swing.JLabel jtxtGG;
    private javax.swing.JLabel jtxtPage;
    private javax.swing.JTextField jtxtTenKH;
    private javax.swing.JTextField jtxtTenNV;
    private javax.swing.JTextField jtxtTenSachHD;
    private javax.swing.JTextField jtxtTenSachTK;
    private javax.swing.JLabel jtxtTongTienHD;
    // End of variables declaration//GEN-END:variables
}
