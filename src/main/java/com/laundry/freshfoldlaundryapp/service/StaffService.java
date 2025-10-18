package com.laundry.freshfoldlaundryapp.service;

import com.laundry.freshfoldlaundryapp.repository.order.OrdersRepository;
import com.laundry.freshfoldlaundryapp.model.order.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffService {

    @Autowired
    private OrdersRepository ordersRepository;

    // Get tasks assigned to a specific staff (pickup or delivery assignments)
    public List<Orders> getAssignedTasksForStaff(Long staffId) {
        try {
            return ordersRepository.findByStaffId(staffId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Alternative method to get all orders for staff dashboard
    public List<Orders> getAllOrdersForStaff() {
        try {
            return ordersRepository.findAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Get orders by status for staff
    public List<Orders> getOrdersByStatus(String status) {
        try {
            return ordersRepository.findByStatus(status);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Mark task as completed
    public boolean markTaskAsCompleted(Long taskId) {
        try {
            return ordersRepository.updateOrderStatus(taskId.intValue(), "COMPLETED");
        } catch (Exception e) {
            return false;
        }
    }

    // Mark task in progress
    public boolean markTaskInProgress(Long orderId) {
        try {
            return ordersRepository.updateOrderStatus(orderId.intValue(), "IN_PROGRESS");
        } catch (Exception e) {
            return false;
        }
    }

    // Update order status (for staff to move orders through different stages)
    public boolean updateOrderStatus(Long orderId, String newStatus) {
        try {
            return ordersRepository.updateOrderStatus(orderId.intValue(), newStatus);
        } catch (Exception e) {
            return false;
        }
    }
}
