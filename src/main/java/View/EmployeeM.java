/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Presenter.EmployeeMPresenter;
import Presenter.MainMenuPresenter;
import Presenter.MainMenuManagerPresenter;
import Model.Employee;
import View.MainMenu;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import View.interfaces.IEmployeeM;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.math.BigDecimal;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author MSI GF63
 */
public class EmployeeM extends javax.swing.JFrame implements IEmployeeM {
    
    private EmployeeMPresenter presenter;
    private MainMenuPresenter mainMenuPresenter;
    private MainMenuManagerPresenter mainMenuManagerPresenter;
    private boolean isEditing = false; // Flag để biết đang ở chế độ chỉnh sửa hay thêm mới

    /**
     * Creates new form EmployeeM
     */
    public EmployeeM() {
        this((MainMenuPresenter)null);
    }
    
    public EmployeeM(MainMenuPresenter mainMenuPresenter) {
        this.mainMenuPresenter = mainMenuPresenter;
        this.mainMenuManagerPresenter = null;
        initComponents();
        // Đảm bảo gán ActionListener cho tất cả các nút
        btnBack.addActionListener(evt -> btnBackActionPerformed(evt));
        btnRefresh.addActionListener(evt -> btnRefreshActionPerformed(evt));
        btnThem.addActionListener(evt -> btnThemActionPerformed(evt));
        btnSua.addActionListener(evt -> btnSuaActionPerformed(evt));
        btnXoa.addActionListener(evt -> btnXoaActionPerformed(evt));
        btnTimKiem.addActionListener(evt -> btnTimKiemActionPerformed(evt));
        
        presenter = new EmployeeMPresenter(this);
        if (this.mainMenuPresenter != null) {
            presenter.addListener(this.mainMenuPresenter);
            System.out.println("DEBUG (EmployeeM): Đã đăng ký MainMenuPresenter làm listener cho EmployeeMPresenter.");
        } else {
            System.out.println("DEBUG (EmployeeM): MainMenuPresenter là null, không thể đăng ký listener.");
        }
        // Tải danh sách tên công việc từ API
        loadPositionsToComboBox();

        // Thêm listener cho table selection
        jTable_Employees.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable_Employees.getSelectedRow() != -1) {
                    presenter.onEmployeeSelected();
                }
            }
        });

        // Thêm window listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (mainMenuPresenter != null) {
                    presenter.removeListener(mainMenuPresenter);
                    System.out.println("DEBUG (EmployeeM): Đã hủy đăng ký MainMenuPresenter khỏi EmployeeMPresenter.");
                }
            }
        });

        // Cấu hình ban đầu
        updateButtonStates(false);
    }
    
    public EmployeeM(MainMenuManagerPresenter mainMenuManagerPresenter) {
        this.mainMenuManagerPresenter = mainMenuManagerPresenter;
        this.mainMenuPresenter = null;
        initComponents();
        // Đảm bảo gán ActionListener cho tất cả các nút
        btnBack.addActionListener(evt -> btnBackActionPerformed(evt));
        btnRefresh.addActionListener(evt -> btnRefreshActionPerformed(evt));
        btnThem.addActionListener(evt -> btnThemActionPerformed(evt));
        btnSua.addActionListener(evt -> btnSuaActionPerformed(evt));
        btnXoa.addActionListener(evt -> btnXoaActionPerformed(evt));
        btnTimKiem.addActionListener(evt -> btnTimKiemActionPerformed(evt));
        
        presenter = new EmployeeMPresenter(this);
        // Tải danh sách tên công việc từ API
        loadPositionsToComboBox();

        // Thêm listener cho table selection
        jTable_Employees.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable_Employees.getSelectedRow() != -1) {
                    presenter.onEmployeeSelected();
                }
            }
        });

        // Thêm window listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (mainMenuManagerPresenter != null) {
                    mainMenuManagerPresenter.showMainMenuManager();
                }
            }
        });

        // Cấu hình ban đầu
        updateButtonStates(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPanel_Top = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtTenNV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jdNgaySinh = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jdNgayVaoLam = new com.toedter.calendar.JDateChooser();
        txtLuong = new javax.swing.JTextField();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtTenCV = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Employees = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel_Top.setBackground(new java.awt.Color(0, 0, 102));

        btnBack.setText("Quay lại");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnThem.setText("Thêm");

        btnSua.setText("Sửa");

        btnXoa.setText("Xóa");

        btnRefresh.setText("Làm mới");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Quản lý nhân viên");

        javax.swing.GroupLayout JPanel_TopLayout = new javax.swing.GroupLayout(JPanel_Top);
        JPanel_Top.setLayout(JPanel_TopLayout);
        JPanel_TopLayout.setHorizontalGroup(
            JPanel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanel_TopLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnThem)
                .addGap(18, 18, 18)
                .addComponent(btnSua)
                .addGap(18, 18, 18)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        JPanel_TopLayout.setVerticalGroup(
            JPanel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanel_TopLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPanel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(btnBack)
                    .addComponent(btnRefresh)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel1.setText("Mã nhân viên");

        jLabel2.setText("Tên nhân viên");

        jLabel3.setText("Ngày sinh");

        jLabel4.setText("Số điện thoại");

        jLabel5.setText("Ngày vào làm");

        jLabel6.setText("Lương");

        btnTimKiem.setText("Tìm kiếm");

        jLabel8.setText("Tên công việc");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel8))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTenNV, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(jdNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(txtTenCV))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(btnTimKiem))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdNgayVaoLam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLuong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(121, 121, 121))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addComponent(jdNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jdNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTimKiem)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTenCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTable_Employees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Ngày vào làm", "Tên công việc", "Số điện thoại", "Lương"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_Employees);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JPanel_Top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(JPanel_Top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Event handlers
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        if (mainMenuManagerPresenter != null) {
            mainMenuManagerPresenter.showMainMenuManager();
            this.dispose();
        } else {
            // Fallback nếu không có MainMenuManagerPresenter
            new MainMenu().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        if (validateForm()) {
            Employee employee = getEmployeeFromForm();
            presenter.addEmployee(employee);
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (validateForm()) {
            Employee employee = getEmployeeFromForm();
            presenter.updateEmployee(employee);
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        String maNv = getSelectedMaNv();
        if (maNv != null && !maNv.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa nhân viên này?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                presenter.deleteEmployee(maNv);
            }
        } else {
            showErrorMessage("Vui lòng chọn nhân viên cần xóa!");
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        String query = txtTimKiem.getText().trim();
        presenter.searchEmployees(query);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        clearForm();
        presenter.setAutoEmployeeId();
        presenter.loadAllEmployees(); // Thêm dòng này để load lại danh sách
    }

    // Implementation của interface IEmployeeM
    @Override
    public void displayEmployees(ArrayList<Employee> employees) {
        DefaultTableModel dtm = (DefaultTableModel) jTable_Employees.getModel();
        dtm.setRowCount(0);

        for (Employee employee : employees) {
            Vector<Object> row = new Vector<>();
            row.add(employee.getMaNv());
            row.add(employee.getTenNv());
            row.add(employee.getNgaySinh() != null ? employee.getNgaySinh().toString() : "");
            row.add(employee.getNgayVaoLam() != null ? employee.getNgayVaoLam().toString() : "");
            row.add(employee.getTenCv());
            row.add(employee.getSdt());
            row.add(employee.getLuong());
            dtm.addRow(row);
        }
    }

    @Override
    public void populateEmployeeDetails(Employee employee) {
        txtMaNV.setText(employee.getMaNv());
        txtMaNV.setEditable(false);
        txtTenNV.setText(employee.getTenNv());
        txtSDT.setText(employee.getSdt());
        txtLuong.setText(employee.getLuong() != null ? employee.getLuong().toString() : "");
        txtTenCV.setText(employee.getTenCv());
        // Set dates cho JDateChooser
        if (employee.getNgaySinh() != null) {
            Date ngaySinhDate = Date.from(employee.getNgaySinh().atStartOfDay(ZoneId.systemDefault()).toInstant());
            jdNgaySinh.setDate(ngaySinhDate);
        } else {
            jdNgaySinh.setDate(null);
        }
        if (employee.getNgayVaoLam() != null) {
            Date ngayVaoLamDate = Date.from(employee.getNgayVaoLam().atStartOfDay(ZoneId.systemDefault()).toInstant());
            jdNgayVaoLam.setDate(ngayVaoLamDate);
        } else {
            jdNgayVaoLam.setDate(null);
        }
        isEditing = true;
        updateButtonStates(true);
    }

    @Override
    public void clearForm() {
        txtMaNV.setText("");
        txtMaNV.setEditable(false);
        txtTenNV.setText("");
        txtSDT.setText("");
        txtLuong.setText("");
        txtTenCV.setText("");
        jdNgaySinh.setDate(null);
        jdNgayVaoLam.setDate(null);
        isEditing = false;
        updateButtonStates(false);
        presenter.setAutoEmployeeId(); // Gọi hàm sinh mã tự động khi làm mới form
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public String getSelectedMaNv() {
        int selectedRow = jTable_Employees.getSelectedRow();
        if (selectedRow != -1) {
            return (String) jTable_Employees.getValueAt(selectedRow, 0);
        }
        return null;
    }

    @Override
    public Employee getEmployeeFromForm() {
        String maNv = txtMaNV.getText().trim();
        String tenNv = txtTenNV.getText().trim();
        String sdt = txtSDT.getText().trim();
        String luongStr = txtLuong.getText().trim();
        String tenCv = txtTenCV.getText().trim();
        // Convert dates
        LocalDate ngaySinh = null;
        LocalDate ngayVaoLam = null;
        if (jdNgaySinh.getDate() != null) {
            ngaySinh = jdNgaySinh.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (jdNgayVaoLam.getDate() != null) {
            ngayVaoLam = jdNgayVaoLam.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        BigDecimal luong = null;
        if (!luongStr.isEmpty()) {
            try {
                luong = new BigDecimal(luongStr);
            } catch (NumberFormatException e) {
                // Handle invalid number format
            }
        }
        return new Employee(maNv, tenNv, ngaySinh, ngayVaoLam, sdt, luong, tenCv);
    }

    @Override
    public boolean validateForm() {
        if (txtTenNV.getText().trim().isEmpty()) {
            showErrorMessage("Vui lòng nhập tên nhân viên!");
            txtTenNV.requestFocus();
            return false;
        }
        if (jdNgayVaoLam.getDate() == null) {
            showErrorMessage("Vui lòng chọn ngày vào làm!");
            jdNgayVaoLam.requestFocus();
            return false;
        }
        String sdt = txtSDT.getText().trim();
        if (!sdt.matches("^\\d{10}$")) {
            showErrorMessage("Số điện thoại phải có đúng 10 chữ số!");
            txtSDT.requestFocus();
            return false;
        }
        String luongStr = txtLuong.getText().trim();
        if (luongStr.isEmpty()) {
            showErrorMessage("Vui lòng nhập lương!");
            txtLuong.requestFocus();
            return false;
        }
        try {
            BigDecimal luong = new BigDecimal(luongStr);
            if (luong.compareTo(BigDecimal.ZERO) <= 0) {
                showErrorMessage("Lương phải lớn hơn 0!");
                txtLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Lương phải là số!");
            txtLuong.requestFocus();
            return false;
        }
        if (txtTenCV.getText().trim().isEmpty()) {
            showErrorMessage("Vui lòng nhập tên công việc!");
            txtTenCV.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void updateButtonStates(boolean isEditing) {
        btnThem.setEnabled(!isEditing);
        btnSua.setEnabled(isEditing);
        btnXoa.setEnabled(isEditing);
    }

    @Override
    public void clearTableSelection() {
        jTable_Employees.clearSelection();
    }

    @Override
    public Date getNgaySinhFromDateChooser() {
        return jdNgaySinh.getDate();
    }

    @Override
    public Date getNgayVaoLamFromDateChooser() {
        return jdNgayVaoLam.getDate();
    }

    @Override
    public void setNgaySinhToDateChooser(Date date) {
        jdNgaySinh.setDate(date);
    }

    @Override
    public void setNgayVaoLamToDateChooser(Date date) {
        jdNgayVaoLam.setDate(date);
    }

    @Override
    public void setMaNv(String maNv) {
        txtMaNV.setText(maNv);
        txtMaNV.setEditable(false);
    }

    private void loadPositionsToComboBox() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://localhost:8080/api/employees/positions")
                        .get()
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String json = response.body().string();
                        ObjectMapper mapper = new ObjectMapper();
                        String[] positions = mapper.readValue(json, String[].class);
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            // cboTenCV.removeAllItems(); // This line is removed as txtTenCV is used
                            // for (String pos : positions) {
                            //     cboTenCV.addItem(pos);
                            // }
                        });
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

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
            java.util.logging.Logger.getLogger(EmployeeM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanel_Top;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Employees;
    private com.toedter.calendar.JDateChooser jdNgaySinh;
    private com.toedter.calendar.JDateChooser jdNgayVaoLam;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenCV;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
