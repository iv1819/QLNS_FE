/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class Employee {
    private String maNv;
    private String tenNv;
    private LocalDate ngaySinh;
    private LocalDate ngayVaoLam;
    private String sdt;
    private BigDecimal luong;
    private String tenCv;

    // Constructor mặc định
    public Employee() {
    }

    // Constructor với tham số
    public Employee(String maNv, String tenNv, LocalDate ngaySinh, LocalDate ngayVaoLam, String sdt, BigDecimal luong, String tenCv) {
        this.maNv = maNv;
        this.tenNv = tenNv;
        this.ngaySinh = ngaySinh;
        this.ngayVaoLam = ngayVaoLam;
        this.sdt = sdt;
        this.luong = luong;
        this.tenCv = tenCv;
    }

    // Getters và Setters
    public String getMaNv() {
        return maNv;
    }

    public void setMaNv(String maNv) {
        this.maNv = maNv;
    }

    public String getTenNv() {
        return tenNv;
    }

    public void setTenNv(String tenNv) {
        this.tenNv = tenNv;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public BigDecimal getLuong() {
        return luong;
    }

    public void setLuong(BigDecimal luong) {
        this.luong = luong;
    }

    public String getTenCv() {
        return tenCv;
    }
    public void setTenCv(String tenCv) {
        this.tenCv = tenCv;
    }

    // Utility methods để convert giữa LocalDate và Date
    public Date getNgaySinhAsDate() {
        if (ngaySinh == null) return null;
        return Date.from(ngaySinh.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public void setNgaySinhFromDate(Date date) {
        if (date == null) {
            this.ngaySinh = null;
        } else {
            this.ngaySinh = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    public Date getNgayVaoLamAsDate() {
        if (ngayVaoLam == null) return null;
        return Date.from(ngayVaoLam.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public void setNgayVaoLamFromDate(Date date) {
        if (date == null) {
            this.ngayVaoLam = null;
        } else {
            this.ngayVaoLam = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    @Override
    public String toString() {
        return "Employee{" +
                "maNv='" + maNv + '\'' +
                ", tenNv='" + tenNv + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", ngayVaoLam=" + ngayVaoLam +
                ", sdt='" + sdt + '\'' +
                ", luong=" + luong +
                ", tenCv='" + tenCv + '\'' +
                '}';
    }
}
