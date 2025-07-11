/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import API.ODApiClient;
import Model.Book;
import Model.OD;
import View.OrderM;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

/**
 *
 * @author Admin
 */
public class ODPresenter {
    private OrderM view; // Tham chiếu đến View
    private ODApiClient odApi;
       public ODPresenter(OrderM view) {
        this.view = view;
        this.odApi = new ODApiClient();
    }
    public void loadODByMaDH(String maDH) {
        new SwingWorker<List<OD>, Void>() {
            @Override
            protected List<OD> doInBackground() throws Exception {
                return odApi.getODsByMaDH(maDH);
            }

            @Override
            protected void done() {
                try {
                    List<OD> ods = get();
                    view.displayOD(new ArrayList<>(ods));
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi tải ctdh từ API: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void deleteODByMaDH(String maDH) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                odApi.deleteODbyMaDH(maDH);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Kiểm tra ngoại lệ
                    view.showMessage("Xóa ctdh thành công!");
                    loadODByMaDH(maDH);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showErrorMessage("Lỗi khi xóa ctdh qua API: " + e.getMessage());
                }
            }
        }.execute();
    }

    

}
