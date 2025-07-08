/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Objects;

/**
 *
 * @author Admin
 */
public class Author {
    private String maTG;
    private String tenTG;

    public Author() {
    }

    public Author(String maTG, String tenTG) {
        this.maTG = maTG;
        this.tenTG = tenTG;
    }

    // Getters
    public String getMaTG() {
        return maTG;
    }

    public String getTenTG() {
        return tenTG;
    }

    // Setters
    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }

    public void setTenTG(String tenTG) {
        this.tenTG = tenTG;
    }

    @Override
    public String toString() {
        return "Author{" +
               "maTG='" + maTG + '\'' +
               ", tenTG='" + tenTG + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(maTG, author.maTG);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maTG);
    }
}
