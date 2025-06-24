/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.interfaces;

/**
 *
 * @author Admin
 */
public interface ILogin {
    String getUsername();
    String getPassword();
    void showMessage(String msg);
    void navigateToMain(boolean isManager); 
}
