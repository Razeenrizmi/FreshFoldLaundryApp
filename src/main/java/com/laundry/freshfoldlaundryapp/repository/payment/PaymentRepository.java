package com.laundry.freshfoldlaundryapp.repository.payment;

import com.laundry.freshfoldlaundryapp.model.payment.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Payment payment) {
        String sql = "INSERT INTO payment (order_id, payment_method, payment_status, amount, payment_datetime, card_holder, card_number, expiry_date, cvv) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                payment.getOrderId(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getAmount(),
                Timestamp.valueOf(payment.getPaymentDatetime()),
                payment.getCardHolder(),
                payment.getCardNumber(),
                payment.getExpiryDate(),
                payment.getCvv()
        );
    }

    public List<Payment> findAll() {
        String sql = "SELECT * FROM payment";
        return jdbcTemplate.query(sql, mapRow());
    }

    public Optional<Payment> findByOrderId(int orderId) {
        String sql = "SELECT * FROM payment WHERE order_id = ?";
        List<Payment> result = jdbcTemplate.query(sql, mapRow(), orderId);
        return result.stream().findFirst();
    }

    public Optional<Payment> findLatestByOrderId(int orderId) {
        String sql = "SELECT * FROM payment WHERE order_id = ? ORDER BY payment_datetime DESC LIMIT 1";
        List<Payment> result = jdbcTemplate.query(sql, mapRow(), orderId);
        return result.stream().findFirst();
    }

    public int updateStatus(int orderId, String status, String method) {
        String sql = "UPDATE payment SET payment_status = ?, payment_method = ?, payment_datetime = ? WHERE order_id = ?";
        return jdbcTemplate.update(sql, status, method, Timestamp.valueOf(LocalDateTime.now()), orderId);
    }

    private RowMapper<Payment> mapRow() {
        return (ResultSet rs, int rowNum) -> {
            Payment p = new Payment();
            p.setPaymentId(rs.getInt("payment_id"));
            p.setOrderId(rs.getInt("order_id"));
            p.setPaymentMethod(rs.getString("payment_method"));
            p.setPaymentStatus(rs.getString("payment_status"));
            p.setAmount(rs.getDouble("amount"));
            p.setPaymentDatetime(rs.getTimestamp("payment_datetime").toLocalDateTime());

            // Handle new card fields (may be null for cash payments)
            p.setCardHolder(rs.getString("card_holder"));
            p.setCardNumber(rs.getString("card_number"));
            p.setExpiryDate(rs.getString("expiry_date"));
            p.setCvv(rs.getString("cvv"));

            return p;
        };
    }
}
