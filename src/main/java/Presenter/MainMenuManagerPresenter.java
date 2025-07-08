package Presenter;

import View.BookM;
import View.EmployeeM;
import View.MainMenu;
import View.MainMenu_Manager2;
import View.OrderM;
import View.interfaces.IMainMenuManager;

/**
 * Presenter cho MainMenuManager
 * Quản lý navigation giữa các form quản lý
 */
public class MainMenuManagerPresenter {
    
    private MainMenu_Manager2 view;
    private MainMenuPresenter main;
    public MainMenuManagerPresenter(MainMenu_Manager2 view, MainMenuPresenter mainMenu) {
        this.view = view;
        this.main = mainMenu;
    }
    
    /**
     * Mở form quản lý sách
     */
    public void openBookManagement() {
        BookM bookManagementFrame = new BookM(main);
        bookManagementFrame.setVisible(true);
    }
    public void openAccount() {
    }
    public void openNXB() {
    }
    public void openAuthor() {
    }
    public void openOrder() {
        OrderM orderM = new OrderM();
        orderM.setVisible(true);
        view.setVisible(false); // Ẩn MainMenuManager
    }
    public void openCustomer() {
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