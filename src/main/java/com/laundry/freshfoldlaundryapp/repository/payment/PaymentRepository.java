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
        try {
            // Use Pid instead of payment_id, and handle date conversion for payment_datetime
            String sql = "INSERT INTO payment (order_id, payment_method, payment_status, amount, payment_datetime, card_holder, card_number, expiry_date, cvv) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Log the SQL and parameters for debugging
            System.out.println("=== PAYMENT INSERT DEBUG ===");
            System.out.println("SQL: " + sql);
            System.out.println("Parameters:");
            System.out.println("  order_id: " + payment.getOrderId());
            System.out.println("  payment_method: " + payment.getPaymentMethod());
            System.out.println("  payment_status: " + payment.getPaymentStatus());
            System.out.println("  amount: " + payment.getAmount());
            System.out.println("  payment_datetime: " + payment.getPaymentDatetime());
            System.out.println("  card_holder: " + payment.getCardHolder());
            System.out.println("  card_number: " + payment.getCardNumber());
            System.out.println("  expiry_date: " + payment.getExpiryDate());
            System.out.println("  cvv: " + payment.getCvv());

            // Convert payment_datetime to Date
            java.sql.Date paymentDate = payment.getPaymentDatetime() != null ?
                    java.sql.Date.valueOf(payment.getPaymentDatetime().toLocalDate()) :
                    new java.sql.Date(System.currentTimeMillis());

            System.out.println("  Converted payment_date: " + paymentDate);
            System.out.println("=========================");

            int result = jdbcTemplate.update(sql,
                    payment.getOrderId(),
                    payment.getPaymentMethod(),
                    payment.getPaymentStatus(),
                    payment.getAmount(),
                    paymentDate,
                    payment.getCardHolder(),
                    payment.getCardNumber(),
                    payment.getExpiryDate(),
                    payment.getCvv()
            );

            System.out.println("✓ Payment saved successfully! Result: " + result);
            return result;

        } catch (Exception e) {
            // Log the full error for debugging with complete stack trace
            System.err.println("=== PAYMENT INSERT ERROR ===");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getName());
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getMessage());
                System.err.println("Root cause class: " + e.getCause().getClass().getName());
            }
            System.err.println("Full stack trace:");
            e.printStackTrace();
            System.err.println("=========================");
            throw new RuntimeException("Failed to save payment: " + e.getMessage(), e);
        }
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
            p.setPaymentId(rs.getInt("Pid")); // Use Pid instead of payment_id
            p.setOrderId(rs.getInt("order_id"));
            p.setPaymentMethod(rs.getString("payment_method"));
            p.setPaymentStatus(rs.getString("payment_status"));
            p.setAmount(rs.getDouble("amount"));

            // payment_datetime is DATE type, not TIMESTAMP, so convert it properly
            java.sql.Date paymentDate = rs.getDate("payment_datetime");
            if (paymentDate != null) {
                p.setPaymentDatetime(paymentDate.toLocalDate().atStartOfDay());
            }

            // Handle card fields (may be null for cash payments)
            p.setCardHolder(rs.getString("card_holder"));
            p.setCardNumber(rs.getString("card_number"));
            p.setExpiryDate(rs.getString("expiry_date"));
            p.setCvv(rs.getString("cvv"));

            return p;
        };
    }
}
