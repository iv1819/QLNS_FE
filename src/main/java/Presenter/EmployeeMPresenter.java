package Presenter;

import API.EmployeeApiClient;
import Model.Employee;
import View.interfaces.IEmployeeM;
import util.BookDataChangeListener;
import javax.swing.SwingWorker;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingUtilities;

/**
 * Presenter cho Employee Management
 * Xử lý logic nghiệp vụ liên quan đến quản lý nhân viên (CRUD)
 */
public class EmployeeMPresenter {

    private IEmployeeM view;
    private EmployeeApiClient employeeApiClient;
    private List<BookDataChangeListener> listeners = new ArrayList<>();
    private MainMenuPresenter mainMenuListener; // Listener để thông báo thay đổi
    private Employee currentEmployee; // Lưu lại nhân viên đang được chọn

    public EmployeeMPresenter(IEmployeeM view) {
        this.view = view;
        this.employeeApiClient = new EmployeeApiClient();
        initializeData();
    }

    // Phương thức để thêm Listener
    public void addListener(BookDataChangeListener listener) {
        listeners.add(listener);
        System.out.println("DEBUG (EmployeeMPresenter): Đã thêm listener: " + listener.getClass().getSimpleName());
    }

    // Phương thức để xóa Listener
    public void removeListener(BookDataChangeListener listener) {
        listeners.remove(listener);
        System.out.println("DEBUG (EmployeeMPresenter): Đã xóa listener: " + listener.getClass().getSimpleName());
    }

    // Phương thức thông báo cho tất cả các Listener
    private void notifyListeners() {
        System.out.println("DEBUG (EmployeeMPresenter): Đang thông báo cho các listener về thay đổi dữ liệu nhân viên.");
        for (BookDataChangeListener listener : listeners) {
            listener.onBookDataChanged();
        }
    }

    /**
     * Tải tất cả nhân viên từ API
     */
    public void loadAllEmployees() {
        new SwingWorker<List<Employee>, Void>() {
            @Override
            protected List<Employee> doInBackground() throws Exception {
                return employeeApiClient.getAllEmployees();
            }

            @Override
            protected void done() {
                try {
                    List<Employee> employees = get();
                    view.displayEmployees(new ArrayList<>(employees));
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi tải dữ liệu nhân viên: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Thêm nhân viên mới
     */
    public void addEmployee(Employee employee) {
        new SwingWorker<Employee, Void>() {
            @Override
            protected Employee doInBackground() throws Exception {
                return employeeApiClient.addEmployee(employee);
            }

            @Override
            protected void done() {
                try {
                    Employee newEmployee = get();
                    view.showMessage("Thêm nhân viên thành công!");
                    view.clearForm();
                    loadAllEmployees(); // Tải lại danh sách
                    notifyListeners(); // Thông báo thay đổi
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi thêm nhân viên: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Cập nhật nhân viên
     */
    public void updateEmployee(Employee employee) {
        new Thread(() -> {
            try {
                Employee updatedEmployee = employeeApiClient.updateEmployee(employee);
                SwingUtilities.invokeLater(() -> {
                    view.showMessage("Cập nhật nhân viên thành công!");
                    view.clearForm();
                    loadAllEmployees();
                    if (mainMenuListener != null) {
                        mainMenuListener.onBookDataChanged();
                    }
                });
            } catch (IOException e) {
                // Khi có lỗi, hiển thị và khôi phục lại form
                SwingUtilities.invokeLater(() -> {
                    view.showErrorMessage("Lỗi khi cập nhật nhân viên: " + e.getMessage());
                    if (currentEmployee != null) {
                        view.populateEmployeeDetails(currentEmployee); // Khôi phục lại dữ liệu gốc
                    }
                });
            }
        }).start();
    }

    /**
     * Xóa nhân viên
     */
    public void deleteEmployee(String maNv) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                employeeApiClient.deleteEmployee(maNv);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    view.showMessage("Xóa nhân viên thành công!");
                    view.clearForm();
                    loadAllEmployees(); // Tải lại danh sách
                    notifyListeners(); // Thông báo thay đổi
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi xóa nhân viên: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Tìm kiếm nhân viên
     */
    public void searchEmployees(String query) {
        new SwingWorker<List<Employee>, Void>() {
            @Override
            protected List<Employee> doInBackground() throws Exception {
                if (query == null || query.trim().isEmpty()) {
                    return employeeApiClient.getAllEmployees();
                }
                return employeeApiClient.searchEmployees(query.trim());
            }

            @Override
            protected void done() {
                try {
                    List<Employee> employees = get();
                    view.displayEmployees(new ArrayList<>(employees));
                    if (query != null && !query.trim().isEmpty()) {
                        view.showMessage("Tìm thấy " + employees.size() + " nhân viên phù hợp.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi tìm kiếm nhân viên: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Tìm kiếm nhân viên theo tên
     */
    public void searchEmployeesByName(String name) {
        new SwingWorker<List<Employee>, Void>() {
            @Override
            protected List<Employee> doInBackground() throws Exception {
                return employeeApiClient.searchEmployeesByName(name);
            }

            @Override
            protected void done() {
                try {
                    List<Employee> employees = get();
                    view.displayEmployees(new ArrayList<>(employees));
                    view.showMessage("Tìm thấy " + employees.size() + " nhân viên có tên chứa '" + name + "'.");
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi tìm kiếm theo tên: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Tìm kiếm nhân viên theo số điện thoại
     */
    public void searchEmployeesByPhone(String phone) {
        new SwingWorker<List<Employee>, Void>() {
            @Override
            protected List<Employee> doInBackground() throws Exception {
                return employeeApiClient.searchEmployeesByPhone(phone);
            }

            @Override
            protected void done() {
                try {
                    List<Employee> employees = get();
                    view.displayEmployees(new ArrayList<>(employees));
                    view.showMessage("Tìm thấy " + employees.size() + " nhân viên có số điện thoại chứa '" + phone + "'.");
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi tìm kiếm theo số điện thoại: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Tìm kiếm nhân viên theo khoảng lương
     */
    public void searchEmployeesBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        new SwingWorker<List<Employee>, Void>() {
            @Override
            protected List<Employee> doInBackground() throws Exception {
                return employeeApiClient.searchEmployeesBySalaryRange(minSalary, maxSalary);
            }

            @Override
            protected void done() {
                try {
                    List<Employee> employees = get();
                    view.displayEmployees(new ArrayList<>(employees));
                    view.showMessage("Tìm thấy " + employees.size() + " nhân viên có lương từ " + minSalary + " đến " + maxSalary + ".");
                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorMessage("Lỗi khi tìm kiếm theo khoảng lương: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    /**
     * Xử lý khi nhân viên được chọn từ bảng
     */
    public void onEmployeeSelected() {
        String maNv = view.getSelectedMaNv();
        if (maNv != null && !maNv.isEmpty()) {
            // Lấy thông tin chi tiết và lưu lại
            new Thread(() -> {
                try {
                    currentEmployee = employeeApiClient.getEmployeeById(maNv); // Lưu lại trạng thái gốc
                    if (currentEmployee != null) {
                        SwingUtilities.invokeLater(() -> view.populateEmployeeDetails(currentEmployee));
                    }
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> view.showErrorMessage("Lỗi khi tải chi tiết nhân viên: " + e.getMessage()));
                }
            }).start();
        }
    }

    /**
     * Xóa form và reset trạng thái
     */
    public void clearForm() {
        view.clearForm();
        view.clearTableSelection();
        view.updateButtonStates(false); // Chế độ thêm mới
    }

    /**
     * Khởi tạo dữ liệu ban đầu
     */
    private void initializeData() {
        loadAllEmployees();
    }

    // Đăng ký listener (từ MainMenu)
    public void addListener(MainMenuPresenter listener) {
        this.mainMenuListener = listener;
    }

    // Hủy đăng ký listener
    public void removeListener(MainMenuPresenter listener) {
        this.mainMenuListener = null;
    }
} 