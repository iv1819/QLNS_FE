/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;

/**
 *
 * @author Admin
 */
public class Order {
    private String maDH;
    private String tenKH;
    private Date ngayBan;
    private double tongTien;

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String maKH) {
        this.tenKH = maKH;
    }

   

   
    public Date getNgayBan() {
        return ngayBan;
    }

    public void setNgayBan(Date ngayBan) {
        this.ngayBan = ngayBan;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public Order(String maDH, String tenKH, Date ngayBan, double tongTien) {
        this.maDH = maDH;
        this.tenKH = tenKH;
        this.ngayBan = ngayBan;
        this.tongTien = tongTien;
    }

    public Order() {
    }
}
