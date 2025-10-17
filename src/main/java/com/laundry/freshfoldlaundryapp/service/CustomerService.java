package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.model.Order;
import com.laundry.freshfoldlaundryapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private OrderRepository orderRepository;

    // Simple method using customer ID
    public List<Order> getOrdersForCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // Get orders by customer ID and status
    public List<Order> getOrdersForCustomerByStatus(Long customerId, String status) {
        return orderRepository.findByCustomerIdAndStatus(customerId, status);
    }
}