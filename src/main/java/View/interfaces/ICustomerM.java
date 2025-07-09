package View.interfaces;

import Model.Customer;
import java.util.ArrayList;

public interface ICustomerM {
    void displayCustomers(ArrayList<Customer> customers);
    void populateCustomerDetails(Customer customer);
    void clearForm();
    void showMessage(String message);
    void showErrorMessage(String message);
    String getSelectedMaKh();
    Customer getCustomerFromForm();
    boolean validateForm();
    void updateButtonStates(boolean isEditing);
    void clearTableSelection();
    void setMaKh(String maKh);
} 