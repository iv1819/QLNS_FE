/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.interfaces;

import Model.Account;
import java.util.ArrayList;

/**
 *
 * @author trang
 */
public interface IAccountM {
    String getTaiKhoan();
    String getMatKhau();
    String getTennv();
    String getChucVu(); // Tên chức vụ hiển thị trên combobox
    String getTrangThai();
    
    void setTaiKhoan(String taiKhoan);
    void setMatKhau(String matKhau);
    void setTennv(String tenNV);
    void setChucVu(String tenCV);
    void setTrangThai(String trangThai);

    public void showMessage(String _Thêm_tài_khoản_thành_công);

    public void clearForm();

    public void showErrorMessage(String _Thêm_tài_khoản_thất_bại);

    public void displayAccounts(ArrayList<Account> arrayList);

    public void populateAccountDetails(Account acc);
    
    
    
}
