package Presenter;

import View.BookM;
import View.EmployeeM;
import View.interfaces.IMainMenuManager;

/**
 * Presenter cho MainMenuManager
 * Quản lý navigation giữa các form quản lý
 */
public class MainMenuManagerPresenter {
    
    private IMainMenuManager view;
    
    public MainMenuManagerPresenter(IMainMenuManager view) {
        this.view = view;
    }
    
    /**
     * Mở form quản lý sách
     */
    public void openBookManagement() {
        BookM bookManagementFrame = new BookM(this);
        bookManagementFrame.setVisible(true);
        view.setVisible(false); // Ẩn MainMenuManager
    }
    
    /**
     * Mở form quản lý nhân viên
     */
    public void openEmployeeManagement() {
        EmployeeM employeeManagementFrame = new EmployeeM(this);
        employeeManagementFrame.setVisible(true);
        view.setVisible(false); // Ẩn MainMenuManager
    }
    
    /**
     * Quay lại MainMenuManager (được gọi từ các form con)
     */
    public void showMainMenuManager() {
        view.setVisible(true);
    }
} 