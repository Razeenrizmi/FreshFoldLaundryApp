package com.laundry.freshfoldlaundryapp.repository.order;

import com.laundry.freshfoldlaundryapp.model.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(OrderItem item) {
        String sql = "INSERT INTO OrderItem (order_id, cloth_id, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, item.getOrderId(), item.getClothId(), item.getQuantity());
    }

    public List<OrderItem> findByOrderId(Integer orderId) {
        String sql = "SELECT * FROM OrderItem WHERE order_id = ?";
        return jdbcTemplate.query(sql, new OrderItemRowMapper(), orderId);
    }

    public List<Object[]> findItemsWithClothDetailsByOrderId(Integer orderId) {
        String sql = "SELECT oi.quantity, c.name AS clothName, c.unit_price AS unitPrice, c.image_path AS imagePath, oi.cloth_id " +
                "FROM OrderItem oi " +
                "JOIN Clothes c ON oi.cloth_id = c.cloth_id " +
                "WHERE oi.order_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[]{
                rs.getInt("quantity"),              // Index 0
                rs.getString("clothName"),          // Index 1
                rs.getBigDecimal("unitPrice"),      // Index 2
                rs.getString("imagePath"),          // Index 3
                rs.getInt("cloth_id")               // Index 4 (new)
        }, orderId);
    }

    public void deleteByOrderIdAndClothId(Integer orderId, Integer clothId) {
        String sql = "DELETE FROM OrderItem WHERE order_id = ? AND cloth_id = ?";
        jdbcTemplate.update(sql, orderId, clothId);
    }

    public void updateQuantity(Integer orderId, Integer clothId, Integer quantity) {
        String sql = "UPDATE OrderItem SET quantity = ? WHERE order_id = ? AND cloth_id = ?";
        jdbcTemplate.update(sql, quantity, orderId, clothId);
    }


    private static class OrderItemRowMapper implements RowMapper<OrderItem> {
        @Override
        public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderItem item = new OrderItem();
            item.setOrderId(rs.getInt("order_id"));
            item.setClothId(rs.getInt("cloth_id"));
            item.setQuantity(rs.getInt("quantity"));
            return item;
        }
    }

    public void deleteByOrderId(Integer orderId) {
        String sql = "DELETE FROM OrderItem WHERE order_id = ?";
        jdbcTemplate.update(sql, orderId);
    }
}