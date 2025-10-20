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
@RequestMapping("/pickup-driver")  // Changed from "/driver" to "/pickup-driver" to avoid conflict
public class PickupDriverController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private DriverService driverService;

    @GetMapping("")
    public String driverDashboard(@RequestParam(required = false) Long driverId, Model model) {
        // Get all drivers for navigation
        List<Driver> drivers = driverService.getAllDrivers();
        model.addAttribute("drivers", drivers);

        // If no driverId specified, use first driver or default
        if (driverId == null && !drivers.isEmpty()) {
            driverId = drivers.get(0).getId();
        }

        model.addAttribute("driverId", driverId);

        // Get driver details
        if (driverId != null) {
            Driver driver = driverService.getDriverById(driverId);
            model.addAttribute("driver", driver);

            // Get orders assigned to this driver (both pickup and delivery)
            List<Orders> orders = ordersService.getOrdersAssignedToDriver(driverId);
            model.addAttribute("orders", orders);

            System.out.println("=== DRIVER DASHBOARD DEBUG ===");
            System.out.println("Driver ID: " + driverId);
            System.out.println("Driver name: " + (driver != null ? driver.getName() : "Unknown"));
            System.out.println("Orders assigned: " + orders.size());
            for (Orders order : orders) {
                System.out.println("Order " + order.getOrderId() + ": status='" + order.getStatus() +
                                 "', pickupDriverId=" + order.getPickupDriverId() +
                                 ", deliveryDriverId=" + order.getDeliveryDriverId());
            }
            System.out.println("===============================");
        }

        return "pickupDelivery/driver";
    }

    @PostMapping("/updateStatus/{orderId}")
    public String updateOrderStatus(@PathVariable Integer orderId,
                                  @RequestParam String status,
                                  @RequestParam Long driverId,
                                  RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== DRIVER STATUS UPDATE DEBUG ===");
            System.out.println("Order ID: " + orderId);
            System.out.println("New Status: " + status);
            System.out.println("Driver ID: " + driverId);

            // Get the current order
            Orders order = ordersService.getOrderById(orderId);
            if (order == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Order not found!");
                return "redirect:/pickup-driver?driverId=" + driverId;
            }

            // Validate the status transition based on your workflow
            String currentStatus = order.getStatus();
            boolean isValidTransition = false;
            String successMessage = "";

            if ("Picked Up".equals(status) && "Ready for Pickup".equals(currentStatus)) {
                // Pickup driver marking order as picked up
                isValidTransition = true;
                successMessage = "Order marked as picked up successfully!";
            } else if ("delivered".equals(status) && "out_for_delivery".equals(currentStatus)) {
                // Delivery driver marking order as delivered
                isValidTransition = true;
                successMessage = "Order marked as delivered successfully!";
            }

            if (isValidTransition) {
                boolean success = ordersService.updateOrderStatus(orderId, status);
                if (success) {
                    System.out.println("SUCCESS: Order status updated from '" + currentStatus + "' to '" + status + "'");
                    redirectAttributes.addFlashAttribute("successMessage", successMessage);
                } else {
                    System.err.println("ERROR: Failed to update order status in database");
                    redirectAttributes.addFlashAttribute("errorMessage", "Failed to update order status. Please try again.");
                }
            } else {
                System.err.println("ERROR: Invalid status transition from '" + currentStatus + "' to '" + status + "'");
                redirectAttributes.addFlashAttribute("errorMessage",
                    "Invalid status update. Current status: " + currentStatus + ", attempted: " + status);
            }

        } catch (Exception e) {
            System.err.println("EXCEPTION: Error updating order status: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + e.getMessage());
        }

        return "redirect:/pickup-driver?driverId=" + driverId;
    }

    @GetMapping("/pickup-orders")
    public String viewPickupOrders(@RequestParam Long driverId, Model model) {
        // Show only pickup orders for this driver
        List<Orders> pickupOrders = ordersService.getPickupOrdersForDriver(driverId);
        Driver driver = driverService.getDriverById(driverId);

        model.addAttribute("orders", pickupOrders);
        model.addAttribute("driver", driver);
        model.addAttribute("driverId", driverId);
        model.addAttribute("orderType", "pickup");

        return "pickupDelivery/driver";
    }

    @GetMapping("/delivery-orders")
    public String viewDeliveryOrders(@RequestParam Long driverId, Model model) {
        // Show only delivery orders for this driver
        List<Orders> deliveryOrders = ordersService.getDeliveryOrdersForDriver(driverId);
        Driver driver = driverService.getDriverById(driverId);

        model.addAttribute("orders", deliveryOrders);
        model.addAttribute("driver", driver);
        model.addAttribute("driverId", driverId);
        model.addAttribute("orderType", "delivery");

        return "pickupDelivery/driver";
    }
}
