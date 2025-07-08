/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.interfaces;

import Model.Book;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public interface IBookM {

 String getSelectedMaSach();
 void clearForm();
    void displayBooks(ArrayList<Book> books);

    void populateBookDetails(Book book);


    void updateImagePreview(String imagePath);

    void populateNhaXBNames(List<String> names);

    void populateTacGiaNames(List<String> names);

    void populateDanhMucNames(List<String> names);

    void showMessage(String message);

    void showErrorMessage(String message);
String getMaSach();
    String getTenSach();
    Integer getSoLuong();
    Double getGiaBan();
    String getTacGia();
    String getNhaXB();
    String getDuongDanAnh(); // <--- THÊM GETTER NÀY
    Integer getNamXB();
    String getMaDanhMuc();
    void setMaSach(String maSach);
    void setTenSach(String tenSach);
    void setSoLuong(Integer soLuong);
    void setGiaBan(Double giaBan);
    void setTacGia(String tacGia);
    void setNhaXB(String nhaXB);
    void setDuongDanAnh(String duongDanAnh); // <--- THÊM SETTER NÀY để set giá trị vào jtxtAnh
    void setNamXB(Integer namXB);
    void setMaDanhMuc(String maDanhMuc);
}

