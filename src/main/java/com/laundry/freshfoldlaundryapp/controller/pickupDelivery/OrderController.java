package com.laundry.freshfoldlaundryapp.controller.pickupDelivery;

import com.laundry.freshfoldlaundryapp.model.pickupDelivery.Order;
import com.laundry.freshfoldlaundryapp.service.pickupDelivery.OrderService;
import com.laundry.freshfoldlaundryapp.service.order.OrdersService;
import com.laundry.freshfoldlaundryapp.model.order.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller("pickupDeliveryOrderController")
public class OrderController {
    @Autowired
    @Qualifier("pickupDeliveryOrderService")
    private OrderService orderService;

    @Autowired
    private OrdersService ordersService; // Add the main orders service

    @GetMapping("/")
    public String home() {
        return "pickupDelivery/index";
    }

    @GetMapping("/schedule")
    public String showScheduleForm(Model model) {
        model.addAttribute("order", new Order());
        return "pickupDelivery/schedule";
    }

    @PostMapping("/schedule")
    public String saveSchedule(@ModelAttribute Order order) {
        orderService.saveOrder(order);
        return "redirect:/pickupDelivery/track";
    }

    @GetMapping("/track")
    public String trackOrders(Model model, @RequestParam(required = false) Integer orderId) {
        List<Orders> orders;
        if (orderId != null) {
            // Show specific order for customer tracking
            Orders order = ordersService.getOrderById(orderId);
            orders = order != null ? List.of(order) : List.of();
        } else {
            // Show all orders for coordinator view - use the main orders service
            orders = ordersService.getAllOrders();
        }
        model.addAttribute("orders", orders);
        model.addAttribute("singleOrder", orderId != null);
        return "pickupDelivery/track";
    }

    // Get available times for scheduling
    @GetMapping("/available-times")
    @ResponseBody
    public ResponseEntity<Boolean> checkTimeAvailability(@RequestParam String type, @RequestParam LocalDate date, @RequestParam LocalTime time) {
        boolean isAvailable = orderService.isTimeSlotAvailable(date, time, type);
        return ResponseEntity.ok(isAvailable);
    }
}