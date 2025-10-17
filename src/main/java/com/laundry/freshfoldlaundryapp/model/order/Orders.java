package com.laundry.freshfoldlaundryapp.model.order;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public class Orders {
    private Integer orderId;
    private Integer customerId;
    @NotNull(message = "Service Type is required")
    private String serviceType;
    @NotNull(message = "Pickup Date and Time is required")
    private LocalDateTime pickupDatetime;
    @NotNull(message = "Delivery Date and Time is required")
    private LocalDateTime deliveryDatetime;
    private LocalDateTime orderTime;
    private String status;
    private String clothType;
    private String specialInstructions;
    private Double price;

    // Customer fields for coordinator template compatibility
    private String customerName;
    private String customerPhone;
    private String customerAddress;

    // Driver assignment fields for pickup and delivery
    private Integer pickupDriverId;
    private Integer deliveryDriverId;

    // Additional fields for dashboard display
    private LocalDateTime orderDate;
    private LocalDate pickupDate;
    private LocalDate deliveryDate;

    // For form handling, not persisted directly
    private List<String> specialRequests;

    // Getters and Setters
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public LocalDateTime getPickupDatetime() { return pickupDatetime; }
    public void setPickupDatetime(LocalDateTime pickupDatetime) { this.pickupDatetime = pickupDatetime; }
    public LocalDateTime getDeliveryDatetime() { return deliveryDatetime; }
    public void setDeliveryDatetime(LocalDateTime deliveryDatetime) { this.deliveryDatetime = deliveryDatetime; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(List<String> specialRequests) { this.specialRequests = specialRequests; }

    public String getClothType() {
        return clothType;
    }

    public void setClothType(String clothType) {
        this.clothType = clothType;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // Compatibility methods for payment flow
    public Integer getId() {
        return orderId;
    }

    public void setId(Integer id) {
        this.orderId = id;
    }

    // Additional getters and setters for dashboard fields
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getPickupDriverId() {
        return pickupDriverId;
    }

    public void setPickupDriverId(Integer pickupDriverId) {
        this.pickupDriverId = pickupDriverId;
    }

    public Integer getDeliveryDriverId() {
        return deliveryDriverId;
    }

    public void setDeliveryDriverId(Integer deliveryDriverId) {
        this.deliveryDriverId = deliveryDriverId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}