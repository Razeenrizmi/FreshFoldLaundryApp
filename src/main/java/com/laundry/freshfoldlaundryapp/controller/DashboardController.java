package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.service.OrderService;
import com.laundry.freshfoldlaundryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String showDashboardPage() {
        return "dashboard"; // This will look for a template named manager-dashboard.html
    }
}