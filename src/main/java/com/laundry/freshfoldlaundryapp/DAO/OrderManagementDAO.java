package com.laundry.freshfoldlaundryapp.DAO;

import com.laundry.freshfoldlaundryapp.dto.Admin.OrderManagementDTO;
import com.laundry.freshfoldlaundryapp.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderManagementDAO {
    public List<OrderManagementDTO> getAllOrders() {
        List<OrderManagementDTO> orderList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT o.order_id, CONCAT(u.first_name, ' ', u.last_name) as customer_name, u.email as customer_email, ");
        sql.append("o.order_time, o.status, ");
        sql.append("pd.name as pickup_driver_name, dd.name as delivery_driver_name, o.special_instructions, ");
        sql.append("p.amount as total_amount ");
        sql.append("FROM Orders o ");
        sql.append("JOIN user u ON o.customer_id = u.id ");
        sql.append("LEFT JOIN drivers pd ON o.pickup_driver_id = pd.id ");
        sql.append("LEFT JOIN drivers dd ON o.delivery_driver_id = dd.id ");
        sql.append("LEFT JOIN payment p ON o.order_id = p.order_id");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderManagementDTO dto = new OrderManagementDTO();
                dto.setOrderId(rs.getInt("order_id"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setCustomerEmail(rs.getString("customer_email"));

                // Convert SQL timestamp to LocalDateTime
                Timestamp timestamp = rs.getTimestamp("order_time");
                if (timestamp != null) {
                    dto.setOrderDate(timestamp.toLocalDateTime());
                }

                dto.setTotalAmount(rs.getBigDecimal("total_amount"));
                dto.setStatus(rs.getString("status"));
                //dto.setDeliveryAddress(rs.getString("customer_address"));

                // Prioritize delivery driver, fall back to pickup driver if delivery driver is not assigned
                String deliveryDriverName = rs.getString("delivery_driver_name");
                String pickupDriverName = rs.getString("pickup_driver_name");
                dto.setDriverName(deliveryDriverName != null ? deliveryDriverName : pickupDriverName);

                dto.setOrderNotes(rs.getString("special_instructions"));
                orderList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<OrderManagementDTO> filterOrders(String status, String searchTerm) {
        List<OrderManagementDTO> allOrders = getAllOrders();
        List<OrderManagementDTO> filteredOrders = new ArrayList<>();

        for (OrderManagementDTO order : allOrders) {
            boolean statusMatch = (status == null || status.isEmpty() ||
                    order.getStatus().equalsIgnoreCase(status));

            boolean searchMatch = (searchTerm == null || searchTerm.isEmpty() ||
                    order.getCustomerName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    order.getCustomerEmail().toLowerCase().contains(searchTerm.toLowerCase()));

            if (statusMatch && searchMatch) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }
}