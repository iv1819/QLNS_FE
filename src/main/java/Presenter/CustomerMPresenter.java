package Presenter;

import API.CustomerApiClient;
import Model.Customer;
import View.interfaces.ICustomerM;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerMPresenter {
    private ICustomerM view;
    private CustomerApiClient apiClient;
    private Customer currentCustomer;

    public CustomerMPresenter(ICustomerM view) {
        this.view = view;
        this.apiClient = new CustomerApiClient();
        loadAllCustomers();
        setAutoCustomerId();
    }

    public void loadAllCustomers() {
        new SwingWorker<List<Customer>, Void>() {
            @Override
            protected List<Customer> doInBackground() throws Exception {
                return apiClient.getAll();
            }
            @Override
            protected void done() {
                try {
                    List<Customer> customers = get();
                    view.displayCustomers(new ArrayList<>(customers));
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi tải danh sách khách hàng: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void addCustomer(Customer customer) {
        new SwingWorker<Customer, Void>() {
            @Override
            protected Customer doInBackground() throws Exception {
                return apiClient.add(customer);
            }
            @Override
            protected void done() {
                try {
                    get();
                    view.showMessage("Thêm khách hàng thành công!");
                    view.clearForm();
                    loadAllCustomers();
                    setAutoCustomerId();
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi thêm khách hàng: " + e.getCause().getMessage());
                }
            }
        }.execute();
    }

    public void updateCustomer(Customer customer) {
        new SwingWorker<Customer, Void>() {
            @Override
            protected Customer doInBackground() throws Exception {
                return apiClient.update(customer.getMaKh(), customer);
            }
            @Override
            protected void done() {
                try {
                    get();
                    view.showMessage("Cập nhật khách hàng thành công!");
                    view.clearForm();
                    loadAllCustomers();
                    setAutoCustomerId();
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi cập nhật khách hàng: " + e.getCause().getMessage());
                }
            }
        }.execute();
    }

    public void deleteCustomer(String maKh) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                apiClient.delete(maKh);
                return null;
            }
            @Override
            protected void done() {
                try {
                    get();
                    view.showMessage("Xóa khách hàng thành công!");
                    view.clearForm();
                    loadAllCustomers();
                    setAutoCustomerId();
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi xóa khách hàng: " + e.getCause().getMessage());
                }
            }
        }.execute();
    }

    public void searchCustomers(String keyword) {
        new SwingWorker<List<Customer>, Void>() {
            @Override
            protected List<Customer> doInBackground() throws Exception {
                return apiClient.search(keyword);
            }
            @Override
            protected void done() {
                try {
                    List<Customer> customers = get();
                    view.displayCustomers(new ArrayList<>(customers));
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        view.showMessage("Tìm thấy " + customers.size() + " khách hàng phù hợp.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi tìm kiếm khách hàng: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void setAutoCustomerId() {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return apiClient.getNextId();
            }
            @Override
            protected void done() {
                try {
                    String id = get();
                    view.setMaKh(id);
                } catch (InterruptedException | ExecutionException e) {
                    view.setMaKh("");
                }
            }
        }.execute();
    }

    public void onCustomerSelected(Customer customer) {
        this.currentCustomer = customer;
        view.populateCustomerDetails(customer);
    }
} 