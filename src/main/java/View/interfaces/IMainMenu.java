/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.interfaces;

import Model.Book;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.table.DefaultTableModel;


public interface IMainMenu {
     void populateMaterialCategoryTabs(LinkedHashMap<String, ArrayList<Book>> categorizedBooks);
    void updateSelectedBook(Book book);
    void addReceiptItem(Object[] rowData);
    void showMessage(String message);
    void showErrorMessage(String message);
    void updateReceiptTotal(boolean hasDiscount);
    void clearReceiptTable();
    void populateComboBox();
    double LayTongTien();
    DefaultTableModel getReceiptTableModel();
}
