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

@Controller
public class DeliveryCoordinatorController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private DriverService driverService;

    @GetMapping("/coordinator")
    public String coordinatorDashboard(Model model, @RequestParam(required = false) String status) {
        List<Orders> orders;

        // Modified workflow: Show pending orders for pickup driver assignment
        if (status == null || status.isEmpty()) {
            orders = ordersService.getOrdersByStatusList("Pending");  // Changed from "COMPLETED" to "Pending"
            status = "Pending"; // Set for display purposes
        } else {
            orders = ordersService.getOrdersByStatusList(status);
        }

        // Get available drivers for the new workflow (use existing method)
        List<Driver> availableDrivers = driverService.getAllDrivers(); // Use getAllDrivers() since getAvailablePickupDrivers() doesn't exist
        List<Driver> allDrivers = driverService.getAllDrivers(); // Get all drivers for debugging

        // Debug logging
        System.out.println("=== COORDINATOR DASHBOARD DEBUG ===");
        System.out.println("Status filter: " + status);
        System.out.println("Orders found: " + orders.size());
        System.out.println("All drivers: " + allDrivers.size());
        System.out.println("Available pickup drivers: " + availableDrivers.size());

        for (Orders order : orders) {
            System.out.println("Order " + order.getOrderId() + ": status='" + order.getStatus() +
                             "', pickupDriverId=" + order.getPickupDriverId());
        }

        for (Driver driver : allDrivers) {
            System.out.println("Driver " + driver.getId() + ": " + driver.getName());
        }

        model.addAttribute("orders", orders);
        model.addAttribute("drivers", availableDrivers);
        model.addAttribute("status", status);
        return "pickupDelivery/coordinator";
    }

    @PostMapping("/assignPickupDriver/{orderId}")
    public String assignPickupDriver(@PathVariable Integer orderId, @RequestParam Long driverId, RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: Attempting to assign pickup driver " + driverId + " to order " + orderId);

        try {
            boolean success = ordersService.assignPickupDriver(orderId, driverId);
            if (success) {
                // Update status to "Ready for Pickup" after successful pickup driver assignment
                boolean statusUpdated = ordersService.updateOrderStatus(orderId, "Ready for Pickup");
                if (statusUpdated) {
                    System.out.println("SUCCESS: Pickup driver assigned and status updated to Ready for Pickup");
                    redirectAttributes.addFlashAttribute("successMessage", "Pickup driver assigned successfully! Order is now ready for pickup.");
                } else {
                    System.out.println("SUCCESS: Pickup driver assigned but failed to update status automatically");
                    redirectAttributes.addFlashAttribute("successMessage", "Pickup driver assigned successfully!");
                }
            } else {
                System.err.println("ERROR: Failed to assign pickup driver - database operation returned false");
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to assign pickup driver. Please check if the order exists and database columns are present.");
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION: Error assigning pickup driver: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error assigning pickup driver: " + e.getMessage());
        }

        return "redirect:/coordinator";
    }

    @PostMapping("/assignDeliveryDriver/{orderId}")
    public String assignDeliveryDriver(@PathVariable Integer orderId, @RequestParam Long driverId, RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: Attempting to assign delivery driver " + driverId + " to order " + orderId);

        try {
            boolean success = ordersService.assignDeliveryDriver(orderId, driverId);
            if (success) {
                // Automatically update status to "ready_for_delivery" after successful driver assignment
                boolean statusUpdated = ordersService.updateOrderStatus(orderId, "ready_for_delivery");
                if (statusUpdated) {
                    System.out.println("SUCCESS: Delivery driver assigned and status updated to ready_for_delivery");
                    redirectAttributes.addFlashAttribute("successMessage", "Delivery driver assigned successfully! Order is now ready for delivery.");
                } else {
                    System.out.println("SUCCESS: Driver assigned but failed to update status automatically");
                    redirectAttributes.addFlashAttribute("successMessage", "Delivery driver assigned successfully!");
                }
            } else {
                System.err.println("ERROR: Failed to assign delivery driver - database operation returned false");
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to assign delivery driver. Please check if the order exists and database columns are present.");
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION: Error assigning delivery driver: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error assigning delivery driver: " + e.getMessage());
        }

        return "redirect:/coordinator";
    }

    @PostMapping("/updateStatus/{id}")
    public String updateStatus(@PathVariable Integer id, @RequestParam String status, RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: Attempting to update order " + id + " status to " + status);

        try {
            boolean success = ordersService.updateOrderStatus(id, status);
            if (success) {
                System.out.println("SUCCESS: Order status updated successfully");
                redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully!");
            } else {
                System.err.println("ERROR: Failed to update order status - database operation returned false");
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to update order status.");
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION: Error updating order status: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + e.getMessage());
        }

        return "redirect:/coordinator";
    }
}