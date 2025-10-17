package com.laundry.freshfoldlaundryapp.service.pickupDelivery;

import com.laundry.freshfoldlaundryapp.model.pickupDelivery.Order;
import com.laundry.freshfoldlaundryapp.repository.pickupDelivery.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("pickupDeliveryOrderService")
public class OrderService {
    @Autowired
    @Qualifier("pickupDeliveryOrderRepository")
    private OrderRepository orderRepository;


    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get orders assigned to a specific driver
    public List<Order> getOrdersForDriver(Long driverId) {
        List<Order> pickupOrders = orderRepository.findByPickupDriverId(driverId.intValue());
        List<Order> deliveryOrders = orderRepository.findByDeliveryDriverId(driverId.intValue());

        // Combine both lists and remove duplicates
        pickupOrders.addAll(deliveryOrders);
        return pickupOrders.stream().distinct().collect(Collectors.toList());
    }

    // Check if time slot is available
    public boolean isTimeSlotAvailable(LocalDate date, LocalTime time, String type) {
        List<Order> ordersOnDate = "pickup".equals(type) ?
                orderRepository.findByPickupDate(date) :
                orderRepository.findByDeliveryDate(date);

        // Simple availability check - can be enhanced based on business rules
        return ordersOnDate.size() < 10; // Max 10 orders per day per type
    }

    // Get today's pickups
    public List<Order> getTodayPickups() {
        return orderRepository.findByPickupDate(LocalDate.now());
    }

    // Get today's deliveries
    public List<Order> getTodayDeliveries() {
        return orderRepository.findByDeliveryDate(LocalDate.now());
    }

    // Get order by ID
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    // Update order status
    public Order updateStatus(Long id, String newStatus) {
        Order order = getOrderById(id);
        if (order != null) {
            order.setStatus(newStatus);
            return orderRepository.save(order);
        }
        return null;
    }

    // Assign pickup driver
    public Order assignPickupDriver(Long orderId, Long driverId) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setPickupDriverId(driverId.intValue());
            return orderRepository.save(order);
        }
        return null;
    }

    // Assign delivery driver
    public Order assignDeliveryDriver(Long orderId, Long driverId) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setDeliveryDriverId(driverId.intValue());
            return orderRepository.save(order);
        }
        return null;
    }

    // Get pending orders
    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus("PENDING");
    }

    // Get orders by status
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    // Get orders excluding certain status
    public List<Order> getOrdersExcludingStatus(String status) {
        return orderRepository.findByStatusNot(status);
    }

    // Save order
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}