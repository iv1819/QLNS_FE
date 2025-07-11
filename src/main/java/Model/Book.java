package Model;

import java.io.Serializable;

public class Book implements Serializable {

    private String maSach;
    private String tenSach;
    private String duongDanAnh;

    // Phải khớp với BookDto.java của backend
    private Integer soLuong; // Đổi thành Integer (từ int)
    private Double giaBan;   // Đổi thành Double (từ double)

    private String maTacGia;  // Sẽ gửi lên backend, nhận về từ backend
    private String tenTacGia;  // Chỉ để hiển thị trên UI, nhận về từ backend

    private String maNXB;     // Sẽ gửi lên backend, nhận về từ backend
    private String tenNXB;    // Chỉ để hiển thị trên UI, nhận về từ backend

    private Integer namXB;   // Đổi thành Integer (từ int)

    private String maDanhMuc; 
    private String tenDanhMuc;

    // Default constructor (cần thiết cho Jackson deserialization)
    public Book() {
        this.duongDanAnh = ""; // Đảm bảo không null
    }

    public Book(String maSach, String tenSach, String duongDanAnh, Integer soLuong, Double giaBan, String maTacGia, String tenTacGia, String maNXB, String tenNXB, Integer namXB, String maDanhMuc, String tenDanhMuc) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.duongDanAnh = duongDanAnh;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.maTacGia = maTacGia;
        this.tenTacGia = tenTacGia;
        this.maNXB = maNXB;
        this.tenNXB = tenNXB;
        this.namXB = namXB;
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }


    // --- Getters and Setters cho tất cả các trường ---

    public String getMaSach() { return maSach; }
    public void setMaSach(String maSach) { this.maSach = maSach; }
    public String getTenSach() { return tenSach; }
    public void setTenSach(String tenSach) { this.tenSach = tenSach; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }
    public Double getGiaBan() { return giaBan; }
    public void setGiaBan(Double giaBan) { this.giaBan = giaBan; }

    public String getMaTacGia() { return maTacGia; }
    public void setMaTacGia(String maTacGia) { this.maTacGia = maTacGia; }
    public String getTenTacGia() { return tenTacGia; }
    public void setTenTacGia(String tenTacGia) { this.tenTacGia = tenTacGia; }

    public String getMaNXB() { return maNXB; }
    public void setMaNXB(String maNXB) { this.maNXB = maNXB; }
    public String getTenNXB() { return tenNXB; }
    public void setTenNXB(String tenNXB) { this.tenNXB = tenNXB; }

    public String getDuongDanAnh() { return duongDanAnh; }
    public void setDuongDanAnh(String duongDanAnh) { this.duongDanAnh = (duongDanAnh != null) ? duongDanAnh : ""; }

    public Integer getNamXB() { return namXB; }
    public void setNamXB(Integer namXB) { this.namXB = namXB; }

    public String getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(String maDanhMuc) { this.maDanhMuc = maDanhMuc; }

    // public String getTenDanhMuc() { return tenDanhMuc; } // Nếu có
    // public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; } // Nếu có

    @Override
    public String toString() {
        return "Book{" +
                "maSach='" + maSach + '\'' +
                ", tenSach='" + tenSach + '\'' +
                ", soLuong=" + soLuong +
                ", giaBan=" + giaBan +
                ", maTacGia='" + maTacGia + '\'' +
                ", tenTacGia='" + tenTacGia + '\'' +
                ", maNXB='" + maNXB + '\'' +
                ", tenNXB='" + tenNXB + '\'' +
                ", duongDanAnh='" + duongDanAnh + '\'' +
                ", namXB=" + namXB +
                ", maDanhMuc='" + maDanhMuc + '\'' +
                '}';
    }
}