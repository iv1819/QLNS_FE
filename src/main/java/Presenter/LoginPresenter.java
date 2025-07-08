/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.LoginApiClient;
import Entity.Account;
import View.interfaces.ILogin;
import java.util.Map;
import javax.swing.SwingWorker;

/**
 *
 * @author Admin
 */
public class LoginPresenter {

    private final ILogin view;
    private final LoginApiClient api;

    public LoginPresenter(ILogin view, LoginApiClient apiC) {
        this.view  = view;
        this.api = apiC;
    }

    /** Được gọi khi người dùng bấm nút “Đăng nhập” */
    public void onLoginClicked() {
        String user = view.getUsername();
        String pass = view.getPassword();

        if (user.isEmpty() || pass.isEmpty()) {
            view.showMessage("Vui lòng nhập tài khoản và mật khẩu.");
            return;
        }

        /* ─────────  Gọi API bất đồng bộ ───────── */
        new SwingWorker<Map<String, Object>, Void>() {

            @Override            // chạy ở thread nền
            protected Map<String, Object> doInBackground() {
                return api.login(user, pass);           // trả Map
            }

            @Override            // chạy lại trên EDT sau khi xong
            protected void done() {
                try {
                    Map<String, Object> res = get();      // lấy kết quả

                    boolean success = (boolean) res.getOrDefault("success", false);
                    if (!success) {
                        String msg = (String) res.getOrDefault("message", "Đăng nhập thất bại");
                        view.showMessage(msg);
                        return;
                    }
 String role = String.valueOf(res.getOrDefault("role", "Nhân viên"));
                boolean isManager = "Quản lí".equalsIgnoreCase(role);
                    view.navigateToMain(isManager);       // mở UI tương ứng

                } catch (Exception ex) {                  // Interrupted, ExecutionException
                    ex.printStackTrace();
                    view.showMessage("Không thể xử lý kết quả đăng nhập.");
                }
            }
        }.execute();
    }
}

