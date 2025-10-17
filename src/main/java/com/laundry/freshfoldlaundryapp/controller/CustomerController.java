package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.model.Order;
import com.laundry.freshfoldlaundryapp.model.order.Orders;
import com.laundry.freshfoldlaundryapp.service.CustomerService;
import com.laundry.freshfoldlaundryapp.service.CustomUserDetails;
import com.laundry.freshfoldlaundryapp.service.order.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrdersService ordersService;


    private LocalDate today;

    @GetMapping("/customer-dashboard")
    public String showCustomerDashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            // Get current customer ID from authenticated user
            Long customerId = userDetails != null ? userDetails.getUserId() : null;

            if (customerId == null) {
                model.addAttribute("orders", java.util.Collections.emptyList());
                model.addAttribute("inProgressOrders", java.util.Collections.emptyList());
                model.addAttribute("pendingOrders", java.util.Collections.emptyList());
                model.addAttribute("completedOrders", java.util.Collections.emptyList());
                model.addAttribute("customerId", 0L);
                model.addAttribute("errorMessage", "User not authenticated");
                return "customer-dashboard";
            }

            // Fetch orders for this specific customer from Orders table using customer-specific methods
            System.out.println("Fetching orders for customer ID: " + customerId + " from Orders table...");

            List<Orders> orders = ordersService.getOrdersByCustomerId(customerId.intValue());
            List<Orders> pendingOrders = ordersService.getPendingOrdersByCustomer(customerId.intValue());
            List<Orders> inProgressOrders = ordersService.getInProgressOrdersByCustomer(customerId.intValue());
            List<Orders> completedOrders = ordersService.getCompletedOrdersByCustomer(customerId.intValue());

            System.out.println("Customer orders fetched: " + (orders != null ? orders.size() : "null"));
            System.out.println("Customer pending orders fetched: " + (pendingOrders != null ? pendingOrders.size() : "null"));
            System.out.println("Customer in progress orders fetched: " + (inProgressOrders != null ? inProgressOrders.size() : "null"));
            System.out.println("Customer completed orders fetched: " + (completedOrders != null ? completedOrders.size() : "null"));

            // Filter out null orders and add to model
            model.addAttribute("orders", orders != null ? orders.stream().filter(java.util.Objects::nonNull).toList() : java.util.Collections.emptyList());
            model.addAttribute("inProgressOrders", inProgressOrders != null ? inProgressOrders.stream().filter(java.util.Objects::nonNull).toList() : java.util.Collections.emptyList());
            model.addAttribute("pendingOrders", pendingOrders != null ? pendingOrders.stream().filter(java.util.Objects::nonNull).toList() : java.util.Collections.emptyList());
            model.addAttribute("completedOrders", completedOrders != null ? completedOrders.stream().filter(java.util.Objects::nonNull).toList() : java.util.Collections.emptyList());
            model.addAttribute("customerId", customerId);

            System.out.println("Successfully prepared model for customer dashboard from Orders table");
            return "customer-dashboard";

        } catch (Exception e) {
            System.err.println("Error in customer dashboard: " + e.getMessage());
            e.printStackTrace();

            // Return a safe fallback with empty lists
            model.addAttribute("orders", java.util.Collections.emptyList());
            model.addAttribute("inProgressOrders", java.util.Collections.emptyList());
            model.addAttribute("pendingOrders", java.util.Collections.emptyList());
            model.addAttribute("completedOrders", java.util.Collections.emptyList());
            model.addAttribute("customerId", userDetails != null ? userDetails.getUserId() : 0L);
            model.addAttribute("errorMessage", "Unable to load orders at this time. Please try again later.");

            return "customer-dashboard";
        }
    }

    // New Order redirect mapping
    @GetMapping("/new-order")
    public String redirectToNewOrder() {
        return "redirect:/order/browse";
    }
}
