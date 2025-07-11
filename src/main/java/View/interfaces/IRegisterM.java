/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.interfaces;

/**
 *
 * @author trang
 */
public interface IRegisterM {
    void dangKyTaiKhoan();
    boolean validateInput();
    
    String getTaiKhoan();
    String getMatKhau();
    String getChucVu();
    String getTrangThai();
    String getTennv();
    
    void setTaiKhoan();
    void setMatKhau();
    void setChucVu();
    void setTrangThai();
    void setTennv();
}
