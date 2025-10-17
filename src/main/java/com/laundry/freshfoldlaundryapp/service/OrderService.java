package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.model.Order;
import com.laundry.freshfoldlaundryapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Save a new order
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by ID (returns Optional)
    public Optional<Order> getOrderByIdOptional(Long orderId) {
        return orderRepository.findById(orderId);
    }

    // Get order by ID (returns Order entity directly - for manager controller compatibility)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    // Get orders by customer ID
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // Get orders by status
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    // Get orders by payment method
    public List<Order> getOrdersByPaymentMethod(String paymentMethod) {
        return orderRepository.findByPaymentMethod(paymentMethod);
    }

    // Get orders by payment status (needed for manager dashboard)
    public List<Order> getOrdersByPaymentStatus(String paymentStatus) {
        return orderRepository.findByPaymentStatus(paymentStatus);
    }

    // Update order status
    public Order updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    // Update payment status
    public Order updatePaymentStatus(Long orderId, String paymentStatus) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setPaymentStatus(paymentStatus);
            return orderRepository.save(order);
        }
        return null;
    }

    // Update an existing order
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    // Delete order
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }


}
