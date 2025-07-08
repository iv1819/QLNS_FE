/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenter;

import java.io.IOException;

import API.CategoryApiClient;
import View.Category;

/**
 *
 * @author nam11
 */
public class CategoryP {
    private Entity.Category categoryM;
    private Category view;
    private CategoryApiClient apiClient;
    public CategoryP(Category view) {
        this.view = view;
        this.apiClient = new CategoryApiClient();
    }
    public void loadAllCategories() {
        try {
            var categories = apiClient.getAllCategories();
            // Convert List<Category> to List<String> by extracting names
            java.util.List<String> categoryNames = new java.util.ArrayList<>();
            for (var category : categories) {
                categoryNames.add(category.getTenDanhMuc()); // Assumes Category has getName()
            }
            view.displayCategories(categoryNames);
        } catch (Exception e) {
            view.displayError("Failed to load categories: " + e.getMessage());
        }
    }
    public void addCategory(String categoryName) {
        try {
            Entity.Category newCategory = new Entity.Category();
            newCategory.setTenDanhMuc(categoryName); // Assumes Category has setName()
            Entity.Category addedCategory = apiClient.addCategory(newCategory);
            view.displaySuccess("Thêm danh mục " + addedCategory.getTenDanhMuc());
            loadAllCategories(); // Reload categories after adding
        } catch (Exception e) {
            view.displayError("Failed to add category: " + e.getMessage());
        }
    }
    public void deleteCategory(String tenDanhMuc) throws IOException {
         // Lấy mã danh mục từ tên
        String maDanhMuc = apiClient.getIdByTenDanhMuc(tenDanhMuc);
        apiClient.deleteCategory(maDanhMuc);
    }
    public void updateCategory(String oldCategoryName, String newCategoryName) {
        try {
            // Lấy mã danh mục từ tên cũ
            String maDanhMuc = apiClient.getIdByTenDanhMuc(oldCategoryName);
            Entity.Category updatedCategory = new Entity.Category();
            updatedCategory.setMaDanhMuc(maDanhMuc);
            updatedCategory.setTenDanhMuc(newCategoryName); // Assumes Category has setName()
            Entity.Category result = apiClient.updateCategory(updatedCategory);
            view.displaySuccess("Cập nhật danh mục thành công: " + result.getTenDanhMuc());
            loadAllCategories(); // Reload categories after updating
        } catch (Exception e) {
            view.displayError("Failed to update category: " + e.getMessage());
        }
    }

    
}
