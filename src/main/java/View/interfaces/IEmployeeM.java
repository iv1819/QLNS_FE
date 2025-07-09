package View.interfaces;

import Model.Employee;
import java.util.ArrayList;
import java.util.Date;

/**
 * Interface cho Employee Management View
 * Định nghĩa các method mà View phải implement
 */
public interface IEmployeeM {
    
    /**
     * Hiển thị danh sách nhân viên trong bảng
     * @param employees Danh sách nhân viên cần hiển thị
     */
    void displayEmployees(ArrayList<Employee> employees);
    
    /**
     * Điền thông tin nhân viên vào form
     * @param employee Đối tượng nhân viên cần điền
     */
    void populateEmployeeDetails(Employee employee);
    
    /**
     * Xóa tất cả dữ liệu trong form
     */
    void clearForm();
    
    /**
     * Hiển thị thông báo thành công
     * @param message Nội dung thông báo
     */
    void showMessage(String message);
    
    /**
     * Hiển thị thông báo lỗi
     * @param message Nội dung thông báo lỗi
     */
    void showErrorMessage(String message);
    
    /**
     * Lấy mã nhân viên được chọn từ bảng
     * @return Mã nhân viên được chọn, null nếu không có gì được chọn
     */
    String getSelectedMaNv();
    
    /**
     * Lấy thông tin nhân viên từ form
     * @return Đối tượng Employee với thông tin từ form
     */
    Employee getEmployeeFromForm();
    
    /**
     * Kiểm tra xem form có hợp lệ không
     * @return true nếu form hợp lệ, false nếu không
     */
    boolean validateForm();
    
    /**
     * Cập nhật trạng thái các button (enable/disable)
     * @param isEditing true nếu đang ở chế độ chỉnh sửa, false nếu đang ở chế độ thêm mới
     */
    void updateButtonStates(boolean isEditing);
    
    /**
     * Xóa selection trong bảng
     */
    void clearTableSelection();
    
    /**
     * Lấy ngày từ JDateChooser ngày sinh
     * @return Date object, null nếu không có ngày được chọn
     */
    Date getNgaySinhFromDateChooser();
    
    /**
     * Lấy ngày từ JDateChooser ngày vào làm
     * @return Date object, null nếu không có ngày được chọn
     */
    Date getNgayVaoLamFromDateChooser();
    
    /**
     * Set ngày cho JDateChooser ngày sinh
     * @param date Date object cần set
     */
    void setNgaySinhToDateChooser(Date date);
    
    /**
     * Set ngày cho JDateChooser ngày vào làm
     * @param date Date object cần set
     */
    void setNgayVaoLamToDateChooser(Date date);
    
    /**
     * Set mã nhân viên cho trường mã nhân viên trên form
     * @param maNv mã nhân viên mới
     */
    void setMaNv(String maNv);
} 