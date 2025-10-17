package com.laundry.freshfoldlaundryapp.repository.order;

import com.laundry.freshfoldlaundryapp.model.order.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@Repository
public class CustomerRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer save(Customer customer) {
        final String sql = "INSERT INTO Customer (name, email, address, phone_no) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPhoneNo());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public Customer findById(Integer customerId) {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), customerId);
    }

    public Customer findByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), email);
        } catch (Exception e) {
            return null; // Return null if customer not found
        }
    }

    public void update(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, email = ?, address = ?, phone_no = ? WHERE customer_id = ?";
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAddress(), customer.getPhoneNo(), customer.getCustomerId());
    }

    private static class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setName(rs.getString("name"));
            customer.setEmail(rs.getString("email"));
            customer.setAddress(rs.getString("address"));
            customer.setPhoneNo(rs.getString("phone_no"));
            return customer;
        }
    }
}