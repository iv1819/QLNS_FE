/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Model.Book;
import Presenter.MainMenuPresenter; // Import MainMenuPresenter
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL; // Import URL
import javax.imageio.ImageIO; // Import ImageIO
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Admin
 */
public final class BookItemPanel extends javax.swing.JPanel {
private Book book;
    private MainMenuPresenter mainMenuPresenter; // Tham chiếu đến MainMenuPresenter

    // BASE_URL của API backend (phải khớp với ApiClientBase)
    private static final String API_BASE_URL = "http://localhost:8080"; 
  
    public BookItemPanel(Book book, MainMenuPresenter mainMenuPresenter) {
        this.book = book;
        this.mainMenuPresenter = mainMenuPresenter;
        initComponents();
        setPreferredSize(new Dimension(96, 146));
        jpnlPrice.setPreferredSize(new Dimension(97,37));
        jtxtPriceBI.setPreferredSize(new Dimension(70,25));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setBookData(book); // Đổi tên phương thức để tránh nhầm lẫn với setBook trong Model

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainMenuPresenter != null) {
                    // Thông báo cho Presenter khi sách được click
                    mainMenuPresenter.onBookItemSelected(BookItemPanel.this.book);
                    System.out.println("DEBUG (BookItemPanel): Đã click vào sách: " + BookItemPanel.this.book.getTenSach());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Hiệu ứng khi di chuột vào
                setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Hiệu ứng khi di chuột ra
                setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Viền xám nhạt
            }
        });
    }

    // Constructor mặc định (nếu cần cho GUI builder, nhưng không dùng trong populateCategoryTabs)
    public BookItemPanel() {
        
        this(null, null); // Gọi constructor chính với giá trị null
    }
    
    public void setBookData(Book book) {
        System.out.println("DEBUG (BookItemPanel.setBookData): Đang cập nhật panel cho sách: " + book.getTenSach());

        if (book.getDuongDanAnh() != null && !book.getDuongDanAnh().isEmpty()) {
            try {
                // Tạo URL từ API_BASE_URL và đường dẫn ảnh
                URL imageUrl = new URL(API_BASE_URL + book.getDuongDanAnh()); 
                System.out.println("DEBUG (BookItemPanel): Đang tải ảnh từ URL: " + imageUrl.toString()); 
                BufferedImage originalImage = ImageIO.read(imageUrl);
                
                if (originalImage != null) {
                    // Scale ảnh theo kích thước mong muốn: 58x70 pixel
                    Image scaledImage = originalImage.getScaledInstance(58, 70, Image.SCALE_SMOOTH);
                    jtxtAnhBI.setIcon(new ImageIcon(scaledImage));
                    jtxtAnhBI.setText(""); // Đảm bảo không có văn bản nào hiện ra cùng ảnh
                    System.out.println("DEBUG (BookItemPanel): Ảnh đã tải thành công.");
                } else {
                    jtxtAnhBI.setIcon(null);
                    jtxtAnhBI.setText("Không ảnh"); 
                    System.err.println("LỖI (BookItemPanel): ImageIO.read trả về null.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                jtxtAnhBI.setIcon(null); 
                jtxtAnhBI.setText("Lỗi tải ảnh"); 
                System.err.println("LỖI (BookItemPanel): IOException: " + e.getMessage());
            } catch (Exception e) { // Bắt các ngoại lệ khác như MalformedURLException
                e.printStackTrace();
                jtxtAnhBI.setIcon(null); 
                jtxtAnhBI.setText("Lỗi URL ảnh"); 
                System.err.println("LỖI (BookItemPanel): Lỗi không xác định: " + e.getMessage());
            }
        } else {
            jtxtAnhBI.setIcon(null);
            jtxtAnhBI.setText("Ảnh sách"); 
            System.out.println("DEBUG (BookItemPanel): Đường dẫn ảnh rỗng hoặc null.");
        }

        // Cập nhật tên và giá sách
        // Sử dụng truncateText để cắt ngắn tên (tối đa 15 ký tự)
        jtxtNameBI.setText(truncateText(book.getTenSach(), 15)); 
        jtxtPriceBI.setText(String.format("%.0f $", book.getGiaBan())); 
    }
 private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "..."; // Cắt và thêm "..."
        }
        return text;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtxtNameBI = new javax.swing.JLabel();
        jpnlPrice = new javax.swing.JPanel();
        jtxtPriceBI = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jtxtAnhBI = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(100, 150));

        jtxtNameBI.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jtxtNameBI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtNameBI.setText("Name");
        jtxtNameBI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jpnlPrice.setBackground(new java.awt.Color(102, 153, 255));
        jpnlPrice.setMaximumSize(new java.awt.Dimension(97, 37));

        jtxtPriceBI.setBackground(new java.awt.Color(51, 153, 255));
        jtxtPriceBI.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jtxtPriceBI.setForeground(new java.awt.Color(255, 255, 255));
        jtxtPriceBI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtPriceBI.setText("$");

        javax.swing.GroupLayout jpnlPriceLayout = new javax.swing.GroupLayout(jpnlPrice);
        jpnlPrice.setLayout(jpnlPriceLayout);
        jpnlPriceLayout.setHorizontalGroup(
            jpnlPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jpnlPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpnlPriceLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jtxtPriceBI, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jpnlPriceLayout.setVerticalGroup(
            jpnlPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
            .addGroup(jpnlPriceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpnlPriceLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jtxtPriceBI, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(58, 70));

        jtxtAnhBI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtAnhBI.setText("Anh");
        jtxtAnhBI.setMaximumSize(new java.awt.Dimension(58, 70));
        jtxtAnhBI.setPreferredSize(new java.awt.Dimension(58, 70));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxtAnhBI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtxtAnhBI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnlPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtNameBI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 15, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jtxtNameBI, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpnlPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jpnlPrice;
    private javax.swing.JLabel jtxtAnhBI;
    private javax.swing.JLabel jtxtNameBI;
    private javax.swing.JLabel jtxtPriceBI;
    // End of variables declaration//GEN-END:variables
}
