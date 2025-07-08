/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.ODApiClient;
import API.OrderApiClient;
import Model.OD;
import Model.Order;
import View.OrderM;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Admin
 */
public class OrderMPresenter {
        private OrderM view; // Tham chiếu đến View
    private OrderApiClient orderApi;
       public OrderMPresenter(OrderM view) {
        this.view = view;
        this.orderApi = new OrderApiClient();
    }
    public void loadOrders() {
        new SwingWorker<List<Order>, Void>() {
            @Override
            protected List<Order> doInBackground() throws Exception {
                return orderApi.getAllOrders();
            }

            @Override
            protected void done() {
                try {
                    List<Order> order = get();
                    view.displayOrder(new ArrayList<>(order));
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi tải don hang từ API: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void deleteOrder(String maDH) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                orderApi.deleteOrder(maDH);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Kiểm tra ngoại lệ
                    view.showMessage("Xóa don hang thành công!");
                    loadOrders();
                    view.clearInputFields();
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi xóa don hang qua API: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void searchOrderByMaDH(String maDH) {
        new SwingWorker<List<Order>, Void>() {
            @Override
            protected List<Order> doInBackground() throws Exception {
                String query = "";
                if (maDH != null && !maDH.isEmpty()) {
                    query += maDH;
                }
                if (query.isEmpty()) {
                    return orderApi.getAllOrders();
                }
                return orderApi.searchOrder(query);
            }

            @Override
            protected void done() {
                try {
                    List<Order> searchResults = get();
                    view.displayOrder(new ArrayList<>(searchResults));
                    if (searchResults.isEmpty()) {
                        view.showMessage("Không tìm thấy don hang nào phù hợp.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi tìm kiếm don hang qua API: " + e.getMessage());
                }
            }
        }.execute();
    }

}
