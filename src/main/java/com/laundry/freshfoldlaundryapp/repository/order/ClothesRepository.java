package com.laundry.freshfoldlaundryapp.repository.order;

import com.laundry.freshfoldlaundryapp.model.order.Clothes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClothesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Clothes> findByCategoryId(Integer categoryId) {
        String sql = "SELECT * FROM Clothes WHERE category_id = ?";
        return jdbcTemplate.query(sql, new ClothesRowMapper(), categoryId);
    }

    public Clothes findById(Integer clothId) {
        String sql = "SELECT * FROM Clothes WHERE cloth_id = ?";
        return jdbcTemplate.queryForObject(sql, new ClothesRowMapper(), clothId);
    }

    private static class ClothesRowMapper implements RowMapper<Clothes> {
        @Override
        public Clothes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Clothes(
                    rs.getInt("cloth_id"),
                    rs.getString("name"),
                    rs.getBigDecimal("unit_price"),
                    rs.getInt("category_id"),
                    rs.getString("image_path")
            );
        }
    }
}