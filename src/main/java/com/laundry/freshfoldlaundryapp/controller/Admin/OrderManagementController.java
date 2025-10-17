package com.laundry.freshfoldlaundryapp.controller.Admin;


import com.laundry.freshfoldlaundryapp.dto.Admin.OrderManagementDTO;
import com.laundry.freshfoldlaundryapp.service.admin.OrderManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller("orderManagementController")
public class OrderManagementController {
    private OrderManagementService orderManagementService = new OrderManagementService();

    @GetMapping("/order-management")
    public String orderManagement(Model model) {
        List<OrderManagementDTO> orderList = orderManagementService.getAllOrders();
        model.addAttribute("orderList", orderList);
        return "admin/order-Management";
    }

    @GetMapping("/order-management/filter")
    public String filterOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            Model model) {

        List<OrderManagementDTO> orderList = orderManagementService.filterOrders(status, search);
        model.addAttribute("orderList", orderList);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("searchTerm", search);
        return "admin/order-Management";
    }
}
