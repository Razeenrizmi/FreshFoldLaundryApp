package com.laundry.freshfoldlaundryapp.DAO;

import com.laundry.freshfoldlaundryapp.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClothesDAO {

    // Get clothes names for a specific category (for display in category management)
    public List<String> getClothesNamesByCategory(int categoryId) {
        List<String> clothesNames = new ArrayList<>();
        String sql = "SELECT name FROM Clothes WHERE category_id = ? LIMIT 5"; // Limit to first 5 items

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                clothesNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting clothes names by category: " + e.getMessage());
            e.printStackTrace();
        }

        return clothesNames;
    }

    // Get total count of clothes in a category
    public int getClothesCountByCategory(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Clothes WHERE category_id = ?";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting clothes count by category: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
