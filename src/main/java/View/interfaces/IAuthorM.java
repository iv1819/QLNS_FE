/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.interfaces;

import Model.Author;
import java.util.ArrayList;

/**
 *
 * @author trang
 */
public interface IAuthorM {

    public void showErrorMessage(String string);

    public void displayAuthors(ArrayList<Author> arrayList);

    public String getMaTG();

    public String getTenTG();

    public void showMessage(String thêm_tác_giả_thành_công);

    public void clearForm();

    void setMaTG();

    void setTenTG();
}
