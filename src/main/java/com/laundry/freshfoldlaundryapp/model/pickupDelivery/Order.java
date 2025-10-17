package com.laundry.freshfoldlaundryapp.model.pickupDelivery;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name = "PickupDeliveryOrder")
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Column(name = "pickup_date")
    private LocalDate pickupDate;

    @Column(name = "pickup_time")
    private LocalTime pickupTime;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "delivery_time")
    private LocalTime deliveryTime;

    @Column(name = "status")
    private String status;

    @Column(name = "pickup_driver_id")
    private Integer pickupDriverId;

    @Column(name = "delivery_driver_id")
    private Integer deliveryDriverId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "cloth_type")
    private String clothType;

    @Column(name = "price")
    private Double price;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }

    public LocalTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalTime pickupTime) { this.pickupTime = pickupTime; }

    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }

    public LocalTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(LocalTime deliveryTime) { this.deliveryTime = deliveryTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPickupDriverId() { return pickupDriverId; }
    public void setPickupDriverId(Integer pickupDriverId) { this.pickupDriverId = pickupDriverId; }

    public Integer getDeliveryDriverId() { return deliveryDriverId; }
    public void setDeliveryDriverId(Integer deliveryDriverId) { this.deliveryDriverId = deliveryDriverId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getClothType() { return clothType; }
    public void setClothType(String clothType) { this.clothType = clothType; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    // Helper methods to work with Driver relationships
    public Driver getPickupDriver() {
        // This would need to be populated through service layer
        return null; // Placeholder - use service to get driver by pickupDriverId
    }

    public Driver getDeliveryDriver() {
        // This would need to be populated through service layer
        return null; // Placeholder - use service to get driver by deliveryDriverId
    }
}
