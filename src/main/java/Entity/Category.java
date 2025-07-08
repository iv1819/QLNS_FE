/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author Admin
 */
import java.io.Serializable;

/**
 * Lớp Model Category cho Frontend.
 * Đây là một POJO (Plain Old Java Object) để ánh xạ dữ liệu từ API backend.
 */
public class Category implements Serializable {

    private String maDanhMuc;
    private String tenDanhMuc;

    // Constructor mặc định (cần thiết cho Jackson)
    public Category() {
    }

    // Constructor với tham số
    public Category(String maDanhMuc, String tenDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    // --- Getters và Setters ---
    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    @Override
    public String toString() {
        return "Category{" +
                "maDanhMuc='" + maDanhMuc + '\'' +
                ", tenDanhMuc='" + tenDanhMuc + '\'' +
                '}';
    }
}
