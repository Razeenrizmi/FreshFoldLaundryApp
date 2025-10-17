package com.laundry.freshfoldlaundryapp.repository.order;

import com.laundry.freshfoldlaundryapp.model.order.SpecialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SpecialRequestRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(SpecialRequest request) {
        final String sql = "INSERT INTO SpecialRequest (order_id, name) VALUES (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, request.getOrderId());
            ps.setString(2, request.getName());
            return ps;
        });
    }

    public List<SpecialRequest> findByOrderId(Integer orderId) {
        String sql = "SELECT * FROM SpecialRequest WHERE order_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SpecialRequest request = new SpecialRequest();
            request.setOrderId(rs.getInt("order_id"));
            request.setName(rs.getString("name"));
            return request;
        }, orderId);
    }

    private static class SpecialRequestRowMapper implements RowMapper<SpecialRequest> {
        @Override
        public SpecialRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
            SpecialRequest request = new SpecialRequest();
            request.setOrderId(rs.getInt("order_id"));
            request.setName(rs.getString("name"));
            return request;
        }
    }

    public void deleteByOrderId(Integer orderId) {
        String sql = "DELETE FROM SpecialRequest WHERE order_id = ?";
        jdbcTemplate.update(sql, orderId);
    }

    public void deleteByOrderIdAndName(Integer orderId, String name) {
        String sql = "DELETE FROM SpecialRequest WHERE order_id = ? AND name = ?";
        jdbcTemplate.update(sql, orderId, name);
    }

    public void insert(SpecialRequest request) {
        String sql = "INSERT INTO SpecialRequest (order_id, name) VALUES (?, ?)";
        jdbcTemplate.update(sql, request.getOrderId(), request.getName());
    }
}