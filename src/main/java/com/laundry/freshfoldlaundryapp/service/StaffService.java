package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.model.Order;
import com.laundry.freshfoldlaundryapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
public class StaffService {

    @Autowired
    private OrderRepository orderRepository;

    // Get all pending orders for staff to work on (since we don't have staff assignment yet)
    public List<Order> getAssignedTasksForStaff(Long staffId) {
        try {
            // Return all pending orders that staff can work on
            return orderRepository.findByStaffId(staffId);
        } catch (Exception e) {
            // Return empty list if there's an error
            return new ArrayList<>();
        }
    }

    // Alternative method to get all orders for staff dashboard
    public List<Order> getAllOrdersForStaff() {
        try {
            return orderRepository.findAllByOrderByOrderDateDesc();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Get orders by status for staff
    public List<Order> getOrdersByStatus(String status) {
        try {
            return orderRepository.findByStatus(status);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public boolean markTaskAsCompleted(Long taskId) {
        try {
            Order order = orderRepository.findById(taskId).orElse(null);
            if (order != null) {
                order.setStatus("COMPLETED");
                orderRepository.save(order);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean markTaskInProgress(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null) {
                order.setStatus("IN_PROGRESS");
                orderRepository.save(order);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Update order status (for staff to move orders through different stages)
    public boolean updateOrderStatus(Long orderId, String newStatus) {
        try {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null) {
                order.setStatus(newStatus);
                orderRepository.save(order);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
