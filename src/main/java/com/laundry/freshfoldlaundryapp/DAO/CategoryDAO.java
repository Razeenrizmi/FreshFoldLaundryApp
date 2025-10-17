package com.laundry.freshfoldlaundryapp.DAO;

import com.laundry.freshfoldlaundryapp.model.admin.Category;
import com.laundry.freshfoldlaundryapp.util.DBConnect;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // Create a new category
    public boolean createCategory(Category category) {
        String sql = "INSERT INTO Category (name, image_path) VALUES (?, ?)";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getImagePath());

            int result = ps.executeUpdate();
            System.out.println("Category created successfully!");
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error creating category: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Category ORDER BY name ASC";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setImagePath(rs.getString("image_path"));

                // Handle timestamp conversion
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    category.setCreatedAt(timestamp.toLocalDateTime());
                }

                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all categories: " + e.getMessage());
            e.printStackTrace();
        }

        return categories;
    }

    // Get category by ID
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM Category WHERE category_id = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setImagePath(rs.getString("image_path"));

                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    category.setCreatedAt(timestamp.toLocalDateTime());
                }

                return category;
            }
        } catch (SQLException e) {
            System.err.println("Error getting category by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Update category
    public boolean updateCategory(Category category) {
        String sql = "UPDATE Category SET name = ?, image_path = ? WHERE category_id = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getImagePath());
            ps.setInt(3, category.getCategoryId());

            int result = ps.executeUpdate();
            System.out.println("Category updated successfully!");
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete category
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM Category WHERE category_id = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int result = ps.executeUpdate();
            System.out.println("Category deleted successfully!");
            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Check if category name already exists
    public boolean categoryNameExists(String name) {
        String sql = "SELECT COUNT(*) FROM Category WHERE name = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking category name: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Get count of clothes in a category
    public int getClothesCountInCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Clothes WHERE category_id = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting clothes count: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
