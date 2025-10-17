package com.laundry.freshfoldlaundryapp.service.admin;

import com.laundry.freshfoldlaundryapp.dto.Admin.CategoryManagementDTO;
import com.laundry.freshfoldlaundryapp.DAO.CategoryManagementDAO;

import java.util.List;

public class CategoryManagementService {
    private CategoryManagementDAO categoryDAO = new CategoryManagementDAO();

    public List<CategoryManagementDTO> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public boolean addCategory(String categoryName, String description, String iconClass) {
        return categoryDAO.addCategory(categoryName, description, iconClass);
    }
}