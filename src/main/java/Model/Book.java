
package Model;

import java.io.Serializable;
// Không còn import java.awt.Image, BufferedImage, File, URL, ImageIO
// Không còn import javax.swing.ImageIcon

/**
 * Lớp Model Book cho Frontend.
 * Đây là một POJO (Plain Old Java Object) để ánh xạ dữ liệu từ API backend.
 * Không chứa logic UI hay logic tải ảnh.
 */
public class Book implements Serializable {

    private String maSach;
    private String tenSach;
    private String duongDanAnh; // Sẽ lưu URL tương đối từ backend, ví dụ: "/api/uploads/ten_file.png"
    // private transient ImageIcon anhSach; // Bỏ trường này khỏi Model, View sẽ tự tạo ImageIcon

    private String nhaXB;
    private String tacGia;
    private int soLuong;
    private double giaBan; // Thay đổi từ int sang double cho giá bán
    private int namXB;
    private String maDanhMuc;

    // Constructor mặc định (cần thiết cho Jackson)
    public Book() {
        this.duongDanAnh = ""; // Đảm bảo không null
    }

    // Constructor có tham số
    public Book(String maSach, String tenSach, int soLuong, double giaBan, String tacGia, String nhaXB, String duongDanAnh, int namXB, String maDanhMuc) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.nhaXB = nhaXB;
        this.tacGia = tacGia;
        this.duongDanAnh = (duongDanAnh != null) ? duongDanAnh : "";
        this.namXB = namXB;
        this.maDanhMuc = maDanhMuc;
    }

    // --- Getters và Setters ---
    public String getMaSach() { return maSach; }
    public void setMaSach(String maSach) { this.maSach = maSach; }
    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }
    public String getDuongDanAnh() { return duongDanAnh; }
    public void setDuongDanAnh(String duongDanAnh) { 
        this.duongDanAnh = (duongDanAnh != null) ? duongDanAnh : ""; 
    }
    // Không còn getAnhSach() và setAnhSach(ImageIcon) ở đây nữa
    public String getNhaXB() { return nhaXB; }
    public void setNhaXB(String nhaXB) { this.nhaXB = nhaXB; }
    public String getTacGia() { return tacGia; }
    public void setTacGia(String tacGia) { this.tacGia = tacGia; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public double getGiaBan() { return giaBan; } // Đổi kiểu dữ liệu
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; } // Đổi kiểu dữ liệu
    public int getNamXB() { return namXB; }
    public void setNamXB(int namXB) { this.namXB = namXB; }
    public String getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(String maDanhMuc) { this.maDanhMuc = maDanhMuc; }

    // Không còn readObject() vì không có ImageIcon transient
    // Nếu bạn vẫn cần Serializable, hãy giữ readObject() và writeObject() nếu có các trường transient khác

    @Override
    public String toString() {
        return "Book{" + "maSach=" + maSach + ", tenSach='" + tenSach + '\'' + ", duongDanAnh='" + duongDanAnh + '\'' + ", nhaXB='" + nhaXB + '\'' + ", tacGia='" + tacGia + '\'' + ", soLuong=" + soLuong + ", giaBan=" + giaBan + ", namXB=" + namXB + ", maDanhMuc='" + maDanhMuc + '\'' + '}';
    }
}