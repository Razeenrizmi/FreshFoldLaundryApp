package com.laundry.freshfoldlaundryapp.model.order;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

public class Clothes {
    private Integer clothId;
    private String name;
    private BigDecimal unitPrice;
    private Integer categoryId;
    private String imagePath;

    public Clothes() {}

    public Clothes(Integer clothId, String name, BigDecimal unitPrice, Integer categoryId, String imagePath) {
        this.clothId = clothId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.categoryId = categoryId;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public Integer getClothId() { return clothId; }
    public void setClothId(Integer clothId) { this.clothId = clothId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}