/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;
import Presenter.BookMPresenter; // Import Controller
import Presenter.MainMenuPresenter;
import Presenter.MainMenuManagerPresenter;
import Model.Book;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import View.interfaces.IBookM;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 *
 * @author Admin
 */
public class BookM extends javax.swing.JFrame implements IBookM{
   private BookMPresenter presenter; // Thay thế controller bằng presenter
    private MainMenuPresenter mainMenuPresenter;
    private MainMenuManagerPresenter mainMenuManagerPresenter;
    private static final String API_BASE_URL = "http://localhost:8080/api"; 
    private String currentImagePath;
    private static String API_BASE_URL_FOR_IMAGES; 
    // Lưu đường dẫn ảnh hiện tại
    /**
     * Creates new form BookM
     */
 public BookM() {
        this((MainMenuPresenter)null);
    }
 public BookM(MainMenuPresenter mainMenuPresenter) {
        this.mainMenuPresenter = mainMenuPresenter; 
        this.mainMenuManagerPresenter = null;
        initComponents();
        
        presenter = new BookMPresenter(this); 
        if (this.mainMenuPresenter != null) {
            presenter.addListener(this.mainMenuPresenter);
            System.out.println("DEBUG (BookM): Đã đăng ký MainMenuPresenter làm listener cho BookMPresenter.");
        } else {
            System.out.println("DEBUG (BookM): MainMenuPresenter là null, không thể đăng ký listener.");
        }

        jTable_Books.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable_Books.getSelectedRow() != -1) {
                    presenter.onBookSelected(jTable_Books.getSelectedRow());
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // presenter.closeDatabaseConnection(); // Presenter không còn quản lý kết nối DB trực tiếp
                if (mainMenuPresenter != null) {
                     presenter.removeListener(mainMenuPresenter); // Hủy đăng ký listener
                     System.out.println("DEBUG (BookM): Đã hủy đăng ký MainMenuPresenter khỏi BookMPresenter.");
                }
            }
        });
    }
    
   @Override
    public String getMaSach() {
        return jtxtMaSach.getText();
    }

    @Override
    public String getTenSach() {
        return jtxtTenSach.getText();
    }

    @Override
    public Integer getSoLuong() {
        try {
            return Integer.parseInt(jtxtSoLuong.getText());
        } catch (NumberFormatException e) {
            return 0; // Hoặc ném ngoại lệ, hoặc trả về null tùy logic validation của bạn
        }
    }

    @Override
    public Double getGiaBan() {
        try {
            return Double.parseDouble(jtxtGia.getText());
        } catch (NumberFormatException e) {
            return 0.0; // Hoặc ném ngoại lệ, hoặc trả về null
        }
    }

    @Override
    public String getTacGia() {
        return (String) jcbxTacGia.getSelectedItem();
    }

    @Override
    public String getNhaXB() {
        return (String) jcbxNhaXB.getSelectedItem();
    }
    
    @Override
    public String getDuongDanAnh() { // <--- TRIỂN KHAI GETTER MỚI
        return jtxtAnh.getText();
    }

    @Override
    public Integer getNamXB() {
        try {
            return Integer.parseInt(jtxtNamXB.getText());
        } catch (NumberFormatException e) {
            return 0; // Hoặc ném ngoại lệ, hoặc trả về null
        }
    }

    @Override
    public String getMaDanhMuc() {
        return (String) jcbxDM.getSelectedItem();
    }

    // Các setters để điền dữ liệu vào form khi chọn sách
    @Override
    public void setMaSach(String maSach) {
        jtxtMaSach.setText(maSach);
    }

    @Override
    public void setTenSach(String tenSach) {
        jtxtTenSach.setText(tenSach);
    }

    @Override
    public void setSoLuong(Integer soLuong) {
        jtxtSoLuong.setText(String.valueOf(soLuong));
    }

    @Override
    public void setGiaBan(Double giaBan) {
        jtxtGia.setText(String.valueOf(giaBan));
    }

    @Override
    public void setTacGia(String tacGia) {
        jcbxTacGia.setSelectedItem(tacGia);
    }

    @Override
    public void setNhaXB(String nhaXB) {
        jcbxNhaXB.setSelectedItem(nhaXB);
    }
    
    @Override
    public void setDuongDanAnh(String duongDanAnh) { // <--- TRIỂN KHAI SETTER MỚI
        jtxtAnh.setText(duongDanAnh);
        this.currentImagePath = duongDanAnh; // Lưu đường dẫn ảnh hiện tại
    }

    @Override
    public void setNamXB(Integer namXB) {
        jtxtNamXB.setText(String.valueOf(namXB));
    }

    @Override
    public void setMaDanhMuc(String maDanhMuc) {
        jcbxDM.setSelectedItem(maDanhMuc);
    }

    @Override
    public void populateNhaXBNames(List<String> names) {
        DefaultComboBoxModel<String> nxbModel = new DefaultComboBoxModel<>();
        for (String name : names) {
            nxbModel.addElement(name);
        }
        jcbxNhaXB.setModel(nxbModel);
    }

    @Override
    public void populateTacGiaNames(List<String> names) {
        DefaultComboBoxModel<String> tgModel = new DefaultComboBoxModel<>();
        for (String name : names) {
            tgModel.addElement(name);
        }
        jcbxTacGia.setModel(tgModel);
    }

    @Override
    public void populateDanhMucNames(List<String> names) {
        DefaultComboBoxModel<String> dmModel = new DefaultComboBoxModel<>();
        for (String name : names) {
            dmModel.addElement(name);
        }
        jcbxDM.setModel(dmModel);
    }
    @Override
    public void displayBooks(ArrayList<Book> books) {
        DefaultTableModel dtm = (DefaultTableModel) jTable_Books.getModel();
        dtm.setRowCount(0);
        dtm.setColumnCount(0);

        dtm.addColumn("Mã sách");
        dtm.addColumn("Tên sách");
        dtm.addColumn("Số lượng");
        dtm.addColumn("Giá");
        dtm.addColumn("Tác giả");
        dtm.addColumn("Nhà XB");
        dtm.addColumn("Mã DM");
        dtm.addColumn("Năm XB");
        dtm.addColumn("Ảnh");

        for (Book book : books) {
            Vector<Object> row = new Vector<>();
            row.add(book.getMaSach());
            row.add(book.getTenSach());
            row.add(book.getSoLuong());
            row.add(book.getGiaBan());
            row.add(book.getTacGia());
            row.add(book.getNhaXB());
            row.add(book.getMaDanhMuc());
            row.add(book.getNamXB());
            row.add(book.getDuongDanAnh()); // Lưu URL tương đối vào bảng
            dtm.addRow(row);
        }
    }

    @Override
    public void populateBookDetails(Book book) {
        jtxtMaSach.setText(book.getMaSach());
        jtxtTenSach.setText(book.getTenSach());
        jtxtSoLuong.setText(String.valueOf(book.getSoLuong()));
        jtxtGia.setText(String.valueOf(book.getGiaBan()));

        // Set ComboBoxes
        ((DefaultComboBoxModel<String>)jcbxTacGia.getModel()).setSelectedItem(book.getTacGia());
        ((DefaultComboBoxModel<String>)jcbxNhaXB.getModel()).setSelectedItem(book.getNhaXB());
        ((DefaultComboBoxModel<String>)jcbxDM.getModel()).setSelectedItem(book.getMaDanhMuc());
        

        jtxtNamXB.setText(String.valueOf(book.getNamXB()));
        jtxtAnh.setText(book.getDuongDanAnh());
        // Cập nhật ảnh preview từ URL
        if (book.getDuongDanAnh() != null && !book.getDuongDanAnh().isEmpty()) {
            updateImagePreview(book.getDuongDanAnh());
        }
        
    }

    @Override
        public void clearForm() {
            jtxtMaSach.setText("");
            jtxtTenSach.setText("");
            jtxtSoLuong.setText("");
            jtxtGia.setText("");
            jtxtNamXB.setText("");
            jtxtAnh.setText("");
            // Reset ComboBoxes về trạng thái không chọn hoặc chọn mục đầu tiên nếu có
            jcbxTacGia.setSelectedIndex(0); 
            jcbxNhaXB.setSelectedIndex(0);
            jcbxDM.setSelectedIndex(0);
            
            updateImagePreview(""); // Xóa ảnh preview
            jTable_Books.clearSelection();
        }
 @Override
        public String getSelectedMaSach() {
            int selectedRow = jTable_Books.getSelectedRow();
            if (selectedRow != -1) {
                Object id = jTable_Books.getValueAt(selectedRow, 0);
                return id != null ? id.toString() : null;
            }
            return null;
        }
    
        
        public static void setApiBaseUrlForImages(String url) {
            API_BASE_URL_FOR_IMAGES = url;
        }
       
 @Override
public void updateImagePreview(String imagePath) {
    jtxtAnh.setText(imagePath);

    JImage.setIcon(null);
    JImage.setText("Ảnh sách");

    if (imagePath != null && !imagePath.isEmpty()) {
        try {
            URL imageUrl;

            // Xác định loại đường dẫn
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                imageUrl = new URL(imagePath); // Là URL đầy đủ
            } else if (new File(imagePath).exists()) {
                imageUrl = new File(imagePath).toURI().toURL(); // File cục bộ
            } else {
                imageUrl = new URL(API_BASE_URL_FOR_IMAGES + imagePath); // Đường dẫn tương đối
            }

            // Tải ảnh bằng URLConnection để tránh lỗi ép kiểu
            URLConnection connection = imageUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            BufferedImage originalImage;

            // Kiểm tra loại kết nối
            if (connection instanceof HttpURLConnection httpConn) {
                if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP " + httpConn.getResponseCode() + " - " + httpConn.getResponseMessage());
                }
                originalImage = ImageIO.read(httpConn.getInputStream());
            } else {
                originalImage = ImageIO.read(connection.getInputStream());
            }

            if (originalImage != null) {

                Image scaledImage = originalImage.getScaledInstance(80, 100, Image.SCALE_SMOOTH);
                JImage.setIcon(new ImageIcon(scaledImage));
                JImage.setText("");
            } else {
                JImage.setText("Không ảnh");
            }
        } catch (IOException e) {
            System.err.println("Lỗi tải ảnh từ URL/File: " + imagePath + " - " + e.getMessage());
            JImage.setText("Lỗi tải ảnh");
        }
    }
}

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox2 = new javax.swing.JComboBox<>();
        JPanel_Top = new javax.swing.JPanel();
        JUpper = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jbtnThem = new javax.swing.JButton();
        jbtnSua = new javax.swing.JButton();
        jbtnXoa = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        JMiddle = new javax.swing.JPanel();
        JMaSach = new javax.swing.JLabel();
        JTenSach = new javax.swing.JLabel();
        JTacGia = new javax.swing.JLabel();
        jcbxTacGia = new javax.swing.JComboBox<>();
        jtxtMaSach = new javax.swing.JTextField();
        jtxtTenSach = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jcbxNhaXB = new javax.swing.JComboBox<>();
        jtxtSoLuong = new javax.swing.JTextField();
        jtxtGia = new javax.swing.JTextField();
        jtxtNamXB = new javax.swing.JTextField();
        jbtnAnh = new javax.swing.JButton();
        jtxtAnh = new javax.swing.JTextField();
        JImage = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jcbxDM = new javax.swing.JComboBox<>();
        JBottom = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Books = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jtxtTimTenSach = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtxtTimTenTG = new javax.swing.JTextField();
        jbtnTim = new javax.swing.JButton();

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 12)); // NOI18N
        setMaximumSize(new java.awt.Dimension(150, 40));

        JPanel_Top.setBackground(new java.awt.Color(255, 255, 255));
        JPanel_Top.setForeground(new java.awt.Color(242, 242, 242));
        JPanel_Top.setFocusTraversalPolicyProvider(true);

        JUpper.setBackground(new java.awt.Color(0, 0, 102));
        JUpper.setForeground(new java.awt.Color(242, 242, 242));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Quản lí Sách");

        jbtnThem.setText("Thêm");
        jbtnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnThemActionPerformed(evt);
            }
        });

        jbtnSua.setText("Sửa");
        jbtnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSuaActionPerformed(evt);
            }
        });

        jbtnXoa.setText("Xóa");
        jbtnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnXoaActionPerformed(evt);
            }
        });

        btnBack.setText("Quay lại");

        javax.swing.GroupLayout JUpperLayout = new javax.swing.GroupLayout(JUpper);
        JUpper.setLayout(JUpperLayout);
        JUpperLayout.setHorizontalGroup(
            JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JUpperLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(279, 279, 279))
            .addGroup(JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(JUpperLayout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
                    .addComponent(jbtnThem)
                    .addGap(18, 18, 18)
                    .addComponent(jbtnSua)
                    .addGap(18, 18, 18)
                    .addComponent(jbtnXoa)
                    .addGap(10, 10, 10)))
        );
        JUpperLayout.setVerticalGroup(
            JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JUpperLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnBack)
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(JUpperLayout.createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addGroup(JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbtnThem)
                            .addComponent(jbtnSua)
                            .addComponent(jbtnXoa))
                        .addComponent(jLabel1))
                    .addContainerGap(21, Short.MAX_VALUE)))
        );

        JMiddle.setBackground(new java.awt.Color(255, 255, 255));

        JMaSach.setText("Mã sách");

        JTenSach.setText("Tên sách");

        JTacGia.setText("Tác giả");

        jcbxTacGia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jtxtMaSach.setEditable(false);

        jLabel2.setText("Nhà xuất bản");

        jLabel3.setText("Số lượng");

        jLabel4.setText("Giá");

        jLabel5.setText("Ảnh");

        jLabel6.setText("Năm xuất bản");

        jcbxNhaXB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jbtnAnh.setBackground(new java.awt.Color(204, 204, 204));
        jbtnAnh.setText("Chọn ảnh");
        jbtnAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAnhActionPerformed(evt);
            }
        });

        jtxtAnh.setEditable(false);

        JImage.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        JImage.setText("Ảnh");
        JImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        JImage.setPreferredSize(new java.awt.Dimension(70, 70));

        jLabel10.setText("Danh mục");

        jcbxDM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout JMiddleLayout = new javax.swing.GroupLayout(JMiddle);
        JMiddle.setLayout(JMiddleLayout);
        JMiddleLayout.setHorizontalGroup(
            JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JMiddleLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(JMaSach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JTenSach, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(JTacGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JMiddleLayout.createSequentialGroup()
                        .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JMiddleLayout.createSequentialGroup()
                                .addComponent(JImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jtxtTenSach, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtMaSach, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(133, 133, 133))
                    .addGroup(JMiddleLayout.createSequentialGroup()
                        .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jcbxTacGia, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtxtAnh))
                        .addGap(18, 18, 18)
                        .addComponent(jbtnAnh)
                        .addGap(35, 35, 35)))
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtxtGia)
                    .addComponent(jtxtNamXB, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtxtSoLuong)
                    .addComponent(jcbxDM, 0, 227, Short.MAX_VALUE)
                    .addComponent(jcbxNhaXB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(64, 64, 64))
        );
        JMiddleLayout.setVerticalGroup(
            JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JMiddleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JMaSach)
                    .addComponent(jtxtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jcbxNhaXB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JTenSach)
                    .addComponent(jtxtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JTacGia)
                    .addComponent(jcbxTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jtxtNamXB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnAnh)
                    .addComponent(jtxtAnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JMiddleLayout.createSequentialGroup()
                        .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jcbxDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(JImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jTable_Books);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Tìm kiếm theo tên sách và tên tác giả");

        jLabel8.setText("Tên sách");

        jLabel9.setText("Tên tác giả");

        jbtnTim.setText("Tìm kiếm");
        jbtnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnTimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JBottomLayout = new javax.swing.GroupLayout(JBottom);
        JBottom.setLayout(JBottomLayout);
        JBottomLayout.setHorizontalGroup(
            JBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JBottomLayout.createSequentialGroup()
                .addGap(234, 234, 234)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(JBottomLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtTimTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtTimTenTG, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                .addComponent(jbtnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addComponent(jScrollPane1)
        );
        JBottomLayout.setVerticalGroup(
            JBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtxtTimTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jtxtTimTenTG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnTim))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JPanel_TopLayout = new javax.swing.GroupLayout(JPanel_Top);
        JPanel_Top.setLayout(JPanel_TopLayout);
        JPanel_TopLayout.setHorizontalGroup(
            JPanel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JUpper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(JMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(JPanel_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanel_TopLayout.setVerticalGroup(
            JPanel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanel_TopLayout.createSequentialGroup()
                .addComponent(JUpper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(JMiddle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(JPanel_Top, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnThemActionPerformed
        // TODO add your handling code here:
       presenter.addBook(); 
    }//GEN-LAST:event_jbtnThemActionPerformed

    private void jbtnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSuaActionPerformed
       presenter.updateBook();
    }//GEN-LAST:event_jbtnSuaActionPerformed

    private void jbtnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnXoaActionPerformed
        // TODO add your handling code here:
         String maSach = jtxtMaSach.getText();
        if (maSach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sách này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            presenter.deleteBook(maSach); // Yêu cầu Presenter xóa sách
        }
    }//GEN-LAST:event_jbtnXoaActionPerformed

    private void jbtnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTimActionPerformed
        // TODO add your handling code here:
      String tenSach = jtxtTimTenSach.getText();
        String tenTacGia = jtxtTimTenTG.getText();
        presenter.searchBooks(tenSach, tenTacGia);
    }//GEN-LAST:event_jbtnTimActionPerformed

    private void jbtnAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAnhActionPerformed
        JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Chọn ảnh sách");
                    // Chỉ cho phép chọn file ảnh
                    fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        public boolean accept(File f) {
                            if (f.isDirectory()) {
                                return true;
                            }
                            String filename = f.getName().toLowerCase();
                            return filename.endsWith(".jpg") || filename.endsWith(".jpeg") ||
                                   filename.endsWith(".png") || filename.endsWith(".gif");
                        }
                        public String getDescription() {
                            return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
                        }
                    });

                    int userSelection = fileChooser.showOpenDialog(BookM.this);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        if (presenter != null) {
                            presenter.onImageSelectionRequested(selectedFile); // Gửi file được chọn đến Presenter
                        }
                    }
    }//GEN-LAST:event_jbtnAnhActionPerformed

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
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
            java.util.logging.Logger.getLogger(BookM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BookM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BookM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BookM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BookM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JBottom;
    private javax.swing.JLabel JImage;
    private javax.swing.JLabel JMaSach;
    private javax.swing.JPanel JMiddle;
    private javax.swing.JPanel JPanel_Top;
    private javax.swing.JLabel JTacGia;
    private javax.swing.JLabel JTenSach;
    private javax.swing.JPanel JUpper;
    private javax.swing.JButton btnBack;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Books;
    private javax.swing.JButton jbtnAnh;
    private javax.swing.JButton jbtnSua;
    private javax.swing.JButton jbtnThem;
    private javax.swing.JButton jbtnTim;
    private javax.swing.JButton jbtnXoa;
    private javax.swing.JComboBox<String> jcbxDM;
    private javax.swing.JComboBox<String> jcbxNhaXB;
    private javax.swing.JComboBox<String> jcbxTacGia;
    private javax.swing.JTextField jtxtAnh;
    private javax.swing.JTextField jtxtGia;
    private javax.swing.JTextField jtxtMaSach;
    private javax.swing.JTextField jtxtNamXB;
    private javax.swing.JTextField jtxtSoLuong;
    private javax.swing.JTextField jtxtTenSach;
    private javax.swing.JTextField jtxtTimTenSach;
    private javax.swing.JTextField jtxtTimTenTG;
    // End of variables declaration//GEN-END:variables
}
