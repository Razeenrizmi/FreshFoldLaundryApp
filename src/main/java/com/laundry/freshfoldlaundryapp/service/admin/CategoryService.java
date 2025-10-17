package com.laundry.freshfoldlaundryapp.service.admin;

import com.laundry.freshfoldlaundryapp.DAO.CategoryDAO;
import com.laundry.freshfoldlaundryapp.model.admin.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public boolean addCategory(Category category) {
        try {
            // Validate category data
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be empty");
            }

            // Check if category name already exists
            if (categoryDAO.categoryNameExists(category.getName().trim())) {
                throw new IllegalArgumentException("Category name already exists");
            }

            // Set default image path if not provided
            if (category.getImagePath() == null || category.getImagePath().trim().isEmpty()) {
                category.setImagePath("/images/categories/default.jpg");
            }

            return categoryDAO.createCategory(category);
        } catch (Exception e) {
            System.err.println("Error adding category: " + e.getMessage());
            return false;
        }
    }

    public List<Category> getAllCategories() {
        try {
            return categoryDAO.getAllCategories();
        } catch (Exception e) {
            System.err.println("Error getting all categories: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve categories", e);
        }
    }

    public Category getCategoryById(int id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid category ID");
            }
            return categoryDAO.getCategoryById(id);
        } catch (Exception e) {
            System.err.println("Error getting category by ID: " + e.getMessage());
            return null;
        }
    }

    public boolean updateCategory(Category category) {
        try {
            if (category == null || category.getCategoryId() <= 0) {
                throw new IllegalArgumentException("Invalid category data");
            }

            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be empty");
            }

            // Check if new name already exists (excluding current category)
            Category existingCategory = categoryDAO.getCategoryById(category.getCategoryId());
            if (existingCategory != null &&
                !existingCategory.getName().equals(category.getName()) &&
                categoryDAO.categoryNameExists(category.getName().trim())) {
                throw new IllegalArgumentException("Category name already exists");
            }

            return categoryDAO.updateCategory(category);
        } catch (Exception e) {
            System.err.println("Error updating category: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCategory(int id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid category ID");
            }

            // Check if category has associated clothes
            int clothesCount = categoryDAO.getClothesCountInCategory(id);
            if (clothesCount > 0) {
                throw new IllegalArgumentException("Cannot delete category with associated clothes items");
            }

            return categoryDAO.deleteCategory(id);
        } catch (Exception e) {
            System.err.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }

    public boolean categoryNameExists(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return false;
            }
            return categoryDAO.categoryNameExists(name.trim());
        } catch (Exception e) {
            System.err.println("Error checking category name: " + e.getMessage());
            return false;
        }
    }

    public int getClothesCountInCategory(int categoryId) {
        try {
            return categoryDAO.getClothesCountInCategory(categoryId);
        } catch (Exception e) {
            System.err.println("Error getting clothes count: " + e.getMessage());
            return 0;
        }
    }
}
