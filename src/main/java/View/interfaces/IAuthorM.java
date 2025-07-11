package View.interfaces;

import Model.Author;
import java.util.ArrayList;

public interface IAuthorM {

    void showErrorMessage(String message);

    void displayAuthors(ArrayList<Author> authors);

    String getMaTG();

    String getTenTG();

    void showMessage(String message);

    void clearForm();

    void setMaTG(String maTG);   // ✔ Chuẩn
    void setTenTG(String tenTG); // ✔ Chuẩn
}
