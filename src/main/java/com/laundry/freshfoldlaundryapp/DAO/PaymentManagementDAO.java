package com.laundry.freshfoldlaundryapp.DAO;

import com.laundry.freshfoldlaundryapp.dto.Admin.PaymentManagementDTO;
import com.laundry.freshfoldlaundryapp.util.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentManagementDAO {
    public List<PaymentManagementDTO> getAllPayments() {
        List<PaymentManagementDTO> paymentList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.Pid, p.order_id, CONCAT(u.first_name, ' ', u.last_name) as customer_name, ");
        sql.append("p.payment_method, p.amount, p.payment_datetime, p.payment_status, ");
        sql.append("p.card_holder, p.card_number, p.expiry_date, p.cvv ");
        sql.append("FROM payment p ");
        sql.append("JOIN Orders o ON p.order_id = o.order_id ");
        sql.append("JOIN user u ON o.customer_id = u.id ");
        sql.append("ORDER BY p.payment_datetime DESC");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PaymentManagementDTO dto = new PaymentManagementDTO();
                dto.setPaymentId(rs.getInt("Pid"));
                dto.setOrderId(rs.getInt("order_id"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setPaymentMethod(rs.getString("payment_method"));
                dto.setAmount(rs.getBigDecimal("amount"));

                Timestamp timestamp = rs.getTimestamp("payment_datetime");
                if (timestamp != null) {
                    dto.setPaymentDate(timestamp.toLocalDateTime());
                }

                dto.setStatus(rs.getString("payment_status"));
                dto.setTransactionId(rs.getString("card_holder")); // Using card_holder as transaction reference
                dto.setCardType(rs.getString("card_number")); // Using card_number as card type reference
                dto.setPaymentNotes(rs.getString("expiry_date")); // Using expiry_date as notes reference

                paymentList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentList;
    }

    // Simple method to filter payments by status only
    public List<PaymentManagementDTO> filterPaymentsByStatus(String status) {
        List<PaymentManagementDTO> paymentList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.Pid, p.order_id, CONCAT(u.first_name, ' ', u.last_name) as customer_name, ");
        sql.append("p.payment_method, p.amount, p.payment_datetime, p.payment_status, ");
        sql.append("p.card_holder, p.card_number, p.expiry_date, p.cvv ");
        sql.append("FROM payment p ");
        sql.append("JOIN Orders o ON p.order_id = o.order_id ");
        sql.append("JOIN user u ON o.customer_id = u.id ");

        // Add status filter if provided
        if (status != null && !status.isEmpty()) {
            sql.append(" WHERE p.payment_status = ?");
        }

        sql.append(" ORDER BY p.payment_datetime DESC");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            // Set status parameter if provided
            if (status != null && !status.isEmpty()) {
                ps.setString(1, status);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PaymentManagementDTO dto = new PaymentManagementDTO();
                dto.setPaymentId(rs.getInt("Pid"));
                dto.setOrderId(rs.getInt("order_id"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setPaymentMethod(rs.getString("payment_method"));
                dto.setAmount(rs.getBigDecimal("amount"));

                Timestamp timestamp = rs.getTimestamp("payment_datetime");
                if (timestamp != null) {
                    dto.setPaymentDate(timestamp.toLocalDateTime());
                }

                dto.setStatus(rs.getString("payment_status"));
                dto.setTransactionId(rs.getString("card_holder"));
                dto.setCardType(rs.getString("card_number"));
                dto.setPaymentNotes(rs.getString("expiry_date"));

                paymentList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentList;
    }
}