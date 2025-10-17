package com.laundry.freshfoldlaundryapp.service.order;

import com.laundry.freshfoldlaundryapp.model.order.Customer;
import com.laundry.freshfoldlaundryapp.model.order.OrderItem;
import com.laundry.freshfoldlaundryapp.model.order.Orders;
import com.laundry.freshfoldlaundryapp.model.order.SpecialRequest;
import com.laundry.freshfoldlaundryapp.model.payment.Payment;
import com.laundry.freshfoldlaundryapp.repository.order.*;
import com.laundry.freshfoldlaundryapp.repository.payment.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class OrdersService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private SpecialRequestRepository specialRequestRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ClothesRepository clothesRepository;

    // Get all orders for tracking
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get order by ID for tracking
    public Orders getOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }


    // Add method to get orders by status as a list
    public List<Orders> getOrdersByStatusList(String status) {
        switch (status.toUpperCase()) {
            case "COMPLETED":
                return orderRepository.findCompleted();
            case "PENDING":
                return orderRepository.findPending();
            case "IN_PROGRESS":
                return orderRepository.findInProgress();
            case "READY_FOR_DELIVERY":
                return orderRepository.findByStatus("ready_for_delivery");
            case "OUT_FOR_DELIVERY":
                return orderRepository.findByStatus("out_for_delivery");
            case "DELIVERED":
                return orderRepository.findByStatus("delivered");
            default:
                return orderRepository.findAll();
        }
    }

    // Add method to get completed orders specifically
    public List<Orders> getCompletedOrders() {
        return orderRepository.findCompleted();
    }

    public void saveOrder(Customer customer, Orders order, String specialRequestsStr, List<OrderItem> items) {
        // Validate input data
        validateOrder(customer, order);

        // Save customer and get ID
        Integer customerId = customerRepository.save(customer);
        order.setCustomerId(customerId);

        // Save order and get ID
        Integer orderId = orderRepository.save(order);
        order.setOrderId(orderId);

        // Save special requests if any
        if (specialRequestsStr != null && !specialRequestsStr.isEmpty()) {
            List<String> requests = Arrays.asList(specialRequestsStr.split("\\r?\\n"));
            for (String req : requests) {
                if (!req.trim().isEmpty()) {
                    SpecialRequest specialRequest = new SpecialRequest();
                    specialRequest.setOrderId(orderId);
                    specialRequest.setName(req.trim());
                    specialRequestRepository.save(specialRequest);
                }
            }
        }

        // Save order items if any
        if (items != null) {
            for (OrderItem item : items) {
                item.setOrderId(orderId);
                orderItemRepository.save(item);
            }
        }
    }

    private void validateOrder(Customer customer, Orders order) {
        LocalDateTime now = LocalDateTime.now().withHour(6).withMinute(25).withSecond(0).withNano(0); // 06:25 AM +0530, Sep 28, 2025

        // Date validation (custom check not covered by annotations)
        if (order.getPickupDatetime().isBefore(now)) {
            throw new IllegalArgumentException("Pickup date must be greater than the current date (06:25 AM, Sep 28, 2025)");
        }
        if (order.getDeliveryDatetime().isBefore(now)) {
            throw new IllegalArgumentException("Delivery date must be greater than the current date (06:25 AM, Sep 28, 2025)");
        }
        if (order.getDeliveryDatetime().isBefore(order.getPickupDatetime())) {
            throw new IllegalArgumentException("Delivery date must be greater than pickup date");
        }
    }

    public List<Orders> getCurrentOrders() {
        return orderRepository.findCurrent();
    }

//    public List<Orders> getCompletedOrders() {
//        return orderRepository.findCompleted();
//    }

    public List<Orders> getInProgressOrders() {
        return orderRepository.findInProgress();
    }

    public List<Orders> getPendingOrders() {
        return orderRepository.findPending();
    }


    public Customer getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId);
    }

    public List<SpecialRequest> getSpecialRequestsByOrderId(Integer orderId) {
        return specialRequestRepository.findByOrderId(orderId);
    }

    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public List<Object[]> getOrderItemsWithDetailsByOrderId(Integer orderId) {
        return orderItemRepository.findItemsWithClothDetailsByOrderId(orderId);
    }

    public BigDecimal calculateTotalAmount(Integer orderId) {
        List<Object[]> items = getOrderItemsWithDetailsByOrderId(orderId);
        BigDecimal total = BigDecimal.ZERO;
        for (Object[] item : items) {
            int quantity = (int) item[0];
            BigDecimal unitPrice = (BigDecimal) item[2];
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            total = total.add(itemTotal);
        }
        return total;
    }

    public void updateOrder(Orders order, Customer customer) {
        customerRepository.update(customer);
        orderRepository.update(order);
    }

    public void updateSpecialRequests(Integer orderId, List<String> requests) {
        specialRequestRepository.deleteByOrderId(orderId);
        for (String req : requests) {
            if (!req.trim().isEmpty()) {
                SpecialRequest request = new SpecialRequest();
                request.setOrderId(orderId);
                request.setName(req.trim());
                specialRequestRepository.insert(request);
            }
        }
    }

    public void deleteSpecialRequest(Integer orderId, String name) {
        specialRequestRepository.deleteByOrderIdAndName(orderId, name);
    }

    // Added customer-specific methods for CustomerController
    public List<Orders> getOrdersByCustomerId(Integer customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Orders> getPendingOrdersByCustomer(Integer customerId) {
        return orderRepository.findPendingByCustomerId(customerId);
    }

    public List<Orders> getInProgressOrdersByCustomer(Integer customerId) {
        return orderRepository.findInProgressByCustomerId(customerId);
    }

    public List<Orders> getCompletedOrdersByCustomer(Integer customerId) {
        return orderRepository.findCompletedByCustomerId(customerId);
    }

    // Method to save order and return ID for payment flow
    public Integer saveOrder(Orders order) {
        return orderRepository.save(order);
    }

    // Save order with items for payment flow
    public Orders saveOrderWithItems(Orders order, Map<Integer, Integer> cartItems) {
        try {
            // Save the order first
            Integer orderId = orderRepository.save(order);
            order.setId(orderId);

            // Save order items
            if (cartItems != null && !cartItems.isEmpty()) {
                for (Map.Entry<Integer, Integer> entry : cartItems.entrySet()) {
                    OrderItem item = new OrderItem();
                    item.setOrderId(orderId);
                    item.setClothId(entry.getKey());
                    item.setQuantity(entry.getValue());

                    // Get cloth details for price calculation
                    var cloth = clothesRepository.findById(entry.getKey());
                    if (cloth != null && cloth.getUnitPrice() != null) {
                        item.setUnitPrice(cloth.getUnitPrice().doubleValue());
                        item.setTotalPrice(cloth.getUnitPrice().doubleValue() * entry.getValue());
                    }

                    orderItemRepository.save(item);
                }
            }

            return order;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save order with items: " + e.getMessage(), e);
        }
    }

    // Save payment record
    public void savePayment(Payment payment) {
        try {
            // Set payment datetime if not already set
            if (payment.getPaymentDatetime() == null && payment.getPaymentTime() != null) {
                payment.setPaymentDatetime(payment.getPaymentTime());
            } else if (payment.getPaymentDatetime() == null) {
                payment.setPaymentDatetime(LocalDateTime.now());
            }
            
            // Set payment status if not already set
            if (payment.getPaymentStatus() == null && payment.getStatus() != null) {
                payment.setPaymentStatus(payment.getStatus());
            }
            
            // Use the PaymentRepository to save the payment to database
            int result = paymentRepository.save(payment);
            
            if (result > 0) {
                System.out.println("Payment successfully saved to database: Order ID=" + payment.getOrderId() +
                        ", Amount=" + payment.getAmount() +
                        ", Method=" + payment.getPaymentMethod());
            } else {
                throw new RuntimeException("Failed to save payment to database");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save payment: " + e.getMessage(), e);
        }
    }

    // Driver assignment methods for coordinator dashboard
    public boolean assignPickupDriver(Integer orderId, Long driverId) {
        return orderRepository.assignPickupDriver(orderId, driverId);
    }

    public boolean assignDeliveryDriver(Integer orderId, Long driverId) {
        return orderRepository.assignDeliveryDriver(orderId, driverId);
    }

    public boolean updateOrderStatus(Integer orderId, String status) {
        return orderRepository.updateOrderStatus(orderId, status);
    }
}