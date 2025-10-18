package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.model.order.Orders;
import com.laundry.freshfoldlaundryapp.service.StaffService;
import com.laundry.freshfoldlaundryapp.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/staff-dashboard")
    public String showStaffDashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long staffId = userDetails.getUserId();
        List<Orders> tasks = staffService.getAllOrdersForStaff();
        List<Orders> assignedTasks = staffService.getAssignedTasksForStaff(staffId);
        List<Orders> inProgressTasks = staffService.getOrdersByStatus("IN_PROGRESS");
        List<Orders> completedTasks = staffService.getOrdersByStatus("COMPLETED");
        java.time.LocalDate today = java.time.LocalDate.now();
        for (Orders order : tasks) {
            if ("ASSIGNED".equalsIgnoreCase(order.getStatus()) || "CONFIRMED".equalsIgnoreCase(order.getStatus())) {
                assignedTasks.add(order);
            } else if ("IN_PROGRESS".equalsIgnoreCase(order.getStatus())) {
                inProgressTasks.add(order);
            } else if ("COMPLETED".equalsIgnoreCase(order.getStatus())) {
                if (order.getDeliveryDate() != null && order.getDeliveryDate().equals(today)) {
                    completedTasks.add(order);
                }
            }
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("assignedTasks", assignedTasks);
        model.addAttribute("inProgressTasks", inProgressTasks);
        model.addAttribute("completedTasks", completedTasks);

        return "staff-dashboard";
    }

    @PostMapping("/staff/update-task-status")
    @ResponseBody
    public Map<String, Object> updateTaskStatus(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long orderId = Long.valueOf(payload.get("orderId").toString());
            String status = payload.get("status").toString();
            boolean success = false;
            if ("COMPLETED".equalsIgnoreCase(status)) {
                success = staffService.markTaskAsCompleted(orderId);
            } else if ("IN_PROGRESS".equalsIgnoreCase(status)) {
                success = staffService.markTaskInProgress(orderId);
            }
            response.put("success", success);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
}
