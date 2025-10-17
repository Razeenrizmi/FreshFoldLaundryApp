package com.laundry.freshfoldlaundryapp.service.admin;

import com.laundry.freshfoldlaundryapp.dto.Admin.OrderManagementDTO;
import com.laundry.freshfoldlaundryapp.DAO.OrderManagementDAO;

import java.util.List;

public class OrderManagementService {
    private OrderManagementDAO orderManagementDAO = new OrderManagementDAO();

    public List<OrderManagementDTO> getAllOrders() {
        return orderManagementDAO.getAllOrders();
    }

    public List<OrderManagementDTO> filterOrders(String status, String searchTerm) {
        return orderManagementDAO.filterOrders(status, searchTerm);
    }
}