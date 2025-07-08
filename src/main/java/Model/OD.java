/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Admin
 */
public class OD {
    private int id;
    private String tenSach;
    private int soLuong;
    private double donGia;
    private String maDH;

    public OD(int id, String maSP, int soLuong, double donGia, String maDH) {
        this.id = id;
        this.tenSach = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.maDH = maDH;
    }

    public OD() {
    }

    public int getId() {
        return id;
    }


    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

 
    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }
}
