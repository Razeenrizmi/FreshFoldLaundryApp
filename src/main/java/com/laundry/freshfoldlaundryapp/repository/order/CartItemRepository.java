package com.laundry.freshfoldlaundryapp.repository.order;

import com.laundry.freshfoldlaundryapp.model.order.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CartItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // CREATE - Add item to cart
    public Long saveCartItem(CartItem cartItem) {
        try {
            String sql = "INSERT INTO cart_items (user_id, cloth_id, cloth_name, quantity, unit_price, total_price, " +
                    "category_id, category_name, added_date, session_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, cartItem.getUserId());
                ps.setInt(2, cartItem.getClothId());
                ps.setString(3, cartItem.getClothName());
                ps.setInt(4, cartItem.getQuantity());
                ps.setDouble(5, cartItem.getUnitPrice());
                ps.setDouble(6, cartItem.getTotalPrice());
                ps.setObject(7, cartItem.getCategoryId());
                ps.setString(8, cartItem.getCategoryName());
                ps.setTimestamp(9, Timestamp.valueOf(cartItem.getAddedDate()));
                ps.setString(10, cartItem.getSessionId());
                return ps;
            }, keyHolder);

            return keyHolder.getKey().longValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save cart item: " + e.getMessage(), e);
        }
    }

    // READ - Get all cart items for a user
    public List<CartItem> findByUserId(Long userId) {
        String sql = "SELECT * FROM cart_items WHERE user_id = ? ORDER BY added_date DESC";
        return jdbcTemplate.query(sql, cartItemRowMapper(), userId);
    }

    // READ - Get cart items by session ID (for guest users)
    public List<CartItem> findBySessionId(String sessionId) {
        String sql = "SELECT * FROM cart_items WHERE session_id = ? ORDER BY added_date DESC";
        return jdbcTemplate.query(sql, cartItemRowMapper(), sessionId);
    }

    // READ - Get specific cart item
    public CartItem findByUserIdAndClothId(Long userId, Integer clothId) {
        try {
            String sql = "SELECT * FROM cart_items WHERE user_id = ? AND cloth_id = ?";
            return jdbcTemplate.queryForObject(sql, cartItemRowMapper(), userId, clothId);
        } catch (Exception e) {
            return null;
        }
    }

    // READ - Get cart item by ID
    public CartItem findById(Long cartItemId) {
        try {
            String sql = "SELECT * FROM cart_items WHERE cart_item_id = ?";
            return jdbcTemplate.queryForObject(sql, cartItemRowMapper(), cartItemId);
        } catch (Exception e) {
            return null;
        }
    }

    // UPDATE - Update cart item quantity
    public boolean updateQuantity(Long cartItemId, Integer quantity) {
        try {
            String sql = "UPDATE cart_items SET quantity = ?, total_price = quantity * unit_price WHERE cart_item_id = ?";
            int rowsAffected = jdbcTemplate.update(sql, quantity, cartItemId);
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update cart item quantity: " + e.getMessage(), e);
        }
    }

    // UPDATE - Update entire cart item
    public boolean updateCartItem(CartItem cartItem) {
        try {
            String sql = "UPDATE cart_items SET quantity = ?, unit_price = ?, total_price = ?, " +
                    "cloth_name = ?, category_id = ?, category_name = ? WHERE cart_item_id = ?";
            int rowsAffected = jdbcTemplate.update(sql,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice(),
                    cartItem.getTotalPrice(),
                    cartItem.getClothName(),
                    cartItem.getCategoryId(),
                    cartItem.getCategoryName(),
                    cartItem.getCartItemId());
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update cart item: " + e.getMessage(), e);
        }
    }

    // DELETE - Remove cart item
    public boolean deleteCartItem(Long cartItemId) {
        try {
            String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";
            int rowsAffected = jdbcTemplate.update(sql, cartItemId);
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete cart item: " + e.getMessage(), e);
        }
    }

    // DELETE - Clear user's cart
    public boolean clearCartByUserId(Long userId) {
        try {
            String sql = "DELETE FROM cart_items WHERE user_id = ?";
            int rowsAffected = jdbcTemplate.update(sql, userId);
            return rowsAffected >= 0; // 0 or more items deleted is success
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear cart: " + e.getMessage(), e);
        }
    }

    // DELETE - Clear cart by session ID
    public boolean clearCartBySessionId(String sessionId) {
        try {
            String sql = "DELETE FROM cart_items WHERE session_id = ?";
            int rowsAffected = jdbcTemplate.update(sql, sessionId);
            return rowsAffected >= 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear cart: " + e.getMessage(), e);
        }
    }

    // BUSINESS QUERIES - Get cart total for user
    public Double getCartTotal(Long userId) {
        try {
            String sql = "SELECT COALESCE(SUM(total_price), 0) FROM cart_items WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, Double.class, userId);
        } catch (Exception e) {
            return 0.0;
        }
    }

    // BUSINESS QUERIES - Get cart item count for user
    public Integer getCartItemCount(Long userId) {
        try {
            String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (Exception e) {
            return 0;
        }
    }

    // Row Mapper for CartItem
    private RowMapper<CartItem> cartItemRowMapper() {
        return (rs, rowNum) -> {
            CartItem cartItem = new CartItem();
            cartItem.setCartItemId(rs.getLong("cart_item_id"));
            cartItem.setUserId(rs.getLong("user_id"));
            cartItem.setClothId(rs.getInt("cloth_id"));
            cartItem.setClothName(rs.getString("cloth_name"));
            cartItem.setQuantity(rs.getInt("quantity"));
            cartItem.setUnitPrice(rs.getDouble("unit_price"));
            cartItem.setTotalPrice(rs.getDouble("total_price"));
            cartItem.setCategoryId(rs.getObject("category_id", Integer.class));
            cartItem.setCategoryName(rs.getString("category_name"));

            Timestamp addedTs = rs.getTimestamp("added_date");
            if (addedTs != null) {
                cartItem.setAddedDate(addedTs.toLocalDateTime());
            }

            cartItem.setSessionId(rs.getString("session_id"));
            return cartItem;
        };
    }
}
