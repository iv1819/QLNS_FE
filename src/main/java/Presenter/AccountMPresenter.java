/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.AccountApiClient;
import Model.Account;
import View.interfaces.IAccountM;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author trang
 */
public class AccountMPresenter {

    private IAccountM view;
    private AccountApiClient accountApiClient;
    private String currentSelectedRoleId;

    public AccountMPresenter(IAccountM view) {
        this.view = view;
        this.accountApiClient = new AccountApiClient();
        this.currentSelectedRoleId = "";
        initializeData();
    }

    // Xóa form
    public void clearForm() {
        view.clearForm();
        currentSelectedRoleId = null;
    }

    // Load tất cả tài khoản
    public void loadAllAccounts() {
        new SwingWorker<List<Account>, Void>() {
            @Override
            protected List<Account> doInBackground() throws Exception {
                return accountApiClient.getAllAccounts();
            }

            @Override
            protected void done() {
                try {
                    List<Account> accounts = get();
                    view.displayAccounts(new ArrayList<>(accounts));
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi tải tài khoản từ API: " + e.getMessage());
                }
            }
        }.execute();
    }

    // Thêm tài khoản
    public void addAccount() {
        Account acc = new Account();
        acc.setTaiKhoan(view.getTaiKhoan());
        acc.setMatKhau(view.getMatKhau());
        acc.setTennv(view.getTennv());
        acc.setChucVu(view.getChucVu());
        acc.setTrangThai(view.getTrangThai());

        new SwingWorker<Account, Void>() {
            @Override
            protected Account doInBackground() throws Exception {
                return accountApiClient.addAccount(acc);
            }

            @Override
            protected void done() {
                try {
                    Account addedAcc = get();
                    if (addedAcc != null) {
                        view.showMessage("✅ Thêm tài khoản thành công!");
                        loadAllAccounts();
                        notifyListeners();
                        view.clearForm();
                    } else {
                        view.showErrorMessage("❌ Thêm tài khoản thất bại.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi thêm tài khoản qua API: " + e.getMessage());
                }
            }

            private void notifyListeners() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        }.execute();
    }

    // Cập nhật tài khoản
    public void updateAccount(Account updateAccount) {
        String taiKhoan = view.getTaiKhoan();
        if (taiKhoan == null || taiKhoan.trim().isEmpty()) {
            view.showErrorMessage("Vui lòng chọn tài khoản cần cập nhật.");
            return;
        }

        Account acc = new Account();
        acc.setTaiKhoan(view.getTaiKhoan());
        acc.setMatKhau(view.getMatKhau());
        acc.setTennv(view.getTennv());
        acc.setChucVu(view.getChucVu());
        acc.setTrangThai(view.getTrangThai());

        new SwingWorker<Account, Void>() {
            @Override
            protected Account doInBackground() throws Exception {
                return accountApiClient.updateAccount(taiKhoan, acc);
            }

            @Override
            protected void done() {
                try {
                    Account updatedAcc = get();
                    if (updatedAcc != null) {
                        view.showMessage("✅ Cập nhật tài khoản thành công!");
                        loadAllAccounts();
                        notifyListeners();
                        view.clearForm();
                    } else {
                        view.showErrorMessage("❌ Cập nhật tài khoản thất bại.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi cập nhật tài khoản qua API: " + e.getMessage());
                }
            }

            private void notifyListeners() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        }.execute();
    }

    // Xóa tài khoản
    public void deleteAccount(String taiKhoan) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                accountApiClient.deleteAccount(taiKhoan);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    view.showMessage("✅ Xóa tài khoản thành công!");
                    loadAllAccounts();
                    notifyListeners();
                    view.clearForm();
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi xóa tài khoản qua API: " + e.getMessage());
                }
            }

            private void notifyListeners() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        }.execute();
    }

    // Tìm kiếm tài khoản
    public void searchAccounts(String keyword) {
        new SwingWorker<List<Account>, Void>() {
            @Override
            protected List<Account> doInBackground() throws Exception {
                if (keyword == null || keyword.isEmpty()) {
                    return accountApiClient.getAllAccounts();
                }
                return accountApiClient.searchAccounts(keyword);
            }

            @Override
            protected void done() {
                try {
                    List<Account> results = get();
                    view.displayAccounts(new ArrayList<>(results));
                    if (results.isEmpty()) {
                        view.showMessage("🔍 Không tìm thấy tài khoản nào phù hợp.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi tìm kiếm tài khoản qua API: " + e.getMessage());
                }
            }
        }.execute();
    }

    // Lấy dữ liệu tài khoản khi click JTable
    public void onAccountSelected(int selectedRow) {
        new SwingWorker<Account, Void>() {
            @Override
            protected Account doInBackground() throws Exception {
                List<Account> allAccounts = accountApiClient.getAllAccounts();
                if (selectedRow >= 0 && selectedRow < allAccounts.size()) {
                    return allAccounts.get(selectedRow);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    Account acc = get();
                    if (acc != null) {
                        view.setTaiKhoan(acc.getTaiKhoan());
                        view.setMatKhau(acc.getMatKhau());
                        view.setTennv(acc.getTennv());
                        view.setChucVu(acc.getChucVu());
                        view.setTrangThai(acc.getTrangThai());
                        currentSelectedRoleId = acc.getChucVu();
                        view.populateAccountDetails(acc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi tải dữ liệu tài khoản: " + e.getMessage());
                }
            }
        }.execute();
    }

    // Khởi tạo data ban đầu
    private void initializeData() {
        loadAllAccounts();
    }

    public List<String> getAllMaCV() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<String> getAllTrangThai() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}


