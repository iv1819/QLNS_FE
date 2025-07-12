/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Presenter.MainMenuManagerPresenter;
import Presenter.MainMenuPresenter;

/**
 *
 * @author Admin
 */
public class MainMenu_Manager2 extends javax.swing.JFrame {
    private MainMenuPresenter presenter;
    private MainMenuManagerPresenter manager;

    /**
     * Creates new form MainMenu_Manager
     */
    public MainMenu_Manager2(MainMenu parent, MainMenuPresenter presenter, boolean IsManager) {
    this.presenter = presenter;  // <-- KHÔNG còn null
    initComponents();
    manager = new MainMenuManagerPresenter(this, presenter);
setLocationRelativeTo(null);
    if(IsManager){
        btnBookM.addActionListener(evt -> {
            manager.openBookManagement();
            this.dispose();
        });
        
        
        btnBack.addActionListener(evt -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            this.dispose();
        });
           jbtnOrderM.addActionListener(evt -> {
            manager.openOrder();
            this.dispose();
        });

            // Xử lý nút Quản lý nhân viên
            btnEmployeeM.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    manager.openEmployeeManagement();
                    dispose();
                }
            });

             // Xử lý nút Quản lý khách hàng
        btnCustomerM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manager.openCustomer();
                dispose();
            }
        });        
        
        btnPublisher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Publisher2 publisher2 = new Publisher2();
                publisher2.setVisible(true);
                dispose();
               
            }
        });
        btnCategoryM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Category category = new Category();
                category.setVisible(true);
                dispose();
                
            }
        });
        
        btnAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
              AccountM acc = new AccountM();
                acc.setVisible(true);
                dispose();
                
            }
        });
        
        btnAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AuthorM author = new AuthorM();
                author.setVisible(true);
                dispose();
                
            }
        });
        
    }
    else{
        btnEmployeeM.setEnabled(false);

        btnBookM.addActionListener(evt -> {
        BookM bookM = new BookM(this.presenter);
        bookM.setVisible(true);
        dispose();
        });
   
    // Xử lý nút Quay lại
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose();
            }
        });
        
         jbtnOrderM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderM orderM = new OrderM();
                    orderM.setVisible(true);
                    dispose();
               ;
            }
        });
        
        // Xử lý nút Quản lý khách hàng
        btnCustomerM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manager.openCustomer();
                dispose();
            }
        });
        btnPublisher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               
            }
        });
        btnCategoryM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                
            }
        });
        
    }
}
    public MainMenu_Manager2() {
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        btnBookM = new javax.swing.JButton();
        btnPublisher = new javax.swing.JButton();
        jbtnOrderM = new javax.swing.JButton();
        btnCategoryM = new javax.swing.JButton();
        btnEmployeeM = new javax.swing.JButton();
        btnCustomerM = new javax.swing.JButton();
        btnAuthor = new javax.swing.JButton();
        btnAcc = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnBack.setBackground(new java.awt.Color(0, 51, 102));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Quay lại");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnBookM.setBackground(new java.awt.Color(0, 51, 102));
        btnBookM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBookM.setForeground(new java.awt.Color(255, 255, 255));
        btnBookM.setText("Quản lí sách");

        btnPublisher.setBackground(new java.awt.Color(0, 51, 102));
        btnPublisher.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPublisher.setForeground(new java.awt.Color(255, 255, 255));
        btnPublisher.setText("Quản lí nhà xuất bản");

        jbtnOrderM.setBackground(new java.awt.Color(0, 51, 102));
        jbtnOrderM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtnOrderM.setForeground(new java.awt.Color(255, 255, 255));
        jbtnOrderM.setText("Quản lí đơn hàng");

        btnCategoryM.setBackground(new java.awt.Color(0, 51, 102));
        btnCategoryM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCategoryM.setForeground(new java.awt.Color(255, 255, 255));
        btnCategoryM.setText("Quản lí danh mục");

        btnEmployeeM.setBackground(new java.awt.Color(0, 51, 102));
        btnEmployeeM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEmployeeM.setForeground(new java.awt.Color(255, 255, 255));
        btnEmployeeM.setText("Quản lí nhân viên");
        btnEmployeeM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeMActionPerformed(evt);
            }
        });

        btnCustomerM.setBackground(new java.awt.Color(0, 51, 102));
        btnCustomerM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCustomerM.setForeground(new java.awt.Color(255, 255, 255));
        btnCustomerM.setText("Quản lí khách hàng");

        btnAuthor.setBackground(new java.awt.Color(0, 51, 102));
        btnAuthor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAuthor.setForeground(new java.awt.Color(255, 255, 255));
        btnAuthor.setText("Quản lí tác giả");

        btnAcc.setBackground(new java.awt.Color(0, 51, 102));
        btnAcc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAcc.setForeground(new java.awt.Color(255, 255, 255));
        btnAcc.setText("Quản lí tài khoản");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Các mục quản lí");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(187, 187, 187))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnBookM, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbtnOrderM, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(btnPublisher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(btnAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCategoryM, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnCustomerM, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEmployeeM, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(487, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBookM, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCategoryM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAuthor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEmployeeM, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnOrderM, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCustomerM, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPublisher, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(346, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEmployeeMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEmployeeMActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackActionPerformed

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
            java.util.logging.Logger.getLogger(MainMenu_Manager2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu_Manager2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu_Manager2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu_Manager2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu_Manager2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcc;
    private javax.swing.JButton btnAuthor;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBookM;
    private javax.swing.JButton btnCategoryM;
    private javax.swing.JButton btnCustomerM;
    private javax.swing.JButton btnEmployeeM;
    private javax.swing.JButton btnPublisher;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbtnOrderM;
    // End of variables declaration//GEN-END:variables
}
