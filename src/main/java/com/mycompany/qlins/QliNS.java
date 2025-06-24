/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.qlins;

/**
 *
 * @author Admin
 */
import View.Login;
import javax.swing.*;
public class QliNS extends JFrame {
public static void main(String[] args) {
        // ... (Thiết lập LookAndFeel nếu cần)
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Login lg = new Login();
                lg.setLocationRelativeTo(null); 
                lg.setVisible(true);
            }
        });
    }
}
