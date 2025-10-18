package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.model.Order;
import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.model.order.Orders;
import com.laundry.freshfoldlaundryapp.service.order.OrdersService;
import com.laundry.freshfoldlaundryapp.service.OrderService;
import com.laundry.freshfoldlaundryapp.service.UserService;
import com.laundry.freshfoldlaundryapp.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private UserService userService;

    @GetMapping("/manager-dashboard")
    public String managerDashboard(Model model, Authentication authentication) {
        // Get pending payments for verification
//        List<Order> pendingPayments = ordersService.getOrdersByPaymentStatus("PENDING_VERIFICATION");

        // Get confirmed orders needing staff assignment
        List<Orders> ordersForAssignment = ordersService.getOrdersByStatusList("Pending");

        // Get all staff members (laundry staff)
        List<User> staffList = userService.getUsersByRole("STAFF");

        // Get all delivery staff members
//        List<User> deliveryStaffList = userService.getUsersByRole("DELIVERY_STAFF");

        // Get completed orders needing delivery assignment
//        List<Order> ordersForDelivery = ordersService.getOrdersByStatus("COMPLETED");

//        model.addAttribute("pendingPayments", pendingPayments);
        model.addAttribute("ordersForAssignment", ordersForAssignment);
        model.addAttribute("staffList", staffList);
//        model.addAttribute("deliveryStaffList", deliveryStaffList);
//        model.addAttribute("ordersForDelivery", ordersForDelivery);

        Long managerId = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            managerId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        model.addAttribute("managerId", managerId);

        return "manager-dashboard";
    }

    // Also add a root mapping for easier access
    @GetMapping("")
    public String managerDashboardRoot(Model model, Authentication authentication) {
        return managerDashboard(model, authentication);
    }

//    @PostMapping("/verify-payment")
//    @ResponseBody
//    public ResponseEntity<?> verifyPayment(@RequestParam Long orderId,
//                                         @RequestParam String action,
//                                         Authentication authentication) {
//        try {
//            // Get the manager's user details
//            CustomUserDetails managerDetails = (CustomUserDetails) authentication.getPrincipal();
//            Long managerId = managerDetails.getUserId();
//
//            // Find the order
//            Order order = ordersService.getOrderById(orderId);
//            if (order == null) {
//                return ResponseEntity.badRequest()
//                    .body(Map.of("success", false, "message", "Order not found"));
//            }
//
//            // Check if the order is in the correct state for verification
//            if (!"PENDING_VERIFICATION".equals(order.getPaymentStatus())) {
//                return ResponseEntity.badRequest()
//                    .body(Map.of("success", false, "message", "Order is not pending payment verification"));
//            }
//
//            if ("approve".equals(action)) {
//                // Approve the payment
//                order.setPaymentStatus("VERIFIED");
//                order.setStatus("CONFIRMED");
//                order.setVerifiedBy(managerId);
//                order.setVerificationDate(LocalDateTime.now());
//
//                ordersService.updateOrder(order);
//
//                return ResponseEntity.ok(Map.of(
//                    "success", true,
//                    "message", "Payment approved successfully. Order is now confirmed.",
//                    "orderId", orderId
//                ));
//
//            } else if ("reject".equals(action)) {
//                // Reject the payment
//                order.setPaymentStatus("REJECTED");
//                order.setStatus("CANCELLED");
//                order.setVerifiedBy(managerId);
//                order.setVerificationDate(LocalDateTime.now());
//
//                ordersService.updateOrder(order);
//
//                return ResponseEntity.ok(Map.of(
//                    "success", true,
//                    "message", "Payment rejected. Order has been cancelled.",
//                    "orderId", orderId
//                ));
//
//            } else {
//                return ResponseEntity.badRequest()
//                    .body(Map.of("success", false, "message", "Invalid action. Use 'approve' or 'reject'"));
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                .body(Map.of("success", false, "message", "Error processing payment verification: " + e.getMessage()));
//        }
//    }

    @PostMapping("/assign-staff")
    @ResponseBody
    public ResponseEntity<?> assignStaff(@RequestParam int orderId,
                                       @RequestParam Long staffId,
                                       Authentication authentication) {
        try {
            // Find the order
            Orders order = ordersService.getOrderById(orderId);
            if (order == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Order not found"));
            }

            // Check if the order is confirmed and ready for assignment
            if (!"Pending".equals(order.getStatus())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Order is not ready for staff assignment"));
            }

            // Assign the staff member using repository method to persist to database
            boolean staffAssigned = ordersService.assignStaffToOrder(orderId, staffId);

            if (!staffAssigned) {
                return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", "Failed to assign staff to order"));
            }

            // Update order status to IN_PROGRESS
            ordersService.updateOrderStatus(orderId, "IN_PROGRESS");

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Staff assigned successfully. Order is now in progress.",
                "orderId", orderId,
                "staffId", staffId
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "Error assigning staff: " + e.getMessage()));
        }
    }

//    @PostMapping("/assign-delivery")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> assignDelivery(@RequestParam Long orderId,
//                                                              @RequestParam Long deliveryStaffId) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Order order = ordersService.getOrderById(orderId); // Fixed method name
//            User deliveryStaff = userService.getUserById(deliveryStaffId);
//
//            if (order == null) {
//                response.put("success", false);
//                response.put("message", "Order not found");
//                return ResponseEntity.badRequest().body(response);
//            }
//
//            order.setDeliveryStaffId(deliveryStaffId);
//            order.setStatus("OUT_FOR_DELIVERY");
//            order.setAddress(userService.getUserById(order.getCustomerId()).getAddress());
//            ordersService.saveOrder(order);
//
//            response.put("success", true);
//            response.put("message", "Order assigned for delivery to " + deliveryStaff.getFirstName() + "!");
//
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Error assigning delivery: " + e.getMessage());
//        }
//
//        return ResponseEntity.ok(response);
//    }
}
