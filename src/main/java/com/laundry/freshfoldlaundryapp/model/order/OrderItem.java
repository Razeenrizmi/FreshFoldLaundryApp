package com.laundry.freshfoldlaundryapp.model.order;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private Integer orderId;
    private Integer clothId;
    private Integer quantity;

    // Additional fields for cart and display functionality
    private String clothName;
    private Double unitPrice;
    private Double totalPrice;

    // Default constructor
    public OrderItem() {}

    // Constructor with parameters
    public OrderItem(Integer clothId, String clothName, Integer quantity, Double unitPrice) {
        this.clothId = clothId;
        this.clothName = clothName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * quantity;
    }

    // Getters and Setters
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getClothId() { return clothId; }
    public void setClothId(Integer clothId) { this.clothId = clothId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        // Recalculate total price when quantity changes
        if (this.unitPrice != null) {
            this.totalPrice = this.unitPrice * quantity;
        }
    }

    public String getClothName() { return clothName; }
    public void setClothName(String clothName) { this.clothName = clothName; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        // Recalculate total price when unit price changes
        if (this.quantity != null) {
            this.totalPrice = unitPrice * this.quantity;
        }
    }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}