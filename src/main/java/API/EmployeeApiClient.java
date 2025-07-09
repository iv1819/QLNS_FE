package API;

import Model.Employee;
import okhttp3.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class EmployeeApiClient extends ApiClientBase {
    private static final String EMPLOYEES_API_PATH = "/employees";

    // Constructor kế thừa từ ApiClientBase
    public EmployeeApiClient() {
        super();
    }

    /**
     * Lấy tất cả nhân viên từ API.
     *
     * @return Danh sách các đối tượng Employee.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public List<Employee> getAllEmployees() throws IOException {
        String jsonResponse = sendGetRequest(EMPLOYEES_API_PATH);
        Employee[] employeesArray = objectMapper.readValue(jsonResponse, Employee[].class);
        return Arrays.asList(employeesArray);
    }

    /**
     * Lấy nhân viên theo ID từ API.
     *
     * @param id ID của nhân viên cần lấy.
     * @return Đối tượng Employee.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public Employee getEmployeeById(String id) throws IOException {
        String jsonResponse = sendGetRequest(EMPLOYEES_API_PATH + "/" + id);
        return objectMapper.readValue(jsonResponse, Employee.class);
    }

    /**
     * Thêm một nhân viên mới vào API.
     *
     * @param employee Đối tượng Employee cần thêm.
     * @return Đối tượng Employee đã được thêm (có thể có ID do backend cấp).
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public Employee addEmployee(Employee employee) throws IOException {
        String jsonInputString = objectMapper.writeValueAsString(employee);
        String jsonResponse = sendPostRequest(EMPLOYEES_API_PATH, jsonInputString);
        return objectMapper.readValue(jsonResponse, Employee.class);
    }

    /**
     * Cập nhật một nhân viên hiện có trong API.
     *
     * @param employee Đối tượng Employee cần cập nhật (phải có ID).
     * @return Đối tượng Employee đã được cập nhật.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     * @throws IllegalArgumentException Nếu ID nhân viên là null.
     */
    public Employee updateEmployee(Employee employee) throws IOException {
        if (employee.getMaNv() == null) {
            throw new IllegalArgumentException("ID nhân viên không được để trống khi cập nhật.");
        }

        String jsonInputString = objectMapper.writeValueAsString(employee);
        String jsonResponse = sendPutRequest(EMPLOYEES_API_PATH + "/" + employee.getMaNv(), jsonInputString);
        return objectMapper.readValue(jsonResponse, Employee.class);
    }

    /**
     * Xóa một nhân viên khỏi API.
     *
     * @param employeeId ID của nhân viên cần xóa.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public void deleteEmployee(String employeeId) throws IOException {
        sendDeleteRequest(EMPLOYEES_API_PATH + "/" + employeeId);
    }

    /**
     * Tìm kiếm nhân viên theo từ khóa (tên hoặc số điện thoại).
     *
     * @param query Từ khóa tìm kiếm.
     * @return Danh sách các đối tượng Employee.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public List<Employee> searchEmployees(String query) throws IOException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        String jsonResponse = sendGetRequest(EMPLOYEES_API_PATH + "/search?query=" + encodedQuery);
        Employee[] employeesArray = objectMapper.readValue(jsonResponse, Employee[].class);
        return Arrays.asList(employeesArray);
    }

    /**
     * Tìm kiếm nhân viên theo tên.
     *
     * @param name Tên nhân viên cần tìm.
     * @return Danh sách các đối tượng Employee.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public List<Employee> searchEmployeesByName(String name) throws IOException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        String jsonResponse = sendGetRequest(EMPLOYEES_API_PATH + "/search/name?name=" + encodedName);
        Employee[] employeesArray = objectMapper.readValue(jsonResponse, Employee[].class);
        return Arrays.asList(employeesArray);
    }

    /**
     * Tìm kiếm nhân viên theo số điện thoại.
     *
     * @param phone Số điện thoại cần tìm.
     * @return Danh sách các đối tượng Employee.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public List<Employee> searchEmployeesByPhone(String phone) throws IOException {
        String encodedPhone = URLEncoder.encode(phone, StandardCharsets.UTF_8.toString());
        String jsonResponse = sendGetRequest(EMPLOYEES_API_PATH + "/search/phone?phone=" + encodedPhone);
        Employee[] employeesArray = objectMapper.readValue(jsonResponse, Employee[].class);
        return Arrays.asList(employeesArray);
    }

    /**
     * Tìm kiếm nhân viên theo khoảng lương.
     *
     * @param minSalary Lương tối thiểu.
     * @param maxSalary Lương tối đa.
     * @return Danh sách các đối tượng Employee.
     * @throws IOException Nếu có lỗi trong quá trình kết nối hoặc phản hồi không thành công.
     */
    public List<Employee> searchEmployeesBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) throws IOException {
        String jsonResponse = sendGetRequest(EMPLOYEES_API_PATH + "/search/salary?min=" + minSalary + "&max=" + maxSalary);
        Employee[] employeesArray = objectMapper.readValue(jsonResponse, Employee[].class);
        return Arrays.asList(employeesArray);
    }

    /**
     * Lấy mã nhân viên tự động từ API.
     * @return Mã nhân viên mới dạng NVxxx
     * @throws IOException Nếu có lỗi kết nối
     */
    public String getAutoEmployeeId() throws IOException {
        return sendGetRequest(EMPLOYEES_API_PATH + "/auto-id");
    }
} 