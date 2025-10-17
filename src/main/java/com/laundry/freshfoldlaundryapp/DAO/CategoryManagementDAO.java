package com.laundry.freshfoldlaundryapp.DAO;

import com.laundry.freshfoldlaundryapp.dto.Admin.CategoryManagementDTO;
import com.laundry.freshfoldlaundryapp.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryManagementDAO {
    public List<CategoryManagementDTO> getAllCategories() {
        List<CategoryManagementDTO> categoryList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.category_id, c.name, c.image_path, c.created_at, ");
        sql.append("(SELECT COUNT(*) FROM Clothes WHERE Clothes.category_id = c.category_id) as clothes_count ");
        sql.append("FROM Category c ORDER BY c.name");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoryManagementDTO dto = new CategoryManagementDTO();
                dto.setCategoryId(rs.getInt("category_id"));
                dto.setCategoryName(rs.getString("name")); // Fixed: use "name" instead of "category_name"
                dto.setImagePath(rs.getString("image_path"));
                dto.setCreatedAt(rs.getString("created_at"));
                dto.setClothesCount(rs.getInt("clothes_count"));

                // Fetch clothes names for this category
                dto.setClothesNames(getClothesNamesForCategory(con, dto.getCategoryId()));

                categoryList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    private List<String> getClothesNamesForCategory(Connection con, int categoryId) throws SQLException {
        List<String> clothesNames = new ArrayList<>();
        String sql = "SELECT name FROM Clothes WHERE category_id = ? ORDER BY name LIMIT 5"; // Fixed: use MySQL LIMIT instead of TOP

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                clothesNames.add(rs.getString("name"));
            }
        }
        return clothesNames;
    }

    public boolean addCategory(String categoryName, String description, String iconClass) {
        String sql = "INSERT INTO Category (name, image_path, created_at) VALUES (?, ?, NOW())"; // Fixed: use "name" instead of "category_name" and NOW() for timestamp

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoryName);
            ps.setString(2, description);

            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}