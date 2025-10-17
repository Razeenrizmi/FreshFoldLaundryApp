package com.laundry.freshfoldlaundryapp.DAO;

import com.laundry.freshfoldlaundryapp.dto.Admin.FeedbackManagementDTO;
import com.laundry.freshfoldlaundryapp.util.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    public List<FeedbackManagementDTO> getAllFeedback() {
        List<FeedbackManagementDTO> feedbackList = new ArrayList<>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT f.feedback_id, f.customer_id, f.order_id, ");
        sql.append("f.content, f.date, f.status, f.response, o.status as order_status ");
        sql.append("FROM feedback f ");
        sql.append("JOIN orders o ON f.order_id = o.order_id ");
        sql.append("ORDER BY f.date DESC");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FeedbackManagementDTO dto = new FeedbackManagementDTO();
                dto.setFeedbackId(rs.getInt("feedback_id"));
                dto.setCustomerId(rs.getInt("customer_id"));

                dto.setCustomerName("Customer #" + rs.getInt("customer_id"));
                dto.setOrderId(rs.getInt("order_id"));
                dto.setContent(rs.getString("content"));
                dto.setDate(rs.getTimestamp("date").toLocalDateTime());
                dto.setStatus(rs.getString("status"));
                dto.setResponse(rs.getString("response"));
                dto.setOrderStatus(rs.getString("order_status"));
                feedbackList.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public boolean updateFeedback(int feedbackId, String response, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE feedback SET response = ?, status = ? WHERE feedback_id = ?");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setString(1, response);
            ps.setString(2, status);
            ps.setInt(3, feedbackId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFeedback(int feedbackId) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM feedback WHERE feedback_id = ?");

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setInt(1, feedbackId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}