/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View.interfaces;

import Model.Book;
import java.util.ArrayList;
import java.util.List;

public interface IBookM {

 String getSelectedMaSach();
 void clearForm();
    void displayBooks(ArrayList<Book> books);

    void populateBookDetails(Book book);


    void updateImagePreview(String imagePath);

    void populateNhaXBNames(List<String> names);

    void populateTacGiaNames(List<String> names);

    void populateDanhMucNames(List<String> names);

    void showMessage(String message);

    void showErrorMessage(String message);
    
}

