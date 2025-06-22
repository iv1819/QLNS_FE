/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.qlins;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import View.BookM;
import View.MainMenu;
public class QliNS extends JFrame {
public static void main(String[] args) {
        // ... (Thiết lập LookAndFeel nếu cần)
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainMenu mm = new MainMenu();
                mm.setVisible(true);
            }
        });
    }
}
