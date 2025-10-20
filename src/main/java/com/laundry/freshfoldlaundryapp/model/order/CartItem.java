package com.laundry.freshfoldlaundryapp.model.order;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
public class CartItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "cloth_id", nullable = false)
    private Integer clothId;

    @Column(name = "cloth_name")
    private String clothName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "added_date")
    private LocalDateTime addedDate;

    @Column(name = "session_id")
    private String sessionId;

    // Constructors
    public CartItem() {
        this.addedDate = LocalDateTime.now();
    }

    public CartItem(Long userId, Integer clothId, String clothName, Integer quantity, Double unitPrice) {
        this();
        this.userId = userId;
        this.clothId = clothId;
        this.clothName = clothName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    // Business methods
    public void calculateTotalPrice() {
        if (this.quantity != null && this.unitPrice != null) {
            this.totalPrice = this.quantity * this.unitPrice;
        }
    }

    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
        calculateTotalPrice();
    }

    public void incrementQuantity() {
        this.quantity++;
        calculateTotalPrice();
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
            calculateTotalPrice();
        }
    }

    // Getters and Setters
    public Long getCartItemId() { return cartItemId; }
    public void setCartItemId(Long cartItemId) { this.cartItemId = cartItemId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getClothId() { return clothId; }
    public void setClothId(Integer clothId) { this.clothId = clothId; }

    public String getClothName() { return clothName; }
    public void setClothName(String clothName) { this.clothName = clothName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public LocalDateTime getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
}
