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
    private String tennv;

    public String getTennv() {
        return tennv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }
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

    public Order(String tenKH, double tongTien, String tennv) {
        this.tenKH = tenKH;
        this.tongTien = tongTien;
        this.tennv = tennv;
    }

 
    public Order() {
    }
}
