package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.model.Order;
import com.laundry.freshfoldlaundryapp.model.order.Orders;
import com.laundry.freshfoldlaundryapp.model.order.Customer;
import com.laundry.freshfoldlaundryapp.service.CustomerService;
import com.laundry.freshfoldlaundryapp.service.CustomUserDetails;
import com.laundry.freshfoldlaundryapp.service.order.OrdersService;
import com.laundry.freshfoldlaundryapp.repository.order.CustomerRepository;
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

    @Autowired
    private CustomerRepository customerRepository;


    private LocalDate today;

    @GetMapping("/customer-dashboard")
    public String showCustomerDashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            // Get authenticated user details
            if (userDetails == null || userDetails.getUsername() == null) {
                model.addAttribute("orders", java.util.Collections.emptyList());
                model.addAttribute("inProgressOrders", java.util.Collections.emptyList());
                model.addAttribute("pendingOrders", java.util.Collections.emptyList());
                model.addAttribute("completedOrders", java.util.Collections.emptyList());
                model.addAttribute("customerId", 0L);
                model.addAttribute("errorMessage", "User not authenticated");
                return "customer-dashboard";
            }

            // Get the correct customer ID from Customer table (not the user ID from authentication)
            Integer actualCustomerId = getCustomerIdFromUserDetails(userDetails);

            System.out.println("DEBUG: Authenticated User ID: " + userDetails.getUserId());
            System.out.println("DEBUG: Authenticated Username: " + userDetails.getUsername());
            System.out.println("DEBUG: Mapped Customer ID: " + actualCustomerId);

            if (actualCustomerId == null) {
                model.addAttribute("orders", java.util.Collections.emptyList());
                model.addAttribute("inProgressOrders", java.util.Collections.emptyList());
                model.addAttribute("pendingOrders", java.util.Collections.emptyList());
                model.addAttribute("completedOrders", java.util.Collections.emptyList());
                model.addAttribute("customerId", 0L);
                model.addAttribute("errorMessage", "Customer record not found");
                return "customer-dashboard";
            }

            // Fetch orders using the CORRECT customer ID from Customer table
            System.out.println("Fetching orders for customer ID: " + actualCustomerId + " from Orders table...");

            List<Orders> orders = ordersService.getOrdersByCustomerId(actualCustomerId);
            List<Orders> pendingOrders = ordersService.getPendingOrdersByCustomer(actualCustomerId);
            List<Orders> inProgressOrders = ordersService.getInProgressOrdersByCustomer(actualCustomerId);
            List<Orders> completedOrders = ordersService.getCompletedOrdersByCustomer(actualCustomerId);

            System.out.println("Customer orders fetched: " + (orders != null ? orders.size() : "null"));
            System.out.println("Customer pending orders fetched: " + (pendingOrders != null ? pendingOrders.size() : "null"));
            System.out.println("Customer in progress orders fetched: " + (inProgressOrders != null ? inProgressOrders.size() : "null"));
            System.out.println("Customer completed orders fetched: " + (completedOrders != null ? completedOrders.size() : "null"));

            // Filter out null orders and add to model
            model.addAttribute("orders", orders != null ? orders.stream().filter(java.util.Objects::nonNull).toList() : java.util.Collections.emptyList());
            model.addAttribute("inProgressOrders", inProgressOrders != null ? inProgressOrders.stream().filter(java.util.Objects::nonNull).toList() : java.util.Collections.emptyList());
            model.addAttribute("pendingOrders", pendingOrders != null ? pendingOrders.stream().filter(java.util.Objects::nonNull).toList() : java.util.Collections.emptyList());
            model.addAttribute("completedOrders", completedOrders != null ? completedOrders.stream().filter(java.util.Objects::nonNull).toList() : java.util.Collections.emptyList());
            model.addAttribute("customerId", actualCustomerId.longValue());

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

    /**
     * Gets the correct customer ID from the Customer table based on the authenticated user's email.
     * This maps from the authentication user ID to the actual customer ID used for orders.
     */
    private Integer getCustomerIdFromUserDetails(CustomUserDetails userDetails) {
        try {
            if (userDetails == null || userDetails.getUsername() == null) {
                return null;
            }

            // Find the Customer record by email (username is email in your system)
            Customer existingCustomer = customerRepository.findByEmail(userDetails.getUsername());

            if (existingCustomer != null) {
                return existingCustomer.getCustomerId();
            }

            // If no customer record exists, this means the user hasn't placed any orders yet
            return null;

        } catch (Exception e) {
            System.err.println("Error getting customer ID from user details: " + e.getMessage());
            return null;
        }
    }

    // New Order redirect mapping
    @GetMapping("/new-order")
    public String redirectToNewOrder() {
        return "redirect:/order/browse";
    }
}
