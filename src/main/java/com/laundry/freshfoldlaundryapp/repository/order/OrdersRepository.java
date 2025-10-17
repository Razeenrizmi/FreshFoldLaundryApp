package com.laundry.freshfoldlaundryapp.repository.order;

import com.laundry.freshfoldlaundryapp.model.order.Orders;
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
public class OrdersRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer save(Orders order) {
        final String sql = "INSERT INTO Orders (customer_id, service_type, pickup_datetime, delivery_datetime, order_time, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getCustomerId());
            ps.setString(2, order.getServiceType());
            ps.setTimestamp(3, Timestamp.valueOf(order.getPickupDatetime()));
            ps.setTimestamp(4, Timestamp.valueOf(order.getDeliveryDatetime()));
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now())); // Set current time
            ps.setString(6, "Pending"); // Initial status
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public Orders findById(Integer orderId) {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        return jdbcTemplate.queryForObject(sql, new OrderRowMapper(), orderId);
    }

    public List<Orders> findCurrent() {
        String sql = "SELECT * FROM Orders WHERE status = 'Pending' or status = 'Paid'";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    public List<Orders> findCompleted() {
        String sql = "SELECT o.*, u.first_name, u.last_name, u.phone_number, u.address, " +
                    "CONCAT(u.first_name, ' ', u.last_name) as customer_name, " +
                    "u.phone_number as customer_phone, u.address as customer_address " +
                    "FROM Orders o JOIN user u ON o.customer_id = u.id " +
                    "WHERE o.status = 'Completed'";
        return jdbcTemplate.query(sql, new OrderWithCustomerRowMapper());
    }

    public List<Orders> findInProgress() {
        String sql = "SELECT o.*, u.first_name, u.last_name, u.phone_number, u.address, " +
                    "CONCAT(u.first_name, ' ', u.last_name) as customer_name, " +
                    "u.phone_number as customer_phone, u.address as customer_address " +
                    "FROM Orders o JOIN user u ON o.customer_id = u.id " +
                    "WHERE o.status = 'In_Progress'";
        return jdbcTemplate.query(sql, new OrderWithCustomerRowMapper());
    }

    public List<Orders> findPending() {
        String sql = "SELECT o.*, u.first_name, u.last_name, u.phone_number, u.address, " +
                    "CONCAT(u.first_name, ' ', u.last_name) as customer_name, " +
                    "u.phone_number as customer_phone, u.address as customer_address " +
                    "FROM Orders o JOIN user u ON o.customer_id = u.id " +
                    "WHERE o.status = 'Pending'";
        return jdbcTemplate.query(sql, new OrderWithCustomerRowMapper());
    }

    public List<Orders> findByCustomerId(Integer customerId) {
        String sql = "SELECT * FROM Orders WHERE customer_id = ? ORDER BY order_time DESC";
        return jdbcTemplate.query(sql, new OrderRowMapper(), customerId);
    }

    public List<Orders> findByCustomerIdAndStatus(Integer customerId, String status) {
        String sql = "SELECT * FROM Orders WHERE customer_id = ? AND status = ? ORDER BY order_time DESC";
        return jdbcTemplate.query(sql, new OrderRowMapper(), customerId, status);
    }

    public List<Orders> findAll() {
        String sql = "SELECT o.*, u.first_name, u.last_name, u.phone_number, u.address, " +
                    "CONCAT(u.first_name, ' ', u.last_name) as customer_name, " +
                    "u.phone_number as customer_phone, u.address as customer_address, " +
                    "COALESCE(o.pickup_driver_id, 0) as pickup_driver_id, " +
                    "COALESCE(o.delivery_driver_id, 0) as delivery_driver_id " +
                    "FROM Orders o JOIN user u ON o.customer_id = u.id " +
                    "ORDER BY o.order_time DESC";
        return jdbcTemplate.query(sql, new OrderWithCustomerAndDriversRowMapper());
    }

    // Add generic method to find orders by status with user information (fixed to use user table)
    public List<Orders> findByStatus(String status) {
        String sql = "SELECT o.*, u.first_name, u.last_name, u.phone_number, u.address, " +
                    "CONCAT(u.first_name, ' ', u.last_name) as customer_name, " +
                    "u.phone_number as customer_phone, u.address as customer_address " +
                    "FROM Orders o JOIN user u ON o.customer_id = u.id " +
                    "WHERE o.status = ? ORDER BY o.order_time DESC";
        return jdbcTemplate.query(sql, new OrderWithCustomerRowMapper(), status);
    }

    private static class OrderRowMapper implements RowMapper<Orders> {
        @Override
        public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
            Orders order = new Orders();
            order.setOrderId(rs.getInt("order_id"));
            order.setCustomerId(rs.getInt("customer_id"));
            order.setServiceType(rs.getString("service_type"));
            order.setPickupDatetime(rs.getTimestamp("pickup_datetime").toLocalDateTime());
            order.setDeliveryDatetime(rs.getTimestamp("delivery_datetime").toLocalDateTime());
            order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
            order.setStatus(rs.getString("status"));

            // Set additional fields for dashboard display
            try {
                order.setPrice(rs.getDouble("price"));
            } catch (SQLException e) {
                order.setPrice(0.0); // Default if column doesn't exist
            }

            try {
                order.setClothType(rs.getString("cloth_type"));
            } catch (SQLException e) {
                order.setClothType("Mixed Items"); // Default if column doesn't exist
            }

            try {
                order.setSpecialInstructions(rs.getString("special_instructions"));
            } catch (SQLException e) {
                order.setSpecialInstructions(null); // Default if column doesn't exist
            }

            // Set dashboard date fields (convert from datetime to date/datetime as needed)
            order.setOrderDate(order.getOrderTime()); // Use order_time as orderDate
            order.setPickupDate(order.getPickupDatetime() != null ? order.getPickupDatetime().toLocalDate() : null);
            order.setDeliveryDate(order.getDeliveryDatetime() != null ? order.getDeliveryDatetime().toLocalDate() : null);

            return order;
        }
    }

    // Row mapper for orders with customer information (used in coordinator dashboard)
    private static class OrderWithCustomerRowMapper implements RowMapper<Orders> {
        @Override
        public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
            Orders order = new Orders();
            order.setOrderId(rs.getInt("order_id"));
            order.setCustomerId(rs.getInt("customer_id"));
            order.setServiceType(rs.getString("service_type"));
            order.setPickupDatetime(rs.getTimestamp("pickup_datetime").toLocalDateTime());
            order.setDeliveryDatetime(rs.getTimestamp("delivery_datetime").toLocalDateTime());
            order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
            order.setStatus(rs.getString("status"));

            // Set customer information from joined Customer table
            order.setCustomerName(rs.getString("customer_name"));
            order.setCustomerPhone(rs.getString("customer_phone"));
            order.setCustomerAddress(rs.getString("customer_address"));

            // Set additional fields for dashboard display
            try {
                order.setPrice(rs.getDouble("price"));
            } catch (SQLException e) {
                order.setPrice(0.0); // Default if column doesn't exist
            }

            try {
                order.setClothType(rs.getString("cloth_type"));
            } catch (SQLException e) {
                order.setClothType("Mixed Items"); // Default if column doesn't exist
            }

            try {
                order.setSpecialInstructions(rs.getString("special_instructions"));
            } catch (SQLException e) {
                order.setSpecialInstructions(null); // Default if column doesn't exist
            }

            try {
                order.setPickupDriverId(rs.getInt("pickup_driver_id"));
            } catch (SQLException e) {
                order.setPickupDriverId(null); // Default if column doesn't exist
            }

            try {
                order.setDeliveryDriverId(rs.getInt("delivery_driver_id"));
            } catch (SQLException e) {
                order.setDeliveryDriverId(null); // Default if column doesn't exist
            }

            // Set dashboard date fields (convert from datetime to date/datetime as needed)
            order.setOrderDate(order.getOrderTime()); // Use order_time as orderDate
            order.setPickupDate(order.getPickupDatetime() != null ? order.getPickupDatetime().toLocalDate() : null);
            order.setDeliveryDate(order.getDeliveryDatetime() != null ? order.getDeliveryDatetime().toLocalDate() : null);

            return order;
        }
    }

    // Row mapper for orders with driver information (used in driver dashboard)
    private static class OrderRowMapperWithDrivers implements RowMapper<Orders> {
        @Override
        public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
            Orders order = new Orders();
            order.setOrderId(rs.getInt("order_id"));
            order.setCustomerId(rs.getInt("customer_id"));
            order.setServiceType(rs.getString("service_type"));
            order.setPickupDatetime(rs.getTimestamp("pickup_datetime").toLocalDateTime());
            order.setDeliveryDatetime(rs.getTimestamp("delivery_datetime").toLocalDateTime());
            order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
            order.setStatus(rs.getString("status"));

            // Set driver IDs - these should now be available from the COALESCE query
            order.setPickupDriverId(rs.getInt("pickup_driver_id"));
            order.setDeliveryDriverId(rs.getInt("delivery_driver_id"));

            // Set additional fields for dashboard display
            try {
                order.setPrice(rs.getDouble("price"));
            } catch (SQLException e) {
                order.setPrice(0.0); // Default if column doesn't exist
            }

            try {
                order.setClothType(rs.getString("cloth_type"));
            } catch (SQLException e) {
                order.setClothType("Mixed Items"); // Default if column doesn't exist
            }

            try {
                order.setSpecialInstructions(rs.getString("special_instructions"));
            } catch (SQLException e) {
                order.setSpecialInstructions(null); // Default if column doesn't exist
            }

            // Set dashboard date fields
            order.setOrderDate(order.getOrderTime());
            order.setPickupDate(order.getPickupDatetime() != null ? order.getPickupDatetime().toLocalDate() : null);
            order.setDeliveryDate(order.getDeliveryDatetime() != null ? order.getDeliveryDatetime().toLocalDate() : null);

            return order;
        }
    }

    // Row mapper for orders with both customer and driver information (used in driver dashboard)
    private static class OrderWithCustomerAndDriversRowMapper implements RowMapper<Orders> {
        @Override
        public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
            Orders order = new Orders();
            order.setOrderId(rs.getInt("order_id"));
            order.setCustomerId(rs.getInt("customer_id"));
            order.setServiceType(rs.getString("service_type"));
            order.setPickupDatetime(rs.getTimestamp("pickup_datetime").toLocalDateTime());
            order.setDeliveryDatetime(rs.getTimestamp("delivery_datetime").toLocalDateTime());
            order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
            order.setStatus(rs.getString("status"));

            // Set customer information from joined user table
            order.setCustomerName(rs.getString("customer_name"));
            order.setCustomerPhone(rs.getString("customer_phone"));
            order.setCustomerAddress(rs.getString("customer_address"));

            // Set driver IDs from COALESCE query
            order.setPickupDriverId(rs.getInt("pickup_driver_id"));
            order.setDeliveryDriverId(rs.getInt("delivery_driver_id"));

            // Set additional fields for dashboard display
            try {
                order.setPrice(rs.getDouble("price"));
            } catch (SQLException e) {
                order.setPrice(0.0); // Default if column doesn't exist
            }

            try {
                order.setClothType(rs.getString("cloth_type"));
            } catch (SQLException e) {
                order.setClothType("Mixed Items"); // Default if column doesn't exist
            }

            try {
                order.setSpecialInstructions(rs.getString("special_instructions"));
            } catch (SQLException e) {
                order.setSpecialInstructions(null); // Default if column doesn't exist
            }

            // Set dashboard date fields
            order.setOrderDate(order.getOrderTime());
            order.setPickupDate(order.getPickupDatetime() != null ? order.getPickupDatetime().toLocalDate() : null);
            order.setDeliveryDate(order.getDeliveryDatetime() != null ? order.getDeliveryDatetime().toLocalDate() : null);

            return order;
        }
    }

    public void update(Orders order) {
        String sql = "UPDATE Orders SET service_type = ?, pickup_datetime = ?, delivery_datetime = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, order.getServiceType(), Timestamp.valueOf(order.getPickupDatetime()), Timestamp.valueOf(order.getDeliveryDatetime()), order.getOrderId());
    }

    public void deleteById(Integer orderId) {
        String sql = "DELETE FROM Orders WHERE order_id = ?";
        jdbcTemplate.update(sql, orderId);
    }

    // Customer-specific methods
    public List<Orders> findPendingByCustomerId(Integer customerId) {
        String sql = "SELECT * FROM Orders WHERE customer_id = ? AND status = 'Pending' ORDER BY order_time DESC";
        return jdbcTemplate.query(sql, new OrderRowMapper(), customerId);
    }

    public List<Orders> findInProgressByCustomerId(Integer customerId) {
        String sql = "SELECT * FROM Orders WHERE customer_id = ? AND status = 'In_Progress' ORDER BY order_time DESC";
        return jdbcTemplate.query(sql, new OrderRowMapper(), customerId);
    }

    public List<Orders> findCompletedByCustomerId(Integer customerId) {
        String sql = "SELECT * FROM Orders WHERE customer_id = ? AND status = 'Completed' ORDER BY order_time DESC";
        return jdbcTemplate.query(sql, new OrderRowMapper(), customerId);
    }

    // Driver assignment methods for coordinator dashboard
    public boolean assignPickupDriver(Integer orderId, Long driverId) {
        try {
            String sql = "UPDATE Orders SET pickup_driver_id = ? WHERE order_id = ?";
            int result = jdbcTemplate.update(sql, driverId, orderId);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Error assigning pickup driver: " + e.getMessage());
            return false;
        }
    }

    public boolean assignDeliveryDriver(Integer orderId, Long driverId) {
        try {
            String sql = "UPDATE Orders SET delivery_driver_id = ? WHERE order_id = ?";
            int result = jdbcTemplate.update(sql, driverId, orderId);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Error assigning delivery driver: " + e.getMessage());
            return false;
        }
    }

    public boolean updateOrderStatus(Integer orderId, String status) {
        try {
            String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
            int result = jdbcTemplate.update(sql, status, orderId);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }
}
