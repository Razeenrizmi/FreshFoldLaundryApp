package com.laundry.freshfoldlaundryapp.controller.pickupDelivery;

import com.laundry.freshfoldlaundryapp.model.order.Orders;
import com.laundry.freshfoldlaundryapp.model.pickupDelivery.Driver;
import com.laundry.freshfoldlaundryapp.service.pickupDelivery.DriverService;
import com.laundry.freshfoldlaundryapp.service.order.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DriverController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private DriverService driverService;

    // Driver dashboard: /driver?driverId=1
    @GetMapping("/driver")
    public String driverDashboard(@RequestParam Long driverId, Model model) {
        try {
            // Get all orders assigned to this driver (either for pickup or delivery)
            List<Orders> allOrders = ordersService.getAllOrders();

            // Debug: Show all orders and their driver assignments
            System.out.println("=== DRIVER DASHBOARD DEBUG for Driver " + driverId + " ===");
            System.out.println("Total orders in system: " + allOrders.size());

            for (Orders order : allOrders) {
                System.out.println("Order " + order.getOrderId() + ": status='" + order.getStatus() +
                                 "', pickupDriverId=" + order.getPickupDriverId() +
                                 ", deliveryDriverId=" + order.getDeliveryDriverId());
            }

            // Filter orders assigned to this driver - FIXED to handle 0 vs actual driver IDs
            List<Orders> driverOrders = allOrders.stream()
                .filter(order -> {
                    boolean isPickupDriver = order.getPickupDriverId() != null &&
                                           order.getPickupDriverId().longValue() == driverId.longValue();
                    boolean isDeliveryDriver = order.getDeliveryDriverId() != null &&
                                             order.getDeliveryDriverId() != 0 &&
                                             order.getDeliveryDriverId().longValue() == driverId.longValue();

                    boolean assigned = isPickupDriver || isDeliveryDriver;
                    System.out.println("Order " + order.getOrderId() + " assigned to driver " + driverId + ": " + assigned +
                                     " (pickup: " + isPickupDriver + ", delivery: " + isDeliveryDriver + ")");
                    return assigned;
                })
                .collect(Collectors.toList());

            Driver driver = driverService.getDriverById(driverId);
            List<Driver> allDrivers = driverService.getAllDrivers();

            model.addAttribute("orders", driverOrders);
            model.addAttribute("driver", driver);
            model.addAttribute("drivers", allDrivers);
            model.addAttribute("driverId", driverId);

            System.out.println("DEBUG: Driver " + driverId + " has " + driverOrders.size() + " assigned orders");

        } catch (Exception e) {
            System.err.println("ERROR: Failed to load driver dashboard: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to load driver dashboard: " + e.getMessage());
        }

        return "pickupDelivery/driver";
    }

    // Driver updates status - Enhanced for new workflow
    @PostMapping("/driver/updateStatus/{id}")
    public String updateStatus(@PathVariable Integer id, @RequestParam String status,
                             @RequestParam Long driverId, RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: Driver " + driverId + " attempting to update order " + id + " to status: " + status);

        try {
            Orders order = ordersService.getOrderById(id);
            if (order == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Order not found.");
                return "redirect:/driver?driverId=" + driverId;
            }

            boolean authorized = false;
            String actionDescription = "";

            // Check authorization and perform appropriate action
            if ("picked_up".equals(status) && order.getPickupDriverId() != null &&
                order.getPickupDriverId().equals(driverId)) {
                authorized = true;
                actionDescription = "Order marked as picked up";
            }
            else if ("delivered".equals(status) && order.getDeliveryDriverId() != null &&
                     order.getDeliveryDriverId().equals(driverId) && "out_for_delivery".equals(order.getStatus())) {
                authorized = true;
                actionDescription = "Order marked as delivered";
            }

            if (authorized) {
                boolean success = ordersService.updateOrderStatus(id, status);
                if (success) {
                    System.out.println("SUCCESS: " + actionDescription);
                    redirectAttributes.addFlashAttribute("successMessage", actionDescription + " successfully!");
                } else {
                    System.err.println("ERROR: Failed to update order status in database");
                    redirectAttributes.addFlashAttribute("errorMessage", "Failed to update order status.");
                }
            } else {
                System.err.println("ERROR: Driver " + driverId + " not authorized to update order " + id + " to " + status);
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to perform this action on this order.");
            }

        } catch (Exception e) {
            System.err.println("EXCEPTION: Error updating order status: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + e.getMessage());
        }

        return "redirect:/driver?driverId=" + driverId;
    }
}