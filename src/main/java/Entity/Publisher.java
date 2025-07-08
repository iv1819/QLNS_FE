/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.Objects;

/**
 *
 * @author Admin
 */
public class Publisher {
    private String maNXB;
    private String tenNXB;
    private String sdt; // Số điện thoại

    public Publisher() {
    }

    public Publisher(String maNXB, String tenNXB, String sdt) {
        this.maNXB = maNXB;
        this.tenNXB = tenNXB;
        this.sdt = sdt;
    }

    // Getters
    public String getMaNXB() {
        return maNXB;
    }

    public String getTenNXB() {
        return tenNXB;
    }

    public String getSdt() {
        return sdt;
    }


    // Setters
    public void setMaNXB(String maNXB) {
        this.maNXB = maNXB;
    }

    public void setTenNXB(String tenNXB) {
        this.tenNXB = tenNXB;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return Objects.equals(maNXB, publisher.maNXB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNXB);
    }
}
